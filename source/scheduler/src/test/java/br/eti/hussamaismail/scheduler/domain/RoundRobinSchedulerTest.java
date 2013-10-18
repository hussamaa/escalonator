package br.eti.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.util.ChartsUtil;

/**
 * Classe de testes unitarios utilizada
 * para validar o algoritmo implementado
 * para geracao do diagrama temporal do algoritmo
 * RoundRobin.
 * 
 * @author Hussama Ismail
 *
 */
@RunWith(JUnit4.class)
public class RoundRobinSchedulerTest {

	private RoundRobinScheduler scheduler;
	private ChartsUtil chartsUtil;

	@Before
	public void init() {
		this.chartsUtil = ChartsUtil.getInstance();
		this.scheduler = new RoundRobinScheduler();
		this.scheduler.setTasks(new ArrayList<Task>());
	}
	
	public void setTasksCase1(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(25);
		t1.setPeriod(100);
		t1.setDeadline(100);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(20);
		t2.setPeriod(80);
		t2.setDeadline(80);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(30);
		t3.setPeriod(100);
		t3.setDeadline(100);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("D");
		t4.setComputationTime(20);
		t4.setPeriod(80);
		t4.setDeadline(80);
		
		this.scheduler.setSlotSize(10);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
	}
	
	public void setTasksCase2(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(6);
		t1.setPeriod(0);
		t1.setDeadline(100);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(5);
		t2.setPeriod(0);
		t2.setDeadline(100);
		t2.setActivationTime(0);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(2);
		t3.setPeriod(0);
		t3.setDeadline(100);
		t3.setActivationTime(0);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("D");
		t4.setComputationTime(3);
		t4.setPeriod(0);
		t4.setDeadline(100);
		t4.setActivationTime(0);
		
		PeriodicTask t5 = new PeriodicTask();
		t5.setName("E");
		t5.setComputationTime(7);
		t5.setPeriod(0);
		t5.setDeadline(100);
		t5.setActivationTime(0);
		
		this.scheduler.setSlotSize(2);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
		this.scheduler.getTasks().add(t5);
	}
	
	public void setTasksCase3(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(20);
		t1.setPeriod(0);
		t1.setDeadline(100);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(7);
		t2.setPeriod(0);
		t2.setDeadline(100);
		t2.setActivationTime(0);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(3);
		t3.setPeriod(0);
		t3.setDeadline(100);
		t3.setActivationTime(0);
		
		this.scheduler.setSlotSize(4);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase4(){
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(20);
		t1.setPeriod(0);
		t1.setDeadline(100);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(7);
		t2.setPeriod(0);
		t2.setDeadline(12);
		t2.setActivationTime(0);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(3);
		t3.setPeriod(0);
		t3.setDeadline(100);
		t3.setActivationTime(0);
		
		this.scheduler.setSlotSize(4);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase5(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(3);
		t1.setPeriod(0);
		t1.setDeadline(10);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(4);
		t2.setPeriod(0);
		t2.setDeadline(20);
		t2.setActivationTime(0);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(1);
		t3.setPeriod(0);
		t3.setDeadline(8);
		t3.setActivationTime(0);
		
		this.scheduler.setSlotSize(2);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase1() throws DeadlineNotSatisfiedException{
		
		this.setTasksCase1();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(4, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(3, map.get("A").size());
		
		/* Verifica os intevalos da tarefa A [0-10] */
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalA1[1]));
		
		/* Verifica os intevalos da tarefa A [40-50] */
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(40), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(50), Long.valueOf(intervalA2[1]));
		
		/* Verifica os intevalos da tarefa A [80-85] */
		Integer[] intervalA3 = map.get("A").get(2);
		Assert.assertEquals(Long.valueOf(80), Long.valueOf(intervalA3[0]));
		Assert.assertEquals(Long.valueOf(85), Long.valueOf(intervalA3[1]));
		

		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(2, map.get("B").size());
		
		/* Verifica os intevalos da tarefa B [10-20] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalB1[1]));
		
		/* Verifica os intevalos da tarefa B [50-60] */
		Integer[] intervalB2 = map.get("B").get(1);
		Assert.assertEquals(Long.valueOf(50), Long.valueOf(intervalB2[0]));
		Assert.assertEquals(Long.valueOf(60), Long.valueOf(intervalB2[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(3, map.get("C").size());
		
		/* Verifica os intevalos da tarefa C [20-30] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(30), Long.valueOf(intervalC1[1]));
		
		/* Verifica os intevalos da tarefa C [60-70] */
		Integer[] intervalC2 = map.get("C").get(1);
		Assert.assertEquals(Long.valueOf(60), Long.valueOf(intervalC2[0]));
		Assert.assertEquals(Long.valueOf(70), Long.valueOf(intervalC2[1]));
		
		/* Verifica os intevalos da tarefa C [85-95] */
		Integer[] intervalC3 = map.get("C").get(2);
		Assert.assertEquals(Long.valueOf(85), Long.valueOf(intervalC3[0]));
		Assert.assertEquals(Long.valueOf(95), Long.valueOf(intervalC3[1]));
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase2() throws DeadlineNotSatisfiedException{
		
		this.setTasksCase2();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(5, map.keySet().size());
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(3, map.get("A").size());
		
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalA1[1]));
		
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalA2[1]));
		
		Integer[] intervalA3 = map.get("A").get(2);
		Assert.assertEquals(Long.valueOf(17), Long.valueOf(intervalA3[0]));
		Assert.assertEquals(Long.valueOf(19), Long.valueOf(intervalA3[1]));
		
		
		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(3, map.get("B").size());
		
		/* Verifica os intevalos da tarefa B [2-4] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalB1[1]));
		
		/* Verifica os intevalos da tarefa B [12-14] */
		Integer[] intervalB2 = map.get("B").get(1);
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalB2[0]));
		Assert.assertEquals(Long.valueOf(14), Long.valueOf(intervalB2[1]));
		
