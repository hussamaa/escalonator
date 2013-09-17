package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;

/**
 * Classe que implementa o comportamento do
 * escalonador com tecnica RoundRobin.
 * 
 * @author Hussama Ismail
 *
 */
public class RoundRobinScheduler extends DynamicScheduler {

	private Integer slotSize;

	public RoundRobinScheduler() {
	}

	public Integer getSlotSize() {
		return slotSize;
	}

	public void setSlotSize(Integer slotSize) {
		this.slotSize = slotSize;
	}

	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException {

		double higherValue = getTasksUtil().getHigherDeadlineFromPeriodicTasks(getTasks());

		NumberAxis xAxis = new NumberAxis(0, higherValue * 2, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,
				yAxis);
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();

		int slotSize = 1;
		
		if (this.getSlotSize() != null){
			slotSize = this.getSlotSize();
		}
		
		List<PeriodicTask> pendentTasks = new ArrayList<PeriodicTask>(getTasks());
		double index = 0;

		while (!pendentTasks.isEmpty()) {
			int qtd = pendentTasks.size();
			for (int i = 0; i < qtd; i++) {
				PeriodicTask pTask = pendentTasks.get(i);
				Series<Number, Number> chartTask = getTasksUtil().getChartTask(
						chartTasks, pTask);
				chartTask.getData().add(new Data<Number, Number>(index, 0));
				chartTask.getData().add(new Data<Number, Number>(index, 1));
				
				if (index > pTask.getDeadline()){
					throw new DeadlineNotSatisfiedException(pTask);
				}
				
				if (pTask.getRemaining() >= slotSize) {
					index = index + slotSize;
					pTask.process(slotSize);
				} else if (pTask.getRemaining() < slotSize) {
					int remaining = pTask.getRemaining();
					index = index + remaining;
					pTask.process(remaining);
				}
				chartTask.getData().add(new Data<Number, Number>(index, 1));
				chartTask.getData().add(new Data<Number, Number>(index, 0));

				if (pTask.getRemaining() == 0) {
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
