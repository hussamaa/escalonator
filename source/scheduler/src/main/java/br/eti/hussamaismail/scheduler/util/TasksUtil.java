package br.eti.hussamaismail.scheduler.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	public double getHigherDeadlineFromPeriodicTasks(List<PeriodicTask> tasks){
		
		double higherDeadline = 0;
		
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
	public double getHigherPeriodFromPeriodicTasks(List<PeriodicTask> tasks){
		
		double higherPeriod = 0;
		
		for (PeriodicTask pTask : tasks) {
			if (pTask.getPeriod() > higherPeriod){
				higherPeriod = pTask.getPeriod();
			}
		}
		
		return higherPeriod;		
	}
}
