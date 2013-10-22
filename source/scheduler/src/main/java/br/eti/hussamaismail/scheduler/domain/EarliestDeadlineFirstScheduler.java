package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import org.apache.log4j.Logger;

import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;
import br.eti.hussamaismail.scheduler.util.ChartsUtil;

/**
 * Classe responsavel por gerar o diagrama temporal para o algoritmo EDF
 * (Earliest Deadline First)
 * 
 * @author Hussama Ismail
 * 
 */
public class EarliestDeadlineFirstScheduler extends DynamicScheduler {

	private SporadicPolicy sporadicPolicy;
	private Chart serverCapacity;

	private Logger log = Logger.getLogger(EarliestDeadlineFirstScheduler.class);

	/**
	 * Metodo que realiza o teste de escalonabilidade para as tarefas periodicas
	 * adicionadas ao escalonador.
	 * 
	 * Para o EDF o numero 1 de P2 representa a utilizacao de 100% do
	 * processador.
	 * 
	 * P1 = SUM(C/T) P2 = 1
	 * 
	 * P1 <= P2
	 * 
	 * @return
	 */
	public Boolean isScalable() {

		log.debug("Inicializando teste de escalonabilidade");

		double p1 = 0, p2 = 1;

		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getTasksUtil()
				.getOnlyPeriodicTasksFromTaskList(this.getTasks());
		for (PeriodicTask pt : onlyPeriodicTasksFromTasks) {
			p1 = p1
					+ ((double) pt.getComputationTime() / ((double) pt
							.getPeriod()));
		}

		log.debug("P1: " + p1 + " | P2: " + p2);

		return p1 <= p2 ? true : false;
	}

	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException,
			SchedulabilityConditionNotSatisfiedException {

		if (this.isScalable() == false) {
			throw new SchedulabilityConditionNotSatisfiedException();
		}

		int position = 0;
		int higherPeriod = getTasksUtil().getHigherPeriodFromPeriodicTasks(
				getTasks());
		Map<Integer, List<PeriodicTask>> mapPeriods = getTasksUtil()
				.getMapWithPeriodsAndTasks(getTasks());
		Map<Integer, List<SporadicTask>> mapSporadicTasks = getMapWithSporadicTasksAndActivations((List<SporadicTask>) getTasksUtil()
				.getOnlySporadicTasksFromTaskList(getTasks()));

		List<PeriodicTask> pendentTasks = new ArrayList<PeriodicTask>();
		List<SporadicTask> pendentSporadicTasks = new ArrayList<SporadicTask>();

		/* Inicia o grafico do diagrama temporal */
		NumberAxis xAxis = new NumberAxis(0, higherPeriod, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,yAxis);
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		
		/* Inicia o grafico da capacidade do servidor */
		List<Series<Number, Number>> chartServerCapacity = null;
		AreaChart<Number, Number> serverCapacityAC = null;
		int actualServerCapacity = -1;
		PeriodicTask taskServer = null;

		if (SporadicPolicy.POLLING_SERVER.equals(getSporadicPolicy()) || SporadicPolicy.SPORADIC_SERVER.equals(getSporadicPolicy())){ 
			taskServer = getTasksUtil().getTaskServerFromTasks(getTasks());	
			NumberAxis xAxisSC = new NumberAxis(0, higherPeriod, 1);
			NumberAxis yAxisSC = new NumberAxis(0, taskServer.getComputationTime() + 1, 1);
			serverCapacityAC = new AreaChart<Number, Number>(xAxisSC, yAxisSC);
			chartServerCapacity = new ArrayList<Series<Number, Number>>();
			
			/* O Servidor sporadic ja inicia com o valor maximo */
			if (SporadicPolicy.SPORADIC_SERVER.equals(getSporadicPolicy())){
				Series<Number, Number> currentServerCapacity = getTasksUtil()
						.getChartTask(chartServerCapacity, taskServer);		
				currentServerCapacity.getData().add(
						new Data<Number, Number>(position, 0));
				currentServerCapacity.getData().add(
						new Data<Number, Number>(position, taskServer.getRemaining()));
						
				actualServerCapacity = taskServer.getComputationTime();
			}
		}
		
		Map<Integer,Integer> rechargerMap = new HashMap<Integer,Integer>();		
		while (position < higherPeriod) {

			/* Adiciona as tarefas periodicas a uma lista de tarefas pendentes */
			if (mapPeriods.containsKey(position)) {
				pendentTasks.addAll(mapPeriods.get(position));
			}

			/* Adiciona as tarefas esporadicas a uma lista de tarefas pendentes */
			if (mapSporadicTasks.containsKey(position)) {	
				List<SporadicTask> list = mapSporadicTasks.get(position);
				for (SporadicTask sporadicTask : list) {
					if (pendentSporadicTasks.contains(sporadicTask) == false){
						pendentSporadicTasks.add(sporadicTask);
					}
				}
			}
			
			/* SERVIDOR SPORADIC*/
			if (SporadicPolicy.SPORADIC_SERVER.equals(getSporadicPolicy())){
				
				/* Ignora as tarefas TS */
				PeriodicTask ts = null;
				if(pendentTasks.isEmpty() == false && (ts = pendentTasks.get(0)).getName().equals("TS")){
					pendentTasks.remove(ts);
					continue;
				}
				
				if (rechargerMap.containsKey(position)){
					Series<Number, Number> currentServerCapacity = getTasksUtil().getChartTask(chartServerCapacity, taskServer);		
					currentServerCapacity.getData().add(new Data<Number, Number>(position, actualServerCapacity));
					actualServerCapacity = actualServerCapacity + rechargerMap.get(position);
					currentServerCapacity.getData().add(new Data<Number, Number>(position, actualServerCapacity));
				}
					
				if (pendentSporadicTasks.isEmpty() == false){			
					SporadicTask sporadicTask = pendentSporadicTasks.get(0);
					if (actualServerCapacity >= sporadicTask.getRemaining()){
				
						int nextChargePosition = position + taskServer.getPeriod();
						
						Series<Number, Number> currentServerCapacity = getTasksUtil().getChartTask(chartServerCapacity, taskServer);		
						Series<Number, Number> currentChartTask = getTasksUtil().getChartTask(chartTasks, sporadicTask);

						currentChartTask.getData().add(new Data<Number, Number>(position, 0));
						currentChartTask.getData().add(new Data<Number, Number>(position, 1));
						
						currentServerCapacity.getData().add(new Data<Number, Number>(position, actualServerCapacity));						
											
						int taskRemaining = sporadicTask.getRemaining();
						position = position + taskRemaining;
						sporadicTask.process(taskRemaining);
						actualServerCapacity = actualServerCapacity - taskRemaining;								
						/* Adiciona no mapa o instante em que sera recarregado e para qual valor o server sera restaurado */
						rechargerMap.put(nextChargePosition, taskRemaining);
						
						currentChartTask.getData().add(new Data<Number, Number>(position, 1));
						currentChartTask.getData().add(new Data<Number, Number>(position, 0));
						
						currentServerCapacity.getData().add(new Data<Number, Number>(position, actualServerCapacity));

						if (sporadicTask.getRemaining() == 0){
							boolean remove = pendentSporadicTasks.remove(sporadicTask);
							System.out.println("removeu? " + remove);
						}
						continue;
					}
				}
			}			
			
			if (pendentTasks.isEmpty() == false) {
				
				PeriodicTask earliestDeadline = getEarliestDeadline(
						pendentTasks, position);

				if (earliestDeadline.getDeadline() <= position) {
					throw new DeadlineNotSatisfiedException(earliestDeadline, position);
				}
				
				/* Escalonamento da tarefa server */
				if (earliestDeadline.getName().equals("TS") && 
						SporadicPolicy.POLLING_SERVER.equals(getSporadicPolicy())){
					
					/* SERVIDOR DE POLLING */
					/* Verifica se existem tarefas esporadicas pendentes de processamento */
					/* Senao o servidor perde sua capacidade */
					if (pendentSporadicTasks.isEmpty() == false && SporadicPolicy.POLLING_SERVER.equals(getSporadicPolicy())){
						
							SporadicTask sporadicTask = pendentSporadicTasks.get(0);
							sporadicTask.process(1);
							
							Series<Number, Number> currentChartTask = getTasksUtil()
									.getChartTask(chartTasks, sporadicTask);
							currentChartTask.getData().add(
									new Data<Number, Number>(position, 0));
							currentChartTask.getData().add(
									new Data<Number, Number>(position, 1));
							
							Series<Number, Number> currentServerCapacity = getTasksUtil()
									.getChartTask(chartServerCapacity, earliestDeadline);		
							currentServerCapacity.getData().add(
									new Data<Number, Number>(position, 0));
							
							currentServerCapacity.getData().add(
									new Data<Number, Number>(position, earliestDeadline.getRemaining()));
							
							position = position + 1;
							earliestDeadline.process(1);
	
							currentServerCapacity.getData().add(
									new Data<Number, Number>(position, earliestDeadline.getRemaining()));						
							
							currentChartTask.getData().add(
									new Data<Number, Number>(position, 1));
							currentChartTask.getData().add(
									new Data<Number, Number>(position, 0));
								
							if (sporadicTask.getRemaining() == 0) {
								pendentSporadicTasks.remove(sporadicTask);
							}
							
							if (earliestDeadline.getRemaining() == 0) {
								pendentTasks.remove(earliestDeadline);
							}
							
					}else if (pendentSporadicTasks.isEmpty() == true && SporadicPolicy.POLLING_SERVER.equals(getSporadicPolicy())){
						/* Servidor Perde a sua capacidade por nao haver tarefas 
						 * esporadicas para execucao */
						pendentTasks.remove(earliestDeadline);
						Series<Number, Number> currentServerCapacity = getTasksUtil()
								.getChartTask(chartServerCapacity, earliestDeadline);
						currentServerCapacity.getData().add(
								new Data<Number, Number>(position, 0));
						log.debug("O Servidor SPORADIC perdeu sua capacidade de execução no instante : " + position);
					}
					
					continue;
				}
				
				/* Escalonamento das tarefas normais */				
				earliestDeadline.process(1);

				Series<Number, Number> currentChartTask = getTasksUtil()
						.getChartTask(chartTasks, earliestDeadline);
				currentChartTask.getData().add(
						new Data<Number, Number>(position, 0));
				currentChartTask.getData().add(
						new Data<Number, Number>(position, 1));
				position = position + 1;
				currentChartTask.getData().add(
						new Data<Number, Number>(position, 1));
				currentChartTask.getData().add(
						new Data<Number, Number>(position, 0));

				if (earliestDeadline.getRemaining() == 0) {
					pendentTasks.remove(earliestDeadline);
				}
				continue;
			} else {

				/* Tecnica de servidor de background */
				if (SporadicPolicy.BACKGROUND_SERVER
						.equals(getSporadicPolicy())) {
					if (pendentSporadicTasks.isEmpty() == false) {

						SporadicTask sporadicTask = pendentSporadicTasks.get(0);
						Series<Number, Number> currentChartTask = getTasksUtil()
								.getChartTask(chartTasks, sporadicTask);
						currentChartTask.getData().add(
								new Data<Number, Number>(position, 0));
						currentChartTask.getData().add(
								new Data<Number, Number>(position, 1));
						position = position + 1;
						currentChartTask.getData().add(
								new Data<Number, Number>(position, 1));
						currentChartTask.getData().add(
								new Data<Number, Number>(position, 0));
						sporadicTask.process(1);

						if (sporadicTask.getRemaining() == 0) {
							pendentSporadicTasks.remove(sporadicTask);
						}
						continue;
					}
				}
			}

			position = position + 1;
		}
		
		if (SporadicPolicy.SPORADIC_SERVER.equals(getSporadicPolicy())){
			Series<Number, Number> currentServerCapacity = getTasksUtil().getChartTask(chartServerCapacity, taskServer);		
			currentServerCapacity.getData().add(new Data<Number, Number>(position, actualServerCapacity));
		}

		ac.getData().addAll(chartTasks);		
		ChartsUtil.getInstance().chartReform(ac);
		
		if (serverCapacityAC != null){
			serverCapacityAC.getData().addAll(chartServerCapacity);
			this.serverCapacity = serverCapacityAC;
		}

		return ac;
	}

