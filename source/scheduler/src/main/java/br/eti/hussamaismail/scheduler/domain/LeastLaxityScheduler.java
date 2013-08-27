package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.HashMap;
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
import br.eti.hussamaismail.scheduler.util.TasksUtil;

public class LeastLaxityScheduler extends StaticScheduler {

	private TasksUtil tasksUtil;
	
	private Integer slotSize;
	
	public void setSlotSize(Integer slotSize) {
		this.slotSize = slotSize;
	}

	public LeastLaxityScheduler() {
		super();
		this.tasksUtil = TasksUtil.getInstance();
	}
	
	@Override
	public Chart simulate() {
		
		Map<Integer, List<PeriodicTask>> mapWithActivationTimeAndTasks = getMapWithActivationTimeAndTasks();
		int position = 0;
		int higherValue = tasksUtil.getHigherDeadlineFromPeriodicTasks(getTasks());
		
		NumberAxis xAxis = new NumberAxis(0, higherValue, 1);
		NumberAxis yAxis = new NumberAxis(0, 2, 1);
		AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis,yAxis);
		
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		Set<PeriodicTask> currentTasks = new LinkedHashSet<PeriodicTask>();
		
		int slotSize = 1;
		if (this.slotSize != null){
			slotSize = this.slotSize;
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
			
			
			/* Verifica se o que falta para executar da tarefa e menor que o slot */
//			int i = 1;
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
					List<PeriodicTask> pendentTasks = mapWithActivationTimeAndTasks.get(aux);
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
	
	/**
	 * Metodo que gera um mapeamento de todas as periodic tasks
	 * e seus tempos de ativacao no grafico.
	 * 
	 * @return
	 */
	private Map<Integer, List<PeriodicTask>> getMapWithActivationTimeAndTasks(){
		
		Map<Integer, List<PeriodicTask>> mapActivationTime = new HashMap<Integer, List<PeriodicTask>>();
		List<PeriodicTask> tasks = getTasks();
		
		for (PeriodicTask periodicTask : tasks) {
			int activationTime = periodicTask.getActivationTime();
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
