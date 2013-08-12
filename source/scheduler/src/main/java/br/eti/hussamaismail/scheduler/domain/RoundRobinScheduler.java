package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class RoundRobinScheduler extends StaticScheduler {

	private TasksUtil tasksUtil;
	
	public RoundRobinScheduler() {
		this.tasksUtil = TasksUtil.getInstance();
	}
	
	@Override
	public Chart simulate() {
		
		double higherValue = tasksUtil.getHigherDeadlineFromPeriodicTasks(getTasks());
		
		NumberAxis xAxis = new NumberAxis(1,higherValue,1);
		NumberAxis yAxis = new NumberAxis(0,2,1);
		AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis, yAxis);
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
	
		long partTime = 10;
		List<PeriodicTask> pendentTasks = new ArrayList<PeriodicTask>(getTasks());
		double index = 0;
		
		while (!pendentTasks.isEmpty()){
			int qtd = pendentTasks.size();
			for (int i=0; i < qtd ; i++){
				PeriodicTask pTask = pendentTasks.get(i);
				Series<Number, Number> chartTask = this.tasksUtil.getChartTask(chartTasks, pTask);
				chartTask.getData().add(new Data<Number, Number>(index, 0));
				chartTask.getData().add(new Data<Number, Number>(index, 1));
				if (pTask.getRemaining() >= partTime){					
					index = index + partTime;
					pTask.process(partTime);		
				}else if (pTask.getRemaining() < partTime){
					double remaining = pTask.getRemaining();
					index = index + remaining;
					pTask.process(remaining);					
				}
				chartTask.getData().add(new Data<Number, Number>(index, 1));
				chartTask.getData().add(new Data<Number, Number>(index, 0));		
				
				if(pTask.getRemaining() == 0){
					pendentTasks.remove(pTask);
					qtd--;
					i--;
				}
			}
		}
		
		ac.getData().addAll(chartTasks);				
		return ac;
	}
}
