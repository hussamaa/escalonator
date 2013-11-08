package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;
import br.eti.hussamaismail.scheduler.util.ChartsUtil;

@RunWith(JUnit4.class)
public class EarliestDeadlineFirstSchedulerTest {

	private EarliestDeadlineFirstScheduler scheduler;
	private ChartsUtil chartsUtil;
	
	@Before
	public void init(){
		this.scheduler = new EarliestDeadlineFirstScheduler();
		this.scheduler.setTasks(new ArrayList<Task>());
		this.chartsUtil = ChartsUtil.getInstance();
	}
	
	public void setTasksCase1(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase2(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(30);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase3(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(1);
		t1.setPeriod(7);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(9);
		t2.setDeadline(9);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(3);
		t3.setPeriod(11);
		t3.setDeadline(11);
		t3.setActivationTime(0);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("T4");
		t4.setComputationTime(4);
		t4.setPeriod(13);
		t4.setDeadline(13);
		t4.setActivationTime(0);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
	}
	
	public void setTasksCase4(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(5);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCaseBackgroundServer(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(2);
		t1.setPeriod(10);
		t1.setDeadline(10);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(3);
		t2.setPeriod(5);
		t2.setDeadline(5);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("SP");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("SP2");
		s2.setComputationTime(1);
		s2.setActivationTime(1);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(s1);
		this.scheduler.getTasks().add(s2);
	}
	
	public void setTasksCaseBackgroundServer2(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
				
		SporadicTask s1 = new SporadicTask();
		s1.setName("T4");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCaseBackgroundServer3(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(1);
		t1.setPeriod(7);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(9);
		t2.setDeadline(9);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(3);
		t3.setPeriod(11);
		t3.setDeadline(11);
		t3.setActivationTime(0);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("T4");
		t4.setComputationTime(4);
		t4.setPeriod(13);
		t4.setDeadline(13);
		t4.setActivationTime(0);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("S1");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
				
		this.scheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCaseBackgroundServer4(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();		
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("S2");
		s2.setComputationTime(1);
		s2.setActivationTime(10);	
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(s2);
	}
	
	public void setTasksCaseBackgroundServer5(){
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(5);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("T4");
		s2.setComputationTime(1);
		s2.setActivationTime(10);	
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.BACKGROUND_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s2);
	}
		
	public void setTasksCasePollingServer(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(1);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("T3");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("T4");
		s2.setComputationTime(1);
		s2.setActivationTime(11);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(ts);

		this.scheduler.getTasks().add(s1);
		this.scheduler.getTasks().add(s2);
	}
	
	public void setTasksCasePollingServer2(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		SporadicTask s1 = new SporadicTask();
		s1.setName("T4");
		s1.setComputationTime(1);
		s1.setActivationTime(0);	
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("TS");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCasePollingServer3(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		SporadicTask s1 = new SporadicTask();
		s1.setName("T4");
		s1.setComputationTime(1);
		s1.setActivationTime(0);	
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("TS");
		t3.setComputationTime(1);
		t3.setPeriod(5);
		t3.setDeadline(5);
		t3.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCasePollingServer4(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		SporadicTask s1 = new SporadicTask();
		s1.setName("T4");
		s1.setComputationTime(1);
		s1.setActivationTime(0);	
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("T5");
		s2.setComputationTime(1);
		s2.setActivationTime(1);	
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
	
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		t2.setActivationTime(0);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("TS");
		t3.setComputationTime(2);
		t3.setPeriod(5);
		t3.setDeadline(5);
		t3.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s1);
		this.scheduler.getTasks().add(s2);
	}
	
	public void setTasksCasePollingServer5(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
	
		SporadicTask s1 = new SporadicTask();
		s1.setName("T2");
		s1.setComputationTime(1);
		s1.setActivationTime(0);	
		

		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		t1.setActivationTime(0);
			
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("TS");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.POLLING_SERVER);
	
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCaseSporadicServer(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(5);
		t2.setDeadline(4);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(2);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("T3");
		s1.setComputationTime(1);
		s1.setActivationTime(3);
		
		SporadicTask s2 = new SporadicTask();
		s2.setName("T4");
		s2.setComputationTime(1);
		s2.setActivationTime(6);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(ts);

		this.scheduler.getTasks().add(s1);
		this.scheduler.getTasks().add(s2);
	}
	
	public void setTasksCaseSporadicServer2(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(8);
		t2.setDeadline(7);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(2);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("S1");
		s1.setComputationTime(1);
		s1.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(ts);
		
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCaseSporadicServer3(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(8);
		t2.setDeadline(7);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(1);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("S1");
		s1.setComputationTime(2);
		s1.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(ts);
		
		this.scheduler.getTasks().add(s1);
	}

	public void setTasksCaseSporadicServer4(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(8);
		t2.setDeadline(7);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(2);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("S1");
		s1.setComputationTime(3);
		s1.setActivationTime(0);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(ts);
		
		this.scheduler.getTasks().add(s1);
	}
	
	public void setTasksCaseSporadicServer5(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("T1");
		t1.setComputationTime(3);
		t1.setPeriod(20);
		t1.setDeadline(7);
		
		PeriodicTask t2 = new PeriodicTask();
		t2.setName("T2");
		t2.setComputationTime(2);
		t2.setPeriod(8);
		t2.setDeadline(7);
		
		PeriodicTask t3 = new PeriodicTask();
		t3.setName("T3");
		t3.setComputationTime(1);
		t3.setPeriod(10);
		t3.setDeadline(8);
		
		PeriodicTask ts = new PeriodicTask();
		ts.setName("TS");
		ts.setComputationTime(2);
		ts.setPeriod(10);
		ts.setDeadline(8);
		
		SporadicTask s1 = new SporadicTask();
		s1.setName("S1");
		s1.setComputationTime(1);
		s1.setActivationTime(4);
		
		this.scheduler.setSporadicPolicy(SporadicPolicy.SPORADIC_SERVER);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(ts);
		
		this.scheduler.getTasks().add(s1);
	}
		
	@Test
	public void testCase1(){
		this.setTasksCase1();
		Assert.assertTrue(this.scheduler.isScalable());
	} 
	
	@Test
	public void testCase2(){
		this.setTasksCase2();
		Assert.assertFalse(this.scheduler.isScalable());
	} 
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase3() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCase1();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(3, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'T1' aparece no grafico */
		Assert.assertEquals(1, map.get("T1").size());
		
		/* Verifica os intevalos da tarefa T1 [2-5] */
		Integer[] intervalT11 = map.get("T1").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT11[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT11[1]));
		

		/* Verifica quantas vezes a tarefa 'T2' aparece no grafico */
		Assert.assertEquals(4, map.get("T2").size());
		
		/* Verifica os intevalos da tarefa T2 [0-2] */
		Integer[] intervalT21 = map.get("T2").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT21[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT21[1]));
		
		/* Verifica os intevalos da tarefa T2 [6-8] */
		Integer[] intervalT22 = map.get("T2").get(1);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT22[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalT22[1]));
		
		/* Verifica os intevalos da tarefa T2 [10-12] */
		Integer[] intervalT23 = map.get("T2").get(2);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT23[0]));
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalT23[1]));
		
