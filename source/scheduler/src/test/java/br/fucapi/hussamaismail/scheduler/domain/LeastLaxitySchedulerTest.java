package br.fucapi.hussamaismail.scheduler.domain;

import java.util.ArrayList;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.Chart;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.LeastLaxityScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;

@RunWith(JUnit4.class)
public class LeastLaxitySchedulerTest {

	private LeastLaxityScheduler scheduler;

	@Before
	public void init() {
		this.scheduler = new LeastLaxityScheduler();
		this.scheduler.setTasks(new ArrayList<PeriodicTask>());
	}
	
	public void setTasksCase1(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(30);
		t1.setPeriod(0);
		t1.setDeadline(40);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(10);
		t2.setPeriod(0);
		t2.setDeadline(30);
		t2.setActivationTime(0);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(30);
		t3.setPeriod(0);
		t3.setDeadline(100);
		t3.setActivationTime(30);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("D");
		t4.setComputationTime(40);
		t4.setPeriod(0);
		t4.setDeadline(200);
		t4.setActivationTime(50);
		
		PeriodicTask t5 = new PeriodicTask();
		t5.setName("E");
		t5.setComputationTime(10);
		t5.setPeriod(0);
		t5.setDeadline(90);
		t5.setActivationTime(70);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
		this.scheduler.getTasks().add(t5);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	@Test
	public void testCase1(){
		
		this.setTasksCase1();
		Chart simulate = this.scheduler.simulate();
		
		AreaChart<Number, Number> chart = (AreaChart<Number, Number>) simulate;
//		System.out.println(chart.getXAxis());
		
//		System.out.println(simulate);
		
	}
	
}
