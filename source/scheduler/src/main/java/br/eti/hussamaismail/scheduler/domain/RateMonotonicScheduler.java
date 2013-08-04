package br.eti.hussamaismail.scheduler.domain;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class RateMonotonicScheduler extends MonotonicScheduler {
	
	private TasksUtil taskUtil;
	
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
		NumberAxis xAxis = new NumberAxis(1,31,1);
		NumberAxis yAxis = new NumberAxis(0,2,1);
		
		AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis, yAxis);

		Series<Number, Number> a = new XYChart.Series<Number, Number>();		
		a.setName("t1");
		a.getData().add(new Data<Number, Number>(1,1));
		a.getData().add(new Data<Number, Number>(4,1));

		ac.getData().addAll(a);
		return ac;
	}
	
}