		/* Verifica os intevalos da tarefa T2 [15-17] */
		Integer[] intervalT24 = map.get("T2").get(3);
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalT24[0]));
		Assert.assertEquals(Long.valueOf(17), Long.valueOf(intervalT24[1]));
		
		
		/* Verifica quantas vezes a tarefa 'T3' aparece no grafico */
		Assert.assertEquals(2, map.get("T3").size());
		
		/* Verifica os intevalos da tarefa T3 [5-6] */
		Integer[] intervalT31 = map.get("T3").get(0);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT31[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT31[1]));
		
		/* Verifica os intevalos da tarefa T3 [12-13] */
		Integer[] intervalT32 = map.get("T3").get(1);
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalT32[0]));
		Assert.assertEquals(Long.valueOf(13), Long.valueOf(intervalT32[1]));
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase4() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCase3();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(4, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'T1' aparece no grafico */
		Assert.assertEquals(2, map.get("T1").size());
		
		/* Verifica os intevalos da tarefa T1 [0-1] */
		Integer[] intervalT11 = map.get("T1").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT11[0]));
		Assert.assertEquals(Long.valueOf(1), Long.valueOf(intervalT11[1]));
		
		/* Verifica os intevalos da tarefa T1 [10-11] */
		Integer[] intervalT12 = map.get("T1").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT12[0]));
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalT12[1]));
		
		
		/* Verifica quantas vezes a tarefa 'T2' aparece no grafico */
		Assert.assertEquals(2, map.get("T2").size());
		
		/* Verifica os intevalos da tarefa T2 [1-3] */
		Integer[] intervalT21 = map.get("T2").get(0);
		Assert.assertEquals(Long.valueOf(1), Long.valueOf(intervalT21[0]));
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT21[1]));
		
		/* Verifica os intevalos da tarefa T2 [11-13] */
		Integer[] intervalT22 = map.get("T2").get(1);
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalT22[0]));
		Assert.assertEquals(Long.valueOf(13), Long.valueOf(intervalT22[1]));
		
		/* Verifica quantas vezes a tarefa 'T3' aparece no grafico */
		Assert.assertEquals(1, map.get("T3").size());
		
		/* Verifica os intevalos da tarefa T3 [3-6] */
		Integer[] intervalT31 = map.get("T3").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT31[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT31[1]));
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [6-10] */
		Integer[] intervalT41 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT41[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT41[1]));
	
	}
	
	@Test(expected=DeadlineNotSatisfiedException.class)
	public void testCase5() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCase4();
		this.scheduler.simulate();
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseBackgroundServer() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseBackgroundServer();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(4, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'T1' aparece no grafico */
		Assert.assertEquals(1, map.get("T1").size());
		
		/* Verifica os intevalos da tarefa T1 [3-5] */
		Integer[] intervalT11 = map.get("T1").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT11[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT11[1]));
		
		/* Verifica quantas vezes a tarefa 'T2' aparece no grafico */
		Assert.assertEquals(2, map.get("T2").size());
		
		/* Verifica os intevalos da tarefa T2 [0-3] */
		Integer[] intervalT21 = map.get("T2").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT21[0]));
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT21[1]));
		
		/* Verifica os intevalos da tarefa T2 [5-8] */
		Integer[] intervalT22 = map.get("T2").get(1);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT22[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalT22[1]));
		
		/* Verifica quantas vezes a tarefa 'SP' aparece no grafico */
		Assert.assertEquals(1, map.get("SP").size());
		
		/* Verifica os intevalos da tarefa T3 [9-10] */
		Integer[] intervalT31 = map.get("SP").get(0);
		Assert.assertEquals(Long.valueOf(9), Long.valueOf(intervalT31[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT31[1]));
		
		/* Verifica quantas vezes a tarefa 'SP2' aparece no grafico */
		Assert.assertEquals(1, map.get("SP2").size());
		
		/* Verifica os intevalos da tarefa SP2 [8-9] */
		Integer[] intervalT41 = map.get("SP2").get(0);
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalT41[0]));
		Assert.assertEquals(Long.valueOf(9), Long.valueOf(intervalT41[1]));	
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseBackgroundServer2() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseBackgroundServer2();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [8-9] */
		Integer[] intervalT3 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(9), Long.valueOf(intervalT3[1]));		
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseBackgroundServer3() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseBackgroundServer3();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S1' aparece no grafico */
		Assert.assertNull(map.get("S1"));	
	}

	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseBackgroundServer4() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseBackgroundServer4();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S2' aparece no grafico */
		Assert.assertEquals(1, map.get("S2").size());
		
		/* Verifica os intevalos da tarefa S2 [12-13] */
		Integer[] intervalT3 = map.get("S2").get(0);
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(13), Long.valueOf(intervalT3[1]));		
	}
	
	@Test(expected=DeadlineNotSatisfiedException.class)
	public void testCaseBackgroundServer5() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseBackgroundServer5();
		this.scheduler.simulate();
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCasePollingServer() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCasePollingServer();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(4, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'T1' aparece no grafico */
		Assert.assertEquals(1, map.get("T1").size());
		
		/* Verifica os intevalos da tarefa T1 [2-5] */
		Integer[] intervalT11 = map.get("T1").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT11[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT11[1]));
		
		/* Verifica quantas vezes a tarefa 'T2' aparece no grafico */
		Assert.assertEquals(4, map.get("T2").size());
		
		/* Verifica os intevalos da tarefa T2 [0-2] */
		Integer[] intervalT21 = map.get("T2").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT21[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT21[1]));
		
		/* Verifica os intevalos da tarefa T2 [6-8] */
		Integer[] intervalT22 = map.get("T2").get(1);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT22[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalT22[1]));
		
		/* Verifica os intevalos da tarefa T2 [10-12] */
		Integer[] intervalT32 = map.get("T2").get(2);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT32[0]));
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalT32[1]));

		/* Verifica os intevalos da tarefa T2 [15-17] */
		Integer[] intervalT42 = map.get("T2").get(3);
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalT42[0]));
		Assert.assertEquals(Long.valueOf(17), Long.valueOf(intervalT42[1]));
		
		/* Verifica quantas vezes a tarefa 'T3' aparece no grafico */
		Assert.assertEquals(1, map.get("T3").size());
		
		/* Verifica os intevalos da tarefa T3 [5-6] */
		Integer[] intervalT31 = map.get("T3").get(0);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT31[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT31[1]));
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [12-13] */
		Integer[] intervalT41 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalT41[0]));
		Assert.assertEquals(Long.valueOf(13), Long.valueOf(intervalT41[1]));
	
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCasePollingServer2() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCasePollingServer2();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [5-6] */
		Integer[] intervalT3 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT3[1]));
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCasePollingServer3() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCasePollingServer3();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [2-3] */
		Integer[] intervalT3 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT3[1]));
	}
	

	@SuppressWarnings({"unchecked" })
	@Test
	public void testCasePollingServer4() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCasePollingServer4();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [2-3] */
		Integer[] intervalT3 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT3[1]));
		
		/* Verifica quantas vezes a tarefa 'T5' aparece no grafico */
		Assert.assertEquals(1, map.get("T5").size());
		
		/* Verifica os intevalos da tarefa T5 [3-4] */
		Integer[] intervalT4 = map.get("T5").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT4[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalT4[1]));
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCasePollingServer5() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCasePollingServer5();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T2' aparece no grafico */
		Assert.assertEquals(1, map.get("T2").size());
		
		/* Verifica os intevalos da tarefa T4 [3-4] */
		Integer[] intervalT3 = map.get("T2").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalT3[1]));
		
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseSporadicServer() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseSporadicServer();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'T3' aparece no grafico */
		Assert.assertEquals(1, map.get("T3").size());
		
		/* Verifica os intevalos da tarefa T3 [3-4] */
		Integer[] intervalT3 = map.get("T3").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalT3[1]));
		
		/* Verifica quantas vezes a tarefa 'T4' aparece no grafico */
		Assert.assertEquals(1, map.get("T4").size());
		
		/* Verifica os intevalos da tarefa T4 [6-7] */
		Integer[] intervalT4 = map.get("T4").get(0);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalT4[0]));
		Assert.assertEquals(Long.valueOf(7), Long.valueOf(intervalT4[1]));				
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseSporadicServer2() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseSporadicServer2();
		
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S1' aparece no grafico */
		Assert.assertEquals(1, map.get("S1").size());
		
		/* Verifica os intevalos da tarefa S1 [0-1] */
		Integer[] intervalT3 = map.get("S1").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(1), Long.valueOf(intervalT3[1]));				
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseSporadicServer3() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		
		this.setTasksCaseSporadicServer3();
		
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S1' aparece no grafico */
		Assert.assertEquals(2, map.get("S1").size());
		
		/* Verifica os intevalos da tarefa S1 [0-1] */
		Integer[] intervalT3 = map.get("S1").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(1), Long.valueOf(intervalT3[1]));	
		
		/* Verifica os intevalos da tarefa S1 [10-11] */
		Integer[] intervalT32 = map.get("S1").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT32[0]));
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalT32[1]));	
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseSporadicServer4() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		this.setTasksCaseSporadicServer4();
		
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S1' aparece no grafico */
		Assert.assertEquals(2, map.get("S1").size());
		
		/* Verifica os intevalos da tarefa S1 [0-1] */
		Integer[] intervalT3 = map.get("S1").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalT3[1]));	
		
		/* Verifica os intevalos da tarefa S1 [10-11] */
		Integer[] intervalT32 = map.get("S1").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalT32[0]));
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalT32[1]));	
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCaseSporadicServer5() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException{
		this.setTasksCaseSporadicServer5();
		
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas vezes a tarefa 'S1' aparece no grafico */
		Assert.assertEquals(1, map.get("S1").size());
		
		/* Verifica os intevalos da tarefa S1 [4-5] */
		Integer[] intervalT3 = map.get("S1").get(0);
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalT3[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalT3[1]));		
	}
}