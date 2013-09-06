package br.eti.hussamaismail.scheduler.domain;

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
		this.getTasksUtil().sortTasksByPeriod(this);
		super.simulate();
		double higherPeriod = getTasksUtil().getHigherPeriodFromPeriodicTasks(getTasks());
		Map<Integer, List<PeriodicTask>> mapWithPeriodsAndTasks = getTasksUtil().getMapWithPeriodsAndTasks(getTasks());			
		
		return generateMonotonicChart(mapWithPeriodsAndTasks, higherPeriod);		
	}
	
}