		/* Verifica os intevalos da tarefa B [19-20] */
		Integer[] intervalB3 = map.get("B").get(2);
		Assert.assertEquals(Long.valueOf(19), Long.valueOf(intervalB3[0]));
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalB3[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(1, map.get("C").size());
		
		/* Verifica os intevalos da tarefa C [4-6] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalC1[1]));
		
		/* Verifica quantas vezes a tarefa 'D' aparece no grafico */
		Assert.assertEquals(2, map.get("D").size());
		
		/* Verifica os intevalos da tarefa D [6-8] */
		Integer[] intervalD1 = map.get("D").get(0);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalD1[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalD1[1]));
		
		/* Verifica os intevalos da tarefa D [14-15] */
		Integer[] intervalD2 = map.get("D").get(1);
		Assert.assertEquals(Long.valueOf(14), Long.valueOf(intervalD2[0]));
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalD2[1]));
		
		
		/* Verifica quantas vezes a tarefa 'E' aparece no grafico */
		Assert.assertEquals(4, map.get("E").size());
		
		/* Verifica os intevalos da tarefa E [8-10] */
		Integer[] intervalE1 = map.get("E").get(0);
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalE1[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalE1[1]));
		
		/* Verifica os intevalos da tarefa E [15-17] */
		Integer[] intervalE2 = map.get("E").get(1);
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalE2[0]));
		Assert.assertEquals(Long.valueOf(17), Long.valueOf(intervalE2[1]));
		
		/* Verifica os intevalos da tarefa E [20-22] */		
		Integer[] intervalE3 = map.get("E").get(2);
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalE3[0]));
		Assert.assertEquals(Long.valueOf(22), Long.valueOf(intervalE3[1]));
		
		/* Verifica os intevalos da tarefa E [22-23] */		
		Integer[] intervalE4 = map.get("E").get(3);
		Assert.assertEquals(Long.valueOf(22), Long.valueOf(intervalE4[0]));
		Assert.assertEquals(Long.valueOf(23), Long.valueOf(intervalE4[1]));
			
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase3() throws DeadlineNotSatisfiedException{
		
		this.setTasksCase3();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(3, map.keySet().size());
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(5, map.get("A").size());
		
		/* Verifica os intevalos da tarefa A [0-4] */
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalA1[1]));
		
		/* Verifica os intevalos da tarefa A [11-15] */
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalA2[1]));
		
		/* Verifica os intevalos da tarefa A [18-22] */
		Integer[] intervalA3 = map.get("A").get(2);
		Assert.assertEquals(Long.valueOf(18), Long.valueOf(intervalA3[0]));
		Assert.assertEquals(Long.valueOf(22), Long.valueOf(intervalA3[1]));
		
		/* Verifica os intevalos da tarefa A [22-26] */
		Integer[] intervalA4 = map.get("A").get(3);
		Assert.assertEquals(Long.valueOf(22), Long.valueOf(intervalA4[0]));
		Assert.assertEquals(Long.valueOf(26), Long.valueOf(intervalA4[1]));
		
		/* Verifica os intevalos da tarefa A [26-30] */
		Integer[] intervalA5 = map.get("A").get(4);
		Assert.assertEquals(Long.valueOf(26), Long.valueOf(intervalA5[0]));
		Assert.assertEquals(Long.valueOf(30), Long.valueOf(intervalA5[1]));
		
		
		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(2, map.get("B").size());
		
		/* Verifica os intevalos da tarefa B [4-8] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalB1[1]));
		
		/* Verifica os intevalos da tarefa B [15-18] */
		Integer[] intervalB2 = map.get("B").get(1);
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalB2[0]));
		Assert.assertEquals(Long.valueOf(18), Long.valueOf(intervalB2[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(1, map.get("C").size());
		
		/* Verifica os intevalos da tarefa C [9-12] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(11), Long.valueOf(intervalC1[1]));

	}
	
	@Test(expected=DeadlineNotSatisfiedException.class)
	public void testCase4() throws DeadlineNotSatisfiedException{
		this.setTasksCase4();
		this.scheduler.simulate();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCase5() throws DeadlineNotSatisfiedException{
		this.setTasksCase5();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(3, map.keySet().size());
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(2, map.get("A").size());

		/* Verifica os intevalos da tarefa A [0-2] */
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalA1[1]));
		
		/* Verifica os intevalos da tarefa A [5-6] */
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalA2[1]));
		

		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(2, map.get("B").size());

		/* Verifica os intevalos da tarefa B [2-4] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalB1[1]));
		
		/* Verifica os intevalos da tarefa B [6-8] */
		Integer[] intervalB2 = map.get("B").get(1);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalB2[0]));
		Assert.assertEquals(Long.valueOf(8), Long.valueOf(intervalB2[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(1, map.get("C").size());

		/* Verifica os intevalos da tarefa A [4-5] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalC1[1]));
	}
	
}
