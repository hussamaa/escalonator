package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;

/**
 * Classe que implementa o comportamento do
 * escalonador LeastLaxity.
 * 
 * Atualmente este escalonador trabalha apenas
 * com tempos de ativacao, ficando como atividade 
 * futura a implementacao do mecanismo por periodo.
 * 
 * @author Hussama Ismail
 *
 */
public class LeastLaxityScheduler extends DynamicScheduler {

	private Integer slotSize;

	public LeastLaxityScheduler() {
		super();
	}
	
	public void setSlotSize(Integer slotSize) {
		this.slotSize = slotSize;
	}
	
	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException {
		
		Map<Integer, List<Task>> mapWithActivationTimeAndTasks = getTasksUtil().getMapWithActivationTimeAndTasks(getTasks());
		int position = 0;
		int higherValue = getTasksUtil().getHigherDeadlineFromPeriodicTasks(getTasks());
		
		NumberAxis xAxis = new NumberAxis(0, higherValue, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,yAxis);
		
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		Set<Task> currentTasks = new LinkedHashSet<Task>();
		
		int slotSize = 1;
		if (this.slotSize != null){
			slotSize = this.slotSize;
		}
	
		PeriodicTask previouslyLeastLaxity = null;
		
		while (position <= higherValue){
			
			if (mapWithActivationTimeAndTasks.containsKey(position)){
				List<Task> pendentTasks = mapWithActivationTimeAndTasks.get(position);
				currentTasks.addAll(pendentTasks);
			}
			
			if (currentTasks.isEmpty())
				break;
			
			PeriodicTask leastLaxity = null;
			double laxity = Double.MAX_VALUE;
			Set<PeriodicTask> onlyPeriodicTasksFromCurrentTasks = (Set<PeriodicTask>) getTasksUtil().getOnlyPeriodicTasksFromTaskList(currentTasks);
			for (PeriodicTask pTask : onlyPeriodicTasksFromCurrentTasks) {
				double tempLaxity = pTask.getDeadline() - pTask.getRemaining() - position;
				if (tempLaxity < laxity || ((tempLaxity <= laxity) && pTask.equals(previouslyLeastLaxity))){
					laxity = tempLaxity;
					leastLaxity = pTask;
				}
			}
			
			previouslyLeastLaxity = leastLaxity;
			Series<Number, Number> chartTask = getTasksUtil().getChartTask(
					chartTasks, leastLaxity);	
			
			int startedPosition = position;
			int positionAfterSlot = position + slotSize;
			boolean firstTime = true;
			chartTask.getData().add(new Data<Number, Number>(position, 0));
			for (int aux = startedPosition; aux <= positionAfterSlot; aux++){
				if (leastLaxity.getRemaining() > 0){
					chartTask.getData().add(new Data<Number, Number>(aux, 1));
					if (firstTime == false){
						if (leastLaxity.getDeadline() < aux){
							throw new DeadlineNotSatisfiedException(leastLaxity);
						}
						leastLaxity.process(1);
					}
					firstTime = false;
				}else{
					chartTask.getData().add(new Data<Number, Number>(aux-1, 0));
				}
				position = aux;
				if (mapWithActivationTimeAndTasks.containsKey(aux)){
					List<Task> pendentTasks = mapWithActivationTimeAndTasks.get(aux);
					currentTasks.addAll(pendentTasks);
				}
			}
			chartTask.getData().add(new Data<Number, Number>(position, 0));
			
			if (leastLaxity.getRemaining() == 0){
				currentTasks.remove(leastLaxity);
			}
			
		}

		ac.getData().addAll(chartTasks);
		return ac;
	}
}
