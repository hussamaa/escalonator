package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.Chart;
import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;

public class RateMonotonicScheduler extends MonotonicScheduler {
	
	@Override
	public void calculateMaximumResponseTimeToTheTasks()
			throws TaskNotScalableException {	
		this.getTasksUtil().sortTasksByPeriod(this);
		super.calculateMaximumResponseTimeToTheTasks();
	}
	
	@Override
	public Chart simulate() {
		
		super.simulate();
		
		double higherPeriod = getTasksUtil().getHigherPeriodFromPeriodicTasks(getTasks());
		Map<Double, List<PeriodicTask>> mapWithPeriodsAndTasks = getMapWithPeriodsAndTasks();			
		
		return generateMonotonicChart(mapWithPeriodsAndTasks, higherPeriod);		
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus periodos do grafico.
	 * 
	 * Ex:
	 * Task1 - Computation = 6.25, Periodo = 25.
     * Task2 - Computation = 6.25, Periodo = 50.
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
	private Map<Double, List<PeriodicTask>> getMapWithPeriodsAndTasks(){
		
		Map<Double, List<PeriodicTask>> mapPeriods = new HashMap<Double,List<PeriodicTask>>();
		mapPeriods.put(0d, this.getTasks());
		List<PeriodicTask> tasks = getTasks();
		double higherPeriodFromPeriodicTasks = getTasksUtil().getHigherPeriodFromPeriodicTasks(tasks);
		
		for (PeriodicTask periodicTask : tasks) {
			double periodAccumulator = periodicTask.getPeriod();
			while (periodAccumulator <= higherPeriodFromPeriodicTasks){
				if (mapPeriods.containsKey(periodAccumulator) == false){
					List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
					periodTaskList.add(periodicTask);
					mapPeriods.put(periodAccumulator, periodTaskList);
				}else{
					mapPeriods.get(periodAccumulator).add(periodicTask);
				}
				periodAccumulator = periodAccumulator + periodicTask.getPeriod();
			}	
		}
		
		return mapPeriods;
	}
	
}
