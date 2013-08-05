package br.eti.hussamaismail.scheduler.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class RateMonotonicScheduler extends MonotonicScheduler {
	
	private TasksUtil taskUtil;
	private Logger log = LoggerFactory.getLogger(RateMonotonicScheduler.class);
	
	public RateMonotonicScheduler() {
		super();
		this.taskUtil = TasksUtil.getInstance();
	}

	@Override
	public void calculateMaximumResponseTimeToTheTasks()
			throws TaskNotScalableException {
	
		this.taskUtil.sortTasksByPeriod(this);
		super.calculateMaximumResponseTimeToTheTasks();
	}
	
	@Override
	public Chart simulate() {
		
		/* Verifica se é possível escalonar com a técnica desejada */
		if (isScalable() == false){
			log.debug("Estas tarefas não passaram no teste de escalonabilidae com 'Rate Monotonic', efetuando calculo do tempo máximo de resposta");
			try {
				calculateMaximumResponseTimeToTheTasks();
			} catch (TaskNotScalableException e) {
				log.debug("Não Foi possivel efetuar o calculo do tempo máximo de resposta. Abortando Processamento");
				return null;
			}
		}
		
		double higherDeadline = taskUtil.getHigherDeadlineFromPeriodicTasks(getTasks());
		/* Geração padrão dos gráficos com representação do processador livre */
		NumberAxis xAxis = new NumberAxis(1,higherDeadline,1);
		NumberAxis yAxis = new NumberAxis(0,2,1);
		AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis, yAxis);
		
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		Series<Number, Number> lazy = new XYChart.Series<Number, Number>();		
		lazy.setName("Processador Livre");
		lazy.getData().add(new Data<Number, Number>(1,1));
		lazy.getData().add(new Data<Number, Number>(higherDeadline,1));
		chartTasks.add(lazy);
		
		Map<Double, List<PeriodicTask>> mapWithDeadlinesAndTasks = getMapWithDeadlinesAndTasks();
		double index = 0;
		double partSize = 0.05;
		double aux = 0;
		
		List<PeriodicTask> list = mapWithDeadlinesAndTasks.get(index);
		double currentIndex = 0;
		for (PeriodicTask periodicTask : list) {
			Series<Number, Number> tempTask = new XYChart.Series<Number, Number>();		
			tempTask.setName(periodicTask.getName());
			boolean started = false;
			double processRemaining = periodicTask.getProcessRemaining();
			
			while (aux <= (index + processRemaining)){
				currentIndex = aux;
				
				if((started == true) && mapWithDeadlinesAndTasks.containsKey(currentIndex)){
					break;
				}
				
				tempTask.getData().add(new Data<Number, Number>(currentIndex,1));
				tempTask.getData().add(new Data<Number, Number>(currentIndex,0));
				periodicTask.process(partSize);
				started = true;
				aux = new BigDecimal(aux + partSize).setScale(2, RoundingMode.HALF_UP).doubleValue();
			}
			chartTasks.add(tempTask);
			index = currentIndex;
		}				

		ac.getData().addAll(chartTasks);
		return ac;
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus deadlines do grafico.
	 * 
	 * Ex:
	 * Task1 - Computation = 6.25, Deadline = 25.
     * Task2 - Computation = 6.25, Deadline = 50.
	 * 
	 * Map<Double,List<PeriodicTask>> - Mapa gerado:
	 * 
	 *  0 = Task1, Task2
	 *	25 = Task1,
	 *  50 = Task1, Task2,
	 *  75 = Task1,
	 *  100 = Task1, Task2,
	 * 
	 * @return
	 */
	private Map<Double, List<PeriodicTask>> getMapWithDeadlinesAndTasks(){
		
		Map<Double, List<PeriodicTask>> mapDeadLines = new HashMap<Double,List<PeriodicTask>>();
		mapDeadLines.put(0d, this.getTasks());
		List<PeriodicTask> tasks = getTasks();
		double higherDeadlineFromPeriodicTasks = this.taskUtil.getHigherDeadlineFromPeriodicTasks(tasks);
		
		for (PeriodicTask periodicTask : tasks) {
			double deadlineAccumulator = periodicTask.getDeadline();
			while (deadlineAccumulator <= higherDeadlineFromPeriodicTasks){
				if (mapDeadLines.containsKey(deadlineAccumulator) == false){
					List<PeriodicTask> deadLineTaskList = new ArrayList<PeriodicTask>();
					deadLineTaskList.add(periodicTask);
					mapDeadLines.put(deadlineAccumulator, deadLineTaskList);
				}else{
					mapDeadLines.get(deadlineAccumulator).add(periodicTask);
				}
				deadlineAccumulator = deadlineAccumulator + periodicTask.getDeadline();
			}	
		}
		
		return mapDeadLines;
	}
	
}