	/**
	 * Metodo que devolve a tarefa com menor deadline absoluto dentre as tarefas
	 * pendentes
	 * 
	 * @param tasks
	 * @param position
	 * @return
	 */
	private PeriodicTask getEarliestDeadline(List<PeriodicTask> pendentTasks,
			int position) {

		PeriodicTask earliestDeadlineTask = null;
		int earliestDeadline = Integer.MAX_VALUE, temp = 0;

		for (PeriodicTask pTask : pendentTasks) {
			if ((temp = (pTask.getDeadline() - position)) < earliestDeadline) {
				earliestDeadline = temp;
				earliestDeadlineTask = pTask;
			}
		}

		return earliestDeadlineTask;
	}

	/**
	 * Metodo que monta um mapa com todas as tarefas esporadicas e seus tempos
	 * de ativacao
	 * 
	 */
	private Map<Integer, List<SporadicTask>> getMapWithSporadicTasksAndActivations(
			List<SporadicTask> sporadicTasks) {

		Map<Integer, List<SporadicTask>> mapSporadics = new HashMap<Integer, List<SporadicTask>>();

		for (SporadicTask sporadicTask : sporadicTasks) {
			int activationTime = sporadicTask.getActivationTime();
			if (mapSporadics.containsKey(activationTime) == false) {
				List<SporadicTask> list = new ArrayList<SporadicTask>();
				list.add(sporadicTask);
				mapSporadics.put(activationTime, list);
			} else {
				mapSporadics.get(activationTime).add(sporadicTask);
			}
		}

		return mapSporadics;

	}

	public SporadicPolicy getSporadicPolicy() {
		return sporadicPolicy;
	}

	public void setSporadicPolicy(SporadicPolicy sporadicPolicy) {
		this.sporadicPolicy = sporadicPolicy;
	}
	
	public Chart getServerCapacity() {
		return serverCapacity;
	}
}
