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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

import org.apache.log4j.Logger;

import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;

/**
 * Classe responsavel por gerar o diagrama
 * temporal para os algoritmos RateMonotonic 
 * e DeadLineMonotonic.
 * 
 * FIXME - Atualmente esses mecanismos funcionam
 * bem para casos com ate um nivel de profundidade,
 * ou seja, para o caso preemptivo: se e encontrado 
 * um conjunto de tarefas, o algorimo para e o processa
 * como prioridade, no entanto, caso durante esse processamento
 * um novo conjunto surgir ocorrem problemas no algoritmo.
 * 
 * Para resolver esse ponto, é necessário implementar uma lógica
 * recursiva.
 * 
 * @author Hussama Ismail
 *
 */
public abstract class MonotonicScheduler extends DynamicScheduler {

	private Logger log = Logger.getLogger(MonotonicScheduler.class);
	private static final int MAX_CONVERGENCE_ATTEMPTS = 10;
	private boolean preemptive;
	
	public boolean isPreemptive() {
		return preemptive;
	}

	public void setPreemptive(boolean preemptive) {
		this.preemptive = preemptive;
	}

	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException {
		
		/* Verifica se é possível escalonar com a técnica desejada */
		if (isScalable() == false){
			log.debug("Estas tarefas não passaram no teste de escalonabilidae com '" + getClass().getName() + "', efetuando calculo do tempo máximo de resposta");
			try {
				calculateMaximumResponseTimeToTheTasks();
			} catch (TaskNotScalableException e) {
				log.debug("Não Foi possivel efetuar o calculo do tempo máximo de resposta. Abortando Processamento");
				return null;
			}
		}

		return null;
	}
	
	/**
	 * Metodo que realiza o teste de escalonabilidade 
	 * para as tarefas periodicas adicionadas ao escalonador.
	 * 
	 * Este metodo utiliza por padrao o teste do RateMonotonic 
	 * que consiste na formula matematica:
	 * 
	 * P1 = SUM(C/T) P2 = N * (2^(1/N) - 1)
	 * 
	 * P1 <= P2
	 * 
	 * @return
	 */
	public Boolean isScalable() {

		log.debug("Inicializando teste de escalonabilidade");

		double p1 = 0, p2;

		Collection<PeriodicTask> onlyPeriodicTasksFromTasks = getTasksUtil().getOnlyPeriodicTasksFromTaskList(this.getTasks());
		for (PeriodicTask pt : onlyPeriodicTasksFromTasks) {
			p1 = p1 + ((double) pt.getComputationTime() / ((double)pt.getPeriod()));
		}

		p2 = this.getTasks().size()
				* (Math.pow(2, (double) 1 / this.getTasks().size()) - 1);

		log.debug("P1: " + p1 + " | P2: " + p2);

		return p1 <= p2 ? true : false;
	}

	/**
	 * Metodo que calcula o tempo maximo de resposta para cada tarefa presente
	 * no escalonador estatico
	 * 
	 * @throws TaskNotScalableException
	 * @throws DeadlineNotSatisfiedException 
	 * 
	 */
	public void calculateMaximumResponseTimeToTheTasks()
			throws TaskNotScalableException, DeadlineNotSatisfiedException {

		if ((this.getTasks() == null) && (this.getTasks().isEmpty())) {
			log.debug("Não existem tarefas adicionadas ao escalonador");
			return;
		}

		List<PeriodicTask> onlyPeriodicTasksFromTasks = (List<PeriodicTask>) getTasksUtil().getOnlyPeriodicTasksFromTaskList(this.getTasks());
		for (int i = (this.getTasks().size() - 1); i >= 0; i--) {
			PeriodicTask actualTask = onlyPeriodicTasksFromTasks.get(i);
			log.debug("Calculando tempo máximo de resposta para tarefa: "
					+ actualTask.getName() + " - " + actualTask);
			double actualValue = actualTask.getComputationTime();
			int attempts = 0;
			while (true) {
				double accumulator = 0;
				for (int j = 0; j < i; j++) {
					PeriodicTask tempTask = onlyPeriodicTasksFromTasks.get(j);
					accumulator =  (accumulator
							+ Math.ceil(actualValue / tempTask.getPeriod())
							* tempTask.getComputationTime());
				}
				double newValue = actualTask.getComputationTime() + accumulator;
				if (newValue != actualValue) {
					actualValue = newValue;
				} else {
					break;
				}
				attempts++;
				if (attempts == MAX_CONVERGENCE_ATTEMPTS) {
					throw new TaskNotScalableException(actualTask);
				}
			}
			
			if (actualValue > actualTask.getDeadline()){
				log.debug("O Tempo máximo de resposta da tarefa '"
						+ actualTask.getName() + "' é: " + actualValue + " e é maior que o deadline " + actualTask.getDeadline());
				throw new DeadlineNotSatisfiedException(actualTask);
			}
			
			log.debug("O Tempo máximo de resposta da tarefa '"
					+ actualTask.getName() + "' é: " + actualValue);
			actualTask.setResponseTime(actualValue);
		}
	}
	
