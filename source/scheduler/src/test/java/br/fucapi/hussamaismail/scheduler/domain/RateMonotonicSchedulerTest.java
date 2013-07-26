package br.fucapi.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RateMonotonicScheduler;

@RunWith(JUnit4.class)
public class RateMonotonicSchedulerTest {

	private RateMonotonicScheduler scheduler;
	
	@Before
	public void init(){
		this.scheduler = new RateMonotonicScheduler();
	}
	
	public void setScalableTasks(){
		List<PeriodicTask> tasks = new ArrayList<PeriodicTask>();
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("t1");
		t1.setComputationTime(6.25);
		t1.setPeriod(25);
		t1.setDeadline(25);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("t2");
		t2.setComputationTime(6.25);
		t2.setPeriod(50);
		t2.setDeadline(50);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("t3");
		t3.setComputationTime(40);
		t3.setPeriod(100);
		t3.setDeadline(100);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		
		scheduler.setTasks(tasks);				
	}
	
	public void setNotScalableTasks(){
		List<PeriodicTask> tasks = new ArrayList<PeriodicTask>();
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("t1");
		t1.setComputationTime(6.25);
		t1.setPeriod(25);
		t1.setDeadline(25);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("t2");
		t2.setComputationTime(6.25);
		t2.setPeriod(50);
		t2.setDeadline(50);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("t3");
		t3.setComputationTime(40);
		t3.setPeriod(80);
		t3.setDeadline(80);
		
		tasks.add(t1);
		tasks.add(t2);
		tasks.add(t3);
		
		scheduler.setTasks(tasks);				
	}
	
	@Test
	public void scalonableTest(){
		setScalableTasks();
		Assert.assertTrue(scheduler.isScalable());
		
	}
	
	@Test
	public void notScalonableTest(){
		setNotScalableTasks();
		Assert.assertFalse(scheduler.isScalable());
		
	}	
}
