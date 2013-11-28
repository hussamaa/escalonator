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

import java.util.List;
import java.util.Map;

import javafx.scene.chart.Chart;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;

/**
 * Classe responsavel por implementar o comportamento
 * do escalonamento utilizando a técnica RateMonotonic. 
 * 
 * Essa classe utiliza os comportamentos herdados da classe
 * 'MonotonicScheduler' aplicando a particularidade das tarefas
 * com menor periodo terem prioridade.
 * 
 * @author Hussama Ismail
 *
 */
public class RateMonotonicScheduler extends MonotonicScheduler {
	
	/**
	 * Modifica o comportamento ordendando as classes
	 * por periodo de forma crescente
	 * @throws DeadlineNotSatisfiedException 
	 * @throws TaskNotScalableException 
	 */
	@Override
	public void calculateMaximumResponseTimeToTheTasks()
			throws DeadlineNotSatisfiedException, TaskNotScalableException {	
		this.getTasksUtil().sortTasksByPeriod(this);
		super.calculateMaximumResponseTimeToTheTasks();
	}
	
	/**
	 * Gera o diagrama temporal utilizando o maior
	 * periodo como referencia para o grafico e também 
	 * menor período com prioridade.
	 * @throws TaskNotScalableException 
	 */
	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException {
		this.getTasksUtil().sortTasksByPeriod(this);
		super.simulate();
		int higherPeriod = getTasksUtil().getHigherPeriodFromPeriodicTasks(getTasks());
		Map<Integer, List<PeriodicTask>> mapWithPeriodsAndTasks = getTasksUtil().getMapWithPeriodsAndTasks(getTasks());			
		
		return generateMonotonicChart(mapWithPeriodsAndTasks, higherPeriod);		
	}
	
}
