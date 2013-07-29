package br.eti.hussamaismail.scheduler.util;

import java.util.Collections;
import java.util.Comparator;

import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.Scheduler;
import br.eti.hussamaismail.scheduler.domain.StaticScheduler;


public class TasksUtil {

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
}
