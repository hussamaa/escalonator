package br.fucapi.hussamaismail.scheduler.util;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;
import br.eti.hussamaismail.scheduler.domain.Scheduler;
import br.eti.hussamaismail.scheduler.domain.StaticScheduler;
import br.eti.hussamaismail.scheduler.util.TasksUtil;

@RunWith(JUnit4.class)
public class TasksUtilTest {

	private Scheduler scheduler;
	private TasksUtil tasksUtil;
	
	@Before
	public void configure(){
		this.tasksUtil = TasksUtil.getInstance();
		this.scheduler = new RateMonotonicScheduler();
		StaticScheduler staticScheduler = (StaticScheduler) this.scheduler;
		staticScheduler.setTasks(new ArrayList<PeriodicTask>());
		
		PeriodicTask pt1 = new PeriodicTask();
		pt1.setName("pt1");
		pt1.setComputationTime(6.25);
		pt1.setDeadline(50);
		pt1.setPeriod(110);
		staticScheduler.getTasks().add(pt1);
		
		PeriodicTask pt2 = new PeriodicTask();
		pt2.setName("pt2");
		pt2.setComputationTime(3.98);
		pt2.setDeadline(20);
		pt2.setPeriod(130);
		staticScheduler.getTasks().add(pt2);
		
		PeriodicTask pt3 = new PeriodicTask();
		pt3.setName("pt3");
		pt3.setComputationTime(5.01);
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
		StaticScheduler staticScheduler = (StaticScheduler) this.scheduler;
		Assert.assertEquals(20d, staticScheduler.getTasks().get(0).getDeadline());
		Assert.assertEquals(50d, staticScheduler.getTasks().get(staticScheduler.getTasks().size() - 1).getDeadline());
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
		StaticScheduler staticScheduler = (StaticScheduler) this.scheduler;
		Assert.assertEquals(3.98d, staticScheduler.getTasks().get(0).getComputationTime());
		Assert.assertEquals(6.25d, staticScheduler.getTasks().get(staticScheduler.getTasks().size() - 1).getComputationTime());
	}
	
	/**
	 * Teste unitario que verifica o funcionamento da funcao
	 * que obtem o maior deadline dentre as tarefas.
	 * 
	 */
	@Test
	public void testHigherDeadlineFromPeriodicTasks(){
		StaticScheduler staticScheduler = (StaticScheduler) this.scheduler;
		Assert.assertEquals(50d, this.tasksUtil.getHigherDeadlineFromPeriodicTasks(staticScheduler.getTasks()));
		
	}
	
	/**
	 * Teste unitario que verifica o funcionamento da funcao
	 * que obtem o maior deadline dentre as tarefas.
	 * 
	 */
	@Test
	public void testHigherPeriodFromPeriodicTasks(){
		StaticScheduler staticScheduler = (StaticScheduler) this.scheduler;
		Assert.assertEquals(130d, this.tasksUtil.getHigherPeriodFromPeriodicTasks(staticScheduler.getTasks()));
		
	}
}
