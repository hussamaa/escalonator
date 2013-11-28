/*
This file is part of Escalonator.

Escalonator is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Escalonator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Escalonator. If not, see <http://www.gnu.org/licenses/>.
*/
package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.Collection;
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

		ViolatedDeadlineEntity violatedDeadlineEntity = null;
		double higherValue = getTasksUtil().getHigherDeadlineFromPeriodicTasks(getTasks());

		NumberAxis xAxis = new NumberAxis(0, higherValue, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,
				yAxis);
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();

		int slotSize = 1;
		
		if (this.getSlotSize() != null){
			slotSize = this.getSlotSize();
		}
	
		Collection<PeriodicTask> onlyPeriodicTasksFromTask = getTasksUtil().getOnlyPeriodicTasksFromTaskList(getTasks());
		List<PeriodicTask> pendentTasks = new ArrayList<PeriodicTask>(onlyPeriodicTasksFromTask);
		int index = 0;

		while (!pendentTasks.isEmpty()) {
			int qtd = pendentTasks.size();
			for (int i = 0; i < qtd; i++) {
				PeriodicTask pTask = pendentTasks.get(i);
				Series<Number, Number> chartTask = getTasksUtil().getChartTask(
						chartTasks, pTask);
				chartTask.getData().add(new Data<Number, Number>(index, 0));
				chartTask.getData().add(new Data<Number, Number>(index, 1));
				
				if (index > pTask.getDeadline()){
					violatedDeadlineEntity = new ViolatedDeadlineEntity();
					violatedDeadlineEntity.setPosition(index);
					violatedDeadlineEntity.setTask(pTask);
					break;
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
			
			if (violatedDeadlineEntity != null){
				break;
			}
		}

		ac.getData().addAll(chartTasks);
		
		if (violatedDeadlineEntity != null){
			violatedDeadlineEntity.setGeneratedChart(ac);
			throw new DeadlineNotSatisfiedException(violatedDeadlineEntity);
		}
		
		return ac;
	}
}