	/**
	 * Metodo que gera os graficos monotonics.
	 * 
	 * @param mapTaskEvent
	 * @param higherValue
	 * @return
	 * @throws DeadlineNotSatisfiedException
	 */
	public AreaChart<Number,Number> generateMonotonicChart(Map<Integer, List<PeriodicTask>> mapTaskEvent, int higherValue) throws DeadlineNotSatisfiedException{
		
		ViolatedDeadlineEntity violatedDeadlineEntity = null;
		
		NumberAxis xAxis = new NumberAxis(0,higherValue,1);
		NumberAxis yAxis = new NumberAxis(0,2,1);
		AreaChart<Number,Number> ac = new AreaChart<Number,Number>(xAxis, yAxis);
		List<Series<Number, Number>> chartTasks = new ArrayList<Series<Number, Number>>();
		
		List<PeriodicTask> pendentTasks = new ArrayList<PeriodicTask>();
		
		int position = 0;

		while (position < higherValue){

			if (mapTaskEvent.containsKey(position)){
				pendentTasks.addAll(mapTaskEvent.get(position));
			}
			
			if (pendentTasks.isEmpty()){
				position = position + 1;
				continue;
			}else{
				if (this instanceof DeadlineMonotonicScheduler){
					getTasksUtil().sortTasksByDeadline(pendentTasks);
				}
			}
			
			boolean initialized = false;
			while (pendentTasks.size() > 0){
			
				PeriodicTask pTask = pendentTasks.get(0);
				
				if ((isPreemptive() == false) && (pTask.getCurrentProcessed() == 0) && (mapTaskEvent.containsKey(position))){
					if (this instanceof DeadlineMonotonicScheduler){
						
						for (PeriodicTask periodicTask : mapTaskEvent.get(position)) {
							if (pendentTasks.contains(periodicTask) == false){
								pendentTasks.add(periodicTask);
							}
						}
				
						getTasksUtil().sortTasksByDeadline(pendentTasks);
						pTask = pendentTasks.get(0);						
					}					
				}
				
				Series<Number, Number> chartTask = getTasksUtil().getChartTask(chartTasks, pTask);	
				chartTask.getData().add(new Data<Number, Number>(position, 0));
				chartTask.getData().add(new Data<Number, Number>(position, 1));
				Vector<List<PeriodicTask>> nonPreemptivePendentTasks = new Vector<List<PeriodicTask>>();
				while (pTask.getRemaining() > 0){
					
					if (position > higherValue){
						break;
					}
					if (position >= pTask.getDeadline()){
						violatedDeadlineEntity = new ViolatedDeadlineEntity();
						violatedDeadlineEntity.setPosition(position);
						violatedDeadlineEntity.setTask(pTask);
						break;
					}
										
					if (initialized == true){
						if (mapTaskEvent.containsKey(position) && (isPreemptive() == true)){
							chartTask.getData().add(new Data<Number, Number>(position, 1));
							chartTask.getData().add(new Data<Number, Number>(position, 0));
							List<PeriodicTask> priorityTasks = mapTaskEvent.get(position);
							
							if (this instanceof DeadlineMonotonicScheduler){
								getTasksUtil().sortTasksByDeadline(priorityTasks);
							}
							
							for (PeriodicTask pTask2 : priorityTasks) {
								
								if (position > higherValue){
									break;
								}
								if (position >= pTask2.getDeadline()){
									violatedDeadlineEntity = new ViolatedDeadlineEntity();
									violatedDeadlineEntity.setPosition(position);
									violatedDeadlineEntity.setTask(pTask2);
									break;
								}
								
								Series<Number, Number> chartTask2 = getTasksUtil().getChartTask(chartTasks, pTask2);	
								chartTask2.getData().add(new Data<Number, Number>(position, 0));
								chartTask2.getData().add(new Data<Number, Number>(position, 1));
								while (pTask2.getRemaining() > 0){
									pTask2.process(1);
									position = position + 1;
								}
								chartTask2.getData().add(new Data<Number, Number>(position, 1));
								chartTask2.getData().add(new Data<Number, Number>(position, 0));
							}
							
							if (violatedDeadlineEntity != null){
								break;
							}
							
							chartTask.getData().add(new Data<Number, Number>(position, 0));
							chartTask.getData().add(new Data<Number, Number>(position, 1));
						}else if (mapTaskEvent.containsKey(position) && (isPreemptive() == false)){
							List<PeriodicTask> list = mapTaskEvent.get(position);
							List<PeriodicTask> validPreemptiveList = new ArrayList<PeriodicTask>();
							for (PeriodicTask periodicTask : list) {
								if (pendentTasks.contains(periodicTask) == false){
									validPreemptiveList.add(periodicTask);
								}
							}
							nonPreemptivePendentTasks.add(validPreemptiveList);							
						}

					}
					initialized = true;
					pTask.process(1);
					position = position + 1;
					
					if (pTask.getRemaining() == 0){
						pendentTasks.remove(pTask);
					}
				}
							
				chartTask.getData().add(new Data<Number, Number>(position, 1));
				chartTask.getData().add(new Data<Number, Number>(position, 0));	
				
				if (violatedDeadlineEntity != null){
					break;
				}
				
				if (nonPreemptivePendentTasks.size() > 0){
					List<PeriodicTask> resultantList = getResultantList(nonPreemptivePendentTasks);
					for (PeriodicTask pTask2 : resultantList) {
					
						if (position > higherValue){
							break;
						}
						if (position >= pTask2.getDeadline()){
							violatedDeadlineEntity = new ViolatedDeadlineEntity();
							violatedDeadlineEntity.setPosition(position);
							violatedDeadlineEntity.setTask(pTask2);
							break;
						}
						
						Series<Number, Number> chartTask2 = getTasksUtil().getChartTask(chartTasks, pTask2);	
						chartTask2.getData().add(new Data<Number, Number>(position, 0));
						chartTask2.getData().add(new Data<Number, Number>(position, 1));
						while (pTask2.getRemaining() > 0){
							pTask2.process(1);
							position = position + 1;
						}
						chartTask2.getData().add(new Data<Number, Number>(position, 1));
						chartTask2.getData().add(new Data<Number, Number>(position, 0));
					}
					
					if (violatedDeadlineEntity != null){
						break;
					}	
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
	
	/**
	 * Metodo que gera uma lista resultante com base
	 * em um vetor com varias listas.
	 * 
	 * @param vector
	 * @return
	 */
	private List<PeriodicTask> getResultantList(Vector<List<PeriodicTask>> vector){
		List<PeriodicTask> resultantList = new ArrayList<PeriodicTask>();
		
		Iterator<List<PeriodicTask>> iterator = vector.iterator();
		while (iterator.hasNext()){
			List<PeriodicTask> next = iterator.next();
			resultantList.addAll(next);
		}
		
		getTasksUtil().sortTasksByDeadline(resultantList);
		
		return resultantList;
	}
}
