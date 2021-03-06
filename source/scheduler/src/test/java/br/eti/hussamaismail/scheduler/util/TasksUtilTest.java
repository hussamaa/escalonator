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
package br.eti.hussamaismail.scheduler.util;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.DynamicScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.Scheduler;
import br.eti.hussamaismail.scheduler.domain.Task;

/**
 * Classe referente aos testes unitarios
 * da classe TasksUtil e seus metodos
 * implementados.
 * 
 * @author Hussama Ismail
 *
 */
@RunWith(JUnit4.class)
public class TasksUtilTest {

	private Scheduler scheduler;
	private TasksUtil tasksUtil;
	
	@Before
	public void configure(){
		this.tasksUtil = TasksUtil.getInstance();
		this.scheduler = new RateMonotonicScheduler();
		DynamicScheduler staticScheduler = (DynamicScheduler) this.scheduler;
		staticScheduler.setTasks(new ArrayList<Task>());
		
		PeriodicTask pt1 = new PeriodicTask();
		pt1.setName("pt1");
		pt1.setComputationTime(6);
		pt1.setDeadline(50);
		pt1.setPeriod(110);
		staticScheduler.getTasks().add(pt1);
		
		PeriodicTask pt2 = new PeriodicTask();
		pt2.setName("pt2");
		pt2.setComputationTime(3);
		pt2.setDeadline(20);
		pt2.setPeriod(130);
		staticScheduler.getTasks().add(pt2);
		
		PeriodicTask pt3 = new PeriodicTask();
		pt3.setName("pt3");
		pt3.setComputationTime(5);
		pt3.setDeadline(25);
		pt3.setPeriod(120);
		staticScheduler.getTasks().add(pt3);		

	}
	
	/**
	 * Teste unitario que verifica se ocorreu a ordenacao
	 * da lista de tarefas do escalonador levando em consideracao 
	 * os seus deadline
	 * 
	 */
	@Test
	public void testSortTasksByDeadLine(){
		this.tasksUtil.sortTasksByDeadline(scheduler);
		DynamicScheduler staticScheduler = (DynamicScheduler) this.scheduler;
		Assert.assertEquals(20, ((PeriodicTask) staticScheduler.getTasks().get(0)).getDeadline());
		Assert.assertEquals(50, ((PeriodicTask)staticScheduler.getTasks().get(staticScheduler.getTasks().size() - 1)).getDeadline());
	}
	
	/**
	 * Teste unitario que verifica se ocorreu a ordenacao
	 * da lista de tarefas do escalonador levando em consideracao 
	 * os seus tempos de computacao
	 * 
	 */
	@Test
	public void testSortTasksByComputationTime(){
		this.tasksUtil.sortTasksByDeadline(scheduler);
		DynamicScheduler staticScheduler = (DynamicScheduler) this.scheduler;
		Assert.assertEquals(3, staticScheduler.getTasks().get(0).getComputationTime());
		Assert.assertEquals(6, staticScheduler.getTasks().get(staticScheduler.getTasks().size() - 1).getComputationTime());
	}
	
	/**
	 * Teste unitario que verifica o funcionamento da funcao
	 * que obtem o maior deadline dentre as tarefas.
	 * 
	 */
	@Test
	public void testHigherDeadlineFromPeriodicTasks(){
		DynamicScheduler staticScheduler = (DynamicScheduler) this.scheduler;
		Assert.assertEquals(50, this.tasksUtil.getHigherDeadlineFromPeriodicTasks(staticScheduler.getTasks()));
		
	}
	
	/**
	 * Teste unitario que verifica o funcionamento da funcao
	 * que obtem o maior deadline dentre as tarefas.
	 * 
	 */
	@Test
	public void testHigherPeriodFromPeriodicTasks(){
		DynamicScheduler staticScheduler = (DynamicScheduler) this.scheduler;
		Assert.assertEquals(130, this.tasksUtil.getHigherPeriodFromPeriodicTasks(staticScheduler.getTasks()));
		
	}
}
