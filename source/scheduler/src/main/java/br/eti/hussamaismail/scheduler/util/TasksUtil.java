package br.eti.hussamaismail.scheduler.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.domain.DynamicScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.Scheduler;
import br.eti.hussamaismail.scheduler.domain.SporadicTask;
import br.eti.hussamaismail.scheduler.domain.Task;

/**
 * Classe responsavel por ter metodos
 * auxiliares para os escalonadores.
 * 
 * Ex: metodos de ordenacao de tarefas,
 * de geracao de mapas de ativacao, entre outras.
 * 
 * @author Hussama Ismail
 *
 */
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
		
		if (!(scheduler instanceof DynamicScheduler))
			return;
		
		DynamicScheduler dynamicScheduler = (DynamicScheduler) scheduler; 
		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(dynamicScheduler.getTasks());
		Collections.sort(onlyPeriodicTasksFromTasks, new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getDeadline(), pt2.getDeadline());
			}
		});		
		
		List<SporadicTask> onlySporadicTasksFromTasks = (List<SporadicTask>) getOnlySporadicTasksFromTaskList(dynamicScheduler.getTasks());

		dynamicScheduler.getTasks().clear();
		dynamicScheduler.getTasks().addAll(onlyPeriodicTasksFromTasks);
		dynamicScheduler.getTasks().addAll(onlySporadicTasksFromTasks);		
	}
	
	/**
	 * Metodo que ordena a lista de tarefas de um  
	 * scheduler com base no seu tempo de computacao
	 * 
	 * @param scheduler
	 */
	public void sortTasksByComputationTime(Scheduler scheduler){		
		
		if (!(scheduler instanceof DynamicScheduler))
			return;
		
		DynamicScheduler dynamicScheduler = (DynamicScheduler) scheduler; 
		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(dynamicScheduler.getTasks());		
		Collections.sort(onlyPeriodicTasksFromTasks, new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getComputationTime(), pt2.getComputationTime());
			}
		});		
		
		List<SporadicTask> onlySporadicTasksFromTasks = (List<SporadicTask>) getOnlySporadicTasksFromTaskList(dynamicScheduler.getTasks());

		dynamicScheduler.getTasks().clear();
		dynamicScheduler.getTasks().addAll(onlyPeriodicTasksFromTasks);
		dynamicScheduler.getTasks().addAll(onlySporadicTasksFromTasks);		
	}
	
	/**
	 * Metodo que ordena a lista de tarefas de um  
	 * scheduler com base no seu periodo
	 * 
	 * @param scheduler
	 */
	public void sortTasksByPeriod(Scheduler scheduler){		
		
		if (!(scheduler instanceof DynamicScheduler))
			return;
		
		DynamicScheduler dynamicScheduler = (DynamicScheduler) scheduler; 
		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(dynamicScheduler.getTasks());		
		Collections.sort(onlyPeriodicTasksFromTasks, new Comparator<PeriodicTask>() {
			public int compare(PeriodicTask pt1, PeriodicTask pt2) {
				return compareValues(pt1.getPeriod(), pt2.getPeriod());
			}
		});		
		
		List<SporadicTask> onlySporadicTasksFromTasks = (List<SporadicTask>) getOnlySporadicTasksFromTaskList(dynamicScheduler.getTasks());

		dynamicScheduler.getTasks().clear();
		dynamicScheduler.getTasks().addAll(onlyPeriodicTasksFromTasks);
		dynamicScheduler.getTasks().addAll(onlySporadicTasksFromTasks);	
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
	public Collection<PeriodicTask> getOnlyPeriodicTasksFromTaskList(Collection<Task> tasks){
	
		Collection<PeriodicTask> periodicTasks = null ;
		
		if (tasks instanceof List){
			periodicTasks = new ArrayList<PeriodicTask>();
		}
		if (tasks instanceof Set){
			periodicTasks = new HashSet<PeriodicTask>();
		}
		
		for (Task task : tasks){
			if (task instanceof PeriodicTask){
				periodicTasks.add((PeriodicTask) task);
			}
		}
		
		return periodicTasks;
	}
	
	/**
	 * Metodo que devolve uma lista de SporadicTask
	 * a partir de uma lista de 'Tasks'.
	 * 
	 * @param tasks
	 * @return
	 */
	public Collection<SporadicTask> getOnlySporadicTasksFromTaskList(Collection<Task> tasks){
	
		Collection<SporadicTask> sporadicTasks = null ;
		
		if (tasks instanceof List){
			sporadicTasks = new ArrayList<SporadicTask>();
		}
		if (tasks instanceof Set){
			sporadicTasks = new HashSet<SporadicTask>();
		}
		
		for (Task task : tasks){
			if (task instanceof SporadicTask){
				sporadicTasks.add((SporadicTask) task);
			}
		}
		
		return sporadicTasks;
	}	
	
	/**
	 * Metodo que devolve o deadline mais alto
	 * dentre as tarefas periodicas existentes.
	 * 
	 * @param tasks
	 * @return
	 */
	public int getHigherDeadlineFromPeriodicTasks(List<Task> tasks){
		
		int higherDeadline = 0;
		
		for (Task task : tasks) {
			if (task instanceof PeriodicTask){
				PeriodicTask pTask = (PeriodicTask) task;
				if (pTask.getDeadline() > higherDeadline){
					higherDeadline = pTask.getDeadline();
				}
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
	public int getHigherPeriodFromPeriodicTasks(List<Task> tasks){
		
		int higherPeriod = 0;
		
		for (Task task : tasks) {
			if (task instanceof PeriodicTask){
				PeriodicTask pTask = (PeriodicTask) task;
				if (pTask.getPeriod() > higherPeriod){
					higherPeriod = pTask.getPeriod();
				}
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
		
		if (scheduler instanceof DynamicScheduler){
			DynamicScheduler staticScheduler = (DynamicScheduler) scheduler;
			for (Task task : staticScheduler.getTasks()){
				if (task instanceof PeriodicTask){
					PeriodicTask pTask = (PeriodicTask) task;
					tempPartSize = pTask.getComputationTime() - ((int) pTask.getComputationTime());
					if ((tempPartSize > 0) && (tempPartSize < partSize)){
						partSize = tempPartSize;
					}
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
			task.reset();
		}
	}
	
	/**
	 * Metodo que retorna a Task Server
	 * utilizada pelas politicas de tarefas
	 * esporadicas.
	 * 
	 * @param tasks
	 */
	public PeriodicTask getTaskServerFromTasks(List<Task> tasks){
		PeriodicTask taskServer = null;
		
		for (Task task : tasks) {
			if (task instanceof PeriodicTask && task.getName().equals("TS")){
				taskServer = (PeriodicTask) task;
				break;
			}
		}
		
		return taskServer;
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus tempos de ativacao no grafico.
	 * 
	 * @return
	 */
	public Map<Integer, List<Task>> getMapWithActivationTimeAndTasks(List<Task> tasks){
		
		Map<Integer, List<Task>> mapActivationTime = new HashMap<Integer, List<Task>>();
		
		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(tasks);
		for (PeriodicTask periodicTask : onlyPeriodicTasksFromTasks) {
			int activationTime = periodicTask.getActivationTime();
			PeriodicTask clone = periodicTask.clone();
			clone.setDeadline(periodicTask.getDeadline() + activationTime);
			if (mapActivationTime.containsKey(activationTime) == false){
				List<Task> periodTaskList = new ArrayList<Task>();
				periodTaskList.add(clone);
				mapActivationTime.put(activationTime, periodTaskList);
			}else{
				mapActivationTime.get(activationTime).add(clone);
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
	 * Map<Integer,List<PeriodicTask>> - Mapa gerado:
	 * 
	 *  0 = Task1, Task2
	 *	25 = Task1,
	 *  50 = Task1, Task2,
	 *  75 = Task1,
	 *  100 = Task1, Task2,
	 * 
	 * @return
	 */
	public Map<Integer, List<PeriodicTask>> getMapWithPeriodsAndTasks(List<Task> tasks){
		
		Map<Integer, List<PeriodicTask>> mapPeriods = new HashMap<Integer,List<PeriodicTask>>();
		
		int higherPeriodFromPeriodicTasks = this.getHigherPeriodFromPeriodicTasks(tasks);
		
		List<PeriodicTask> onlyPeriodicTasksFromTaskList = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(tasks);
		for (PeriodicTask periodicTask : onlyPeriodicTasksFromTaskList) {

			/* Cria a primeira tarefa no periodo de ativacao setado pelo usuario */
			PeriodicTask activationTask = periodicTask.clone();
			activationTask.setDeadline(activationTask.getDeadline() + activationTask.getActivationTime());
			if (mapPeriods.containsKey(activationTask.getActivationTime()) == false){
				List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
				periodTaskList.add(activationTask);
				mapPeriods.put(activationTask.getActivationTime(), periodTaskList);
			}else{
				mapPeriods.get(activationTask.getActivationTime()).add(activationTask);
			}
			
			/* Percorre ate o ultimo maior periodo adicionando todas as ativacoes  */
			int periodAccumulator = periodicTask.getActivationTime() + periodicTask.getPeriod();
			while (periodAccumulator <= higherPeriodFromPeriodicTasks){
				PeriodicTask pTaskClone = periodicTask.clone();
				if (mapPeriods.containsKey(periodAccumulator) == false){
					List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
					pTaskClone.setDeadline(pTaskClone.getDeadline() + periodAccumulator);
					periodTaskList.add(pTaskClone);
					mapPeriods.put(periodAccumulator, periodTaskList);
				}else{
					pTaskClone.setDeadline(pTaskClone.getDeadline() + periodAccumulator);
					mapPeriods.get(periodAccumulator).add(pTaskClone);
				}
				periodAccumulator = periodAccumulator + periodicTask.getPeriod();
			}	
		}
		
		return mapPeriods;
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus deadlines do grafico.
	 * 
	 * Ex:
	 * Task1 - Computation = 6, Deadline = 25.
     * Task2 - Computation = 6, Deadline = 50.
	 * 
	 * Map<Integer,List<PeriodicTask>> - Mapa gerado:
	 * 
	 *  0 = Task1, Task2
	 *	25 = Task1,
	 *  50 = Task1, Task2,
	 *  75 = Task1,
	 *  100 = Task1, Task2,
	 * 
	 * @return
	 */
	public Map<Integer, List<Task>> getMapWithDeadlinesAndTasks(List<Task> tasks){
		
		Map<Integer, List<Task>> mapDeadLines = new HashMap<Integer,List<Task>>();
		mapDeadLines.put(0, tasks);
		int higherDeadlineFromPeriodicTasks = this.getHigherDeadlineFromPeriodicTasks(tasks);
		
		List<PeriodicTask> onlyPeriodicTasksFromTaskList = (List<PeriodicTask>) getOnlyPeriodicTasksFromTaskList(tasks);
		for (PeriodicTask periodicTask : onlyPeriodicTasksFromTaskList) {
			int deadlineAccumulator = periodicTask.getDeadline();
			while (deadlineAccumulator <= higherDeadlineFromPeriodicTasks){
				if (mapDeadLines.containsKey(deadlineAccumulator) == false){
					List<Task> deadLineTaskList = new ArrayList<Task>();
					PeriodicTask pTaskClone = periodicTask.clone();
					pTaskClone.setDeadline(pTaskClone.getDeadline() + deadlineAccumulator);
					deadLineTaskList.add(pTaskClone);
					mapDeadLines.put(deadlineAccumulator, deadLineTaskList);
				}else{
					PeriodicTask pTaskClone = periodicTask.clone();
					pTaskClone.setDeadline(pTaskClone.getDeadline() + deadlineAccumulator);
					mapDeadLines.get(deadlineAccumulator).add(pTaskClone);
				}
				deadlineAccumulator = deadlineAccumulator + periodicTask.getDeadline();
			}	
		}
		
		return mapDeadLines;
	}
	
	/**
	 * Metodo que verifica se existe uma tarefa 
	 * dentro de uma lista de tarefas a partir do seu
	 * nome.
	 * 
	 * @param taskName
	 * @param taskList
	 * @return
	 */
	public boolean existTaskInTaskList(String taskName, List<Task> taskList){
		for (Task task : taskList) {
			if(task.getName().equals(taskName)){
				return true;
			}
		}
		return false;
	}
}
