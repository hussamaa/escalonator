package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.Chart;
import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;

public class DeadlineMonotonicScheduler extends MonotonicScheduler {


	@Override
	public void calculateMaximumResponseTimeToTheTasks()
			throws TaskNotScalableException {	
		this.getTasksUtil().sortTasksByPeriod(this);
		super.calculateMaximumResponseTimeToTheTasks();
	}
	
	@Override
	public Chart simulate() {
		
		super.simulate();
		
		double higherDeadline = getTasksUtil().getHigherDeadlineFromPeriodicTasks(getTasks());
		Map<Double, List<PeriodicTask>> mapWithPeriodsAndTasks = getMapWithDeadlinesAndTasks();			
		
		return generateMonotonicChart(mapWithPeriodsAndTasks, higherDeadline);		
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
		double higherDeadlineFromPeriodicTasks = getTasksUtil().getHigherDeadlineFromPeriodicTasks(tasks);
		
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
