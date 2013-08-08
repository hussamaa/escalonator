package br.eti.hussamaismail.scheduler.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import org.apache.log4j.Logger;

import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public abstract class MonotonicScheduler extends StaticScheduler {

	private Logger log = Logger.getLogger(MonotonicScheduler.class);
	private static final int MAX_CONVERGENCE_ATTEMPTS = 10;
	private boolean preemptive;
	private TasksUtil tasksUtil;

	public TasksUtil getTasksUtil() {
		return tasksUtil;
	}

	public MonotonicScheduler() {
		this.tasksUtil = TasksUtil.getInstance();
	}
	
	public boolean isPreemptive() {
		return preemptive;
	}

	public void setPreemptive(boolean preemptive) {
		this.preemptive = preemptive;
	}

	@Override
	public Chart simulate() {
		
		/* Verifica se é possível escalonar com a técnica desejada */
		if (isScalable() == false){
			log.debug("Estas tarefas não passaram no teste de escalonabilidae com '" + getClass().getName() + "', efetuando calculo do tempo máximo de resposta");
			try {
				calculateMaximumResponseTimeToTheTasks();
			} catch (TaskNotScalableException e) {
				log.debug("Não Foi possivel efetuar o calculo do tempo máximo de resposta. Abortando Processamento");
				return null;
			}
		}

		return null;
	}
	
	/**
	 * Metodo que realiza o teste de escalonabilidade para as tarefas periodicas
	 * adicionadas ao escalonador.
	 * 
	 * Este metodo por padrao o teste do RateMonotonic que consiste na formula
	 * matematica:
	 * 
	 * P1 = SUM(C/T) P2 = N * (2^(1/N) - 1)
	 * 
	 * P1 <= P2
	 * 
	 * @return
	 */
	public Boolean isScalable() {

		log.debug("Inicializando teste de escalonabilidade");

		double p1 = 0, p2;

		for (PeriodicTask pt : this.getTasks()) {
			p1 = p1 + (pt.getComputationTime() / pt.getPeriod());
		}

		p2 = this.getTasks().size()
				* (Math.pow(2, (double) 1 / this.getTasks().size()) - 1);

		log.debug("P1: " + p1 + " | P2: " + p2);

		return p1 <= p2 ? true : false;
	}

	/**
	 * Metodo que calcula o tempo maximo de resposta para cada tarefa presente
	 * no escalonador estatico
	 * 
	 * @throws TaskNotScalableException
	 * 
	 */
	public void calculateMaximumResponseTimeToTheTasks()
			throws TaskNotScalableException {

		if ((this.getTasks() == null) && (this.getTasks().isEmpty())) {
			log.debug("Não existem tarefas adicionadas ao escalonador");
			return;
		}

		for (int i = (this.getTasks().size() - 1); i >= 0; i--) {
			PeriodicTask actualTask = this.getTasks().get(i);
			log.debug("Calculando tempo máximo de resposta para tarefa: "
					+ actualTask.getName() + " - " + actualTask);
			double actualValue = actualTask.getComputationTime();
			int attempts = 0;
			while (true) {
				double accumulator = 0;
				for (int j = 0; j < i; j++) {
					PeriodicTask tempTask = this.getTasks().get(j);
					accumulator = accumulator
							+ Math.ceil(actualValue / tempTask.getPeriod())
							* tempTask.getComputationTime();
				}
				double newValue = actualTask.getComputationTime() + accumulator;
				if (newValue != actualValue) {
					actualValue = newValue;
				} else {
					break;
				}
				attempts++;
				if (attempts == MAX_CONVERGENCE_ATTEMPTS) {
					throw new TaskNotScalableException(actualTask);
				}
			}
			log.debug("O Tempo máximo de resposta da tarefa '"
					+ actualTask.getName() + "' é: " + actualValue);
			actualTask.setResponseTime(actualValue);
		}
	}
	
	public AreaChart<Number,Number> generateMonotonicChart(Map<Double, List<PeriodicTask>> mapTaskEvent, double higherValue){
		
		/* Geração padrão dos gráficos com representação do processador livre */
		NumberAxis xAxis = new NumberAxis(1,higherValue,1);
		NumberAxis yAxis = new NumberAxis(0,1,1);
		AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis, yAxis);
		
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		Series<Number, Number> lazy = new XYChart.Series<Number, Number>();		
		lazy.setName("Processador Livre");
		lazy.getData().add(new Data<Number, Number>(1,1));
		lazy.getData().add(new Data<Number, Number>(higherValue,1));
		chartTasks.add(lazy);

		double partSize = 0.25;
		double position = 0;	
		
		if (isPreemptive() == false) { 
			while (position <= higherValue){
				if (mapTaskEvent.containsKey(position)){
					double containsPosition = position;
					List<PeriodicTask> actualExecutionTasks = mapTaskEvent.get(position);
					List<PeriodicTask> pendentTasks = null;
					for (PeriodicTask pTask : actualExecutionTasks) {
						Series<Number, Number> tempTask = this.tasksUtil.getChartTask(chartTasks, pTask);		
						tempTask.getData().add(new Data<Number, Number>(position,0));					
						for (double j=0; j < pTask.getComputationTime(); j = j + partSize){
							if (containsPosition != position && mapTaskEvent.containsKey(position)){
								pendentTasks = mapTaskEvent.get(position);
							}
							tempTask.getData().add(new Data<Number, Number>(position,1));
							position = new BigDecimal(position + partSize).setScale(2,RoundingMode.UP).doubleValue();
						}
						tempTask.getData().add(new Data<Number, Number>(position,0));					
					}
					if (pendentTasks != null){
						for (PeriodicTask pTask : pendentTasks) {
							Series<Number, Number> tempTask = this.tasksUtil.getChartTask(chartTasks, pTask);
							tempTask.getData().add(new Data<Number, Number>(position,0));						
							for (double j=0; j < pTask.getComputationTime(); j = j + partSize){
								tempTask.getData().add(new Data<Number, Number>(position,1));
								position = position + partSize;
							}
							tempTask.getData().add(new Data<Number, Number>(position,0));						
						}
					}
				}
				position = position + partSize;
			}			
		}
		
		ac.getData().addAll(chartTasks);		
		return ac;
	}
}
