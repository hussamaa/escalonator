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
		this.scheduler.setTasks(new ArrayList<PeriodicTask>());
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
}