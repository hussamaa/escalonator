package br.eti.hussamaismail.scheduler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.Scheduler;
import br.eti.hussamaismail.scheduler.domain.StaticScheduler;
import br.eti.hussamaismail.scheduler.domain.Task;

public class TasksUtil {

	private static TasksUtil instance;
	
	private TasksUtil() {
	}
	
	/**
	 * Metodo getInstance referente ao padrao de projeto 
	 * 'Singleton' aplicado a classe TaskUtil
	 * 
	 * @return
	 */
	public static TasksUtil getInstance(){
		if (instance == null){
			instance = new TasksUtil();
		}
		return instance;
	}
	
	/**
	 * Metodo que ordena a lista de tarefas de um  
	 * scheduler com base no seu deadline
	 * 
	 * @param scheduler
	 */
	public void sortTasksByDeadline(Scheduler scheduler){		
		
		if (!(scheduler instanceof StaticScheduler))
			return;
		
		StaticScheduler staticScheduler = (StaticScheduler) scheduler; 
		Collections.sort(staticScheduler.getTasks(), new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getDeadline(), pt2.getDeadline());
			}
		});		
	}
	
	/**
	 * Metodo que ordena a lista de tarefas de um  
	 * scheduler com base no seu tempo de computacao
	 * 
	 * @param scheduler
	 */
	public void sortTasksByComputationTime(Scheduler scheduler){		
		
		if (!(scheduler instanceof StaticScheduler))
			return;
		
		StaticScheduler staticScheduler = (StaticScheduler) scheduler; 
		Collections.sort(staticScheduler.getTasks(), new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getComputationTime(), pt2.getComputationTime());
			}
		});		
	}
	
	/**
	 * Metodo que ordena a lista de tarefas de um  
	 * scheduler com base no seu periodo
	 * 
	 * @param scheduler
	 */
	public void sortTasksByPeriod(Scheduler scheduler){		
		
		if (!(scheduler instanceof StaticScheduler))
			return;
		
		StaticScheduler staticScheduler = (StaticScheduler) scheduler; 
		Collections.sort(staticScheduler.getTasks(), new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getPeriod(), pt2.getPeriod());
			}
		});		
	}

	/**
	 * Metodo que compara dois valores e devolve
	 * o valor 1 caso o value1 seja maior que o value2,
	 * devolve o valor -1 caso value1 seja menor que value2 e 
	 * retorna 0 caso os dois sejam iguais
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private int compareValues(double value1, double value2){
		if (value1 > value2){
			return 1;
		}else if (value1 < value2){
			return -1;
		}else{
			return 0;
		}
	}
	
	/**
	 * Metodo que devolve uma lista de PeriodicTask
	 * a partir de uma lista de 'Tasks'.
	 * 
	 * @param tasks
	 * @return
	 */
	public List<PeriodicTask> getOnlyPeriodicTasksFromTaskList(List<Task> tasks){
	
		List<PeriodicTask> periodicTasks = new ArrayList<PeriodicTask>();
		
		for (Task task : tasks){
			if (task instanceof PeriodicTask){
				periodicTasks.add((PeriodicTask) task);
			}
		}
		
		return periodicTasks;
	}	
	
	/**
	 * Metodo que devolve o deadline mais alto
	 * dentre as tarefas periodicas existentes.
	 * 
	 * @param tasks
	 * @return
	 */
	public int getHigherDeadlineFromPeriodicTasks(List<PeriodicTask> tasks){
		
		int higherDeadline = 0;
		
		for (PeriodicTask pTask : tasks) {
			if (pTask.getDeadline() > higherDeadline){
				higherDeadline = pTask.getDeadline();
			}
		}
		
		return higherDeadline;		
	}
	
	/**
	 * Metodo que devolve o periodo mais alto
	 * dentre as tarefas periodicas existentes.
	 * 
	 * @param tasks
	 * @return
	 */
	public int getHigherPeriodFromPeriodicTasks(List<PeriodicTask> tasks){
		
		int higherPeriod = 0;
		
		for (PeriodicTask pTask : tasks) {
			if (pTask.getPeriod() > higherPeriod){
				higherPeriod = pTask.getPeriod();
			}
		}
		
		return higherPeriod;		
	}
	
	/**
	 * Metodo que retorna a representacao de uma
	 * determinada tarefa no grafico.
	 * 
	 * @param chartTasks
	 * @param pTask
	 * @return
	 */
	public Series<Number, Number> getChartTask(List<Series<Number, Number>>  chartTasks, Task task){
		
		Series<Number, Number> chartTask = null;
		
		for (Series<Number, Number> series : chartTasks) {
			if (series.getName().equals(task.getName())){
				chartTask = series;
				break;
			}
		}
		
		if (chartTask == null){
			chartTask = new XYChart.Series<Number, Number>();
			chartTask.setName(task.getName());
			chartTasks.add(chartTask);
		}
		
		return chartTask;
	}
	
	/**
	 * Metodo que calcula o tamanho da menor parte
	 * para geracao do grafico;
	 * 
	 * @param scheduler
	 * @return
	 */
	public double calculateMinPartSizeFromTasks(Scheduler scheduler){
		
		double tempPartSize = 0;
		double partSize = 1;
		
		if (scheduler instanceof StaticScheduler){
			StaticScheduler staticScheduler = (StaticScheduler) scheduler;
			for (PeriodicTask pTask : staticScheduler.getTasks()){
				tempPartSize = pTask.getComputationTime() - ((int) pTask.getComputationTime());
				if ((tempPartSize > 0) && (tempPartSize < partSize)){
					partSize = tempPartSize;
				}				
			}
		}
		
		return partSize;
	}
	
	/**
	 * Metodo que reseta todas as tarefas
	 * de uma determinada lista, zendo o valor
	 * de processamento das mesmas.
	 * 
	 * @param tasks
	 */
	public void resetAllTasks(List<Task> tasks){
		for (Task task : tasks) {
			if (task instanceof PeriodicTask){
				((PeriodicTask) task).reset();
			}
		}
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus tempos de ativacao no grafico.
	 * 
	 * @return
	 */
	public Map<Integer, List<PeriodicTask>> getMapWithActivationTimeAndTasks(List<PeriodicTask> tasks){
		
		Map<Integer, List<PeriodicTask>> mapActivationTime = new HashMap<Integer, List<PeriodicTask>>();
		
		for (PeriodicTask periodicTask : tasks) {
			int activationTime = periodicTask.getActivationTime();
			if (mapActivationTime.containsKey(activationTime) == false){
				List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
				periodTaskList.add(periodicTask);
				mapActivationTime.put(activationTime, periodTaskList);
			}else{
				mapActivationTime.get(activationTime).add(periodicTask);
			}
		}
		
		return mapActivationTime;
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus periodos do grafico.
	 * 
	 * Ex:
	 * Task1 - Computation = 6, Periodo = 25.
     * Task2 - Computation = 6, Periodo = 50.
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
	public Map<Integer, List<PeriodicTask>> getMapWithPeriodsAndTasks(List<PeriodicTask> tasks){
		
		Map<Integer, List<PeriodicTask>> mapPeriods = new HashMap<Integer,List<PeriodicTask>>();
		mapPeriods.put(0, tasks);
		int higherPeriodFromPeriodicTasks = this.getHigherPeriodFromPeriodicTasks(tasks);
		
		for (PeriodicTask periodicTask : tasks) {
			int periodAccumulator = periodicTask.getPeriod();
			while (periodAccumulator <= higherPeriodFromPeriodicTasks){
				if (mapPeriods.containsKey(periodAccumulator) == false){
					List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
					periodTaskList.add(periodicTask.clone());
					mapPeriods.put(periodAccumulator, periodTaskList);
				}else{
					mapPeriods.get(periodAccumulator).add(periodicTask.clone());
				}
				periodAccumulator = periodAccumulator + periodicTask.getPeriod();
			}	
		}
		
		return mapPeriods;
	}
}
