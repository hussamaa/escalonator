package br.eti.hussamaismail.scheduler.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class LeastLaxityScheduler extends StaticScheduler {

	private TasksUtil tasksUtil;
	
	private Integer partSize;
	
	public void setPartSize(Integer partSize) {
		this.partSize = partSize;
	}

	public LeastLaxityScheduler() {
		super();
		this.tasksUtil = TasksUtil.getInstance();
	}
	
	@Override
	public Chart simulate() {
		
		Map<Double, List<PeriodicTask>> mapWithActivationTimeAndTasks = getMapWithActivationTimeAndTasks();
		double position = 0;
		double higherValue = tasksUtil.getHigherDeadlineFromPeriodicTasks(getTasks());
		
		NumberAxis xAxis = new NumberAxis(1, higherValue, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,yAxis);
		
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		List<PeriodicTask> currentTasks = new ArrayList<PeriodicTask>();
		
		int partSize = 1;
		if (this.partSize != null){
			 partSize = this.partSize;
		}
		
		while (position <= higherValue){
			if (mapWithActivationTimeAndTasks.containsKey(position)){
				List<PeriodicTask> pendentTasks = mapWithActivationTimeAndTasks.get(position);
				currentTasks.addAll(pendentTasks);
			}
			
			if (currentTasks.isEmpty())
				break;
			
			/* Procura o menor laxity dentre as tarefas da rodada */
			PeriodicTask leastLaxity = null;
			double laxity = Double.MAX_VALUE;
			for (PeriodicTask pTask : currentTasks) {
				double tempLaxity = pTask.getDeadline() - pTask.getRemaining() - position;
				if (tempLaxity < laxity){
					laxity = tempLaxity;
					leastLaxity = pTask;
				}
			}
			Series<Number, Number> chartTask = this.tasksUtil.getChartTask(
					chartTasks, leastLaxity);	
			
			chartTask.getData().add(new Data<Number, Number>(position, 0));
			chartTask.getData().add(new Data<Number, Number>(position, 1));
			
			if (leastLaxity.getRemaining() >= partSize){
				leastLaxity.process(partSize);
				position = (new BigDecimal(position + partSize)).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
			}else if (leastLaxity.getRemaining() < partSize){
				position = (new BigDecimal(position + leastLaxity.getRemaining())).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
				leastLaxity.process(leastLaxity.getRemaining());
			}
			
			chartTask.getData().add(new Data<Number, Number>(position, 1));
			chartTask.getData().add(new Data<Number, Number>(position, 0));
			
			if (leastLaxity.getRemaining() == 0){
				currentTasks.remove(leastLaxity);
			}
			
		}

		ac.getData().addAll(chartTasks);
		return ac;
	}
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus tempos de ativacao no grafico.
	 * 
	 * @return
	 */
	private Map<Double, List<PeriodicTask>> getMapWithActivationTimeAndTasks(){
		
		Map<Double, List<PeriodicTask>> mapActivationTime = new HashMap<Double,List<PeriodicTask>>();
		List<PeriodicTask> tasks = getTasks();
		
		for (PeriodicTask periodicTask : tasks) {
			double activationTime = periodicTask.getActivationTime();
			if (mapActivationTime.containsKey(activationTime) == false){
				List<PeriodicTask> periodTaskList = new ArrayList<PeriodicTask>();
				periodTaskList.add(periodicTask);
				mapActivationTime.put(activationTime, periodTaskList);
			}else{
				mapActivationTime.get(activationTime).add(periodicTask);
			}
		}
		
		return mapActivationTime;
	}
}
