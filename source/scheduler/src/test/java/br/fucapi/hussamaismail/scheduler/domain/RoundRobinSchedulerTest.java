package br.fucapi.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.eti.hussamaismail.scheduler.domain.RoundRobinScheduler;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.fucapi.hussamaismail.scheduler.util.ChartsUtil;

/**
 * Classe de testes unitarios utilizada
 * para validar o algoritmo implementado
 * para geracao do diagrama temporal do algoritmo
 * RoundRobin
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
		this.scheduler.setTasks(new ArrayList<PeriodicTask>());
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
		t1.setPeriod(2);
		t1.setDeadline(2);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(5);
		t2.setPeriod(6);
		t2.setDeadline(6);
		t2.setActivationTime(1);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(2);
		t3.setPeriod(3);
		t3.setDeadline(10);
		t3.setActivationTime(3);
		
		PeriodicTask t4 = new PeriodicTask();
		t4.setName("D");
		t4.setComputationTime(3);
		t4.setPeriod(3);
		t4.setDeadline(10);
		t4.setActivationTime(3);
		
		PeriodicTask t5 = new PeriodicTask();
		t5.setName("E");
		t5.setComputationTime(7);
		t5.setPeriod(3);
		t5.setDeadline(10);
		t5.setActivationTime(3);
		
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
		t1.setComputationTime(2);
		t1.setPeriod(0);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(4);
		t2.setPeriod(0);
		t2.setDeadline(9);
		t2.setActivationTime(2);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(5);
		t3.setPeriod(0);
		t3.setDeadline(15);
		t3.setActivationTime(3);
		
		this.scheduler.setSlotSize(3);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase4(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(2);
		t1.setPeriod(0);
		t1.setDeadline(7);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(4);
		t2.setPeriod(0);
		t2.setDeadline(9);
		t2.setActivationTime(2);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(5);
		t3.setPeriod(0);
		t3.setDeadline(13);
		t3.setActivationTime(3);
		
		this.scheduler.setSlotSize(3);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
	}
	
	public void setTasksCase5(){
		
		this.scheduler.getTasks().removeAll(this.scheduler.getTasks());
		
		PeriodicTask t1 = new PeriodicTask();
		t1.setName("A");
		t1.setComputationTime(10);
		t1.setPeriod(0);
		t1.setDeadline(33);
		t1.setActivationTime(0);

		PeriodicTask t2 = new PeriodicTask();
		t2.setName("B");
		t2.setComputationTime(3);
		t2.setPeriod(0);
		t2.setDeadline(28);
		t2.setActivationTime(4);

		PeriodicTask t3 = new PeriodicTask();
		t3.setName("C");
		t3.setComputationTime(10);
		t3.setPeriod(0);
		t3.setDeadline(29);
		t3.setActivationTime(5);
		
		this.scheduler.setSlotSize(5);
		
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
	@Ignore
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
		
//		
//		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
//		Assert.assertEquals(4, map.get("B").size());
//		
//		/* Verifica os intevalos da tarefa B [1-2] */
//		Integer[] intervalB1 = map.get("B").get(0);
//		Assert.assertEquals(Long.valueOf(1), Long.valueOf(intervalB1[0]));
//		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalB1[1]));
//		
//		/* Verifica os intevalos da tarefa B [2-3] */
//		Integer[] intervalB2 = map.get("B").get(1);
//		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalB2[0]));
//		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalB2[1]));
//		
//		/* Verifica os intevalos da tarefa B [3-4] */
//		Integer[] intervalB3 = map.get("B").get(2);
//		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalB3[0]));
//		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalB3[1]));
//		
//		/* Verifica os intevalos da tarefa B [4-5] */
//		Integer[] intervalB4 = map.get("B").get(3);
//		Assert.assertEquals(Long.valueOf(4), Long.valueOf(intervalB4[0]));
//		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalB4[1]));
//		
//		
//		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
//		Assert.assertEquals(2, map.get("C").size());
//		
//		/* Verifica os intevalos da tarefa C [5-6] */
//		Integer[] intervalC1 = map.get("C").get(0);
//		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalC1[0]));
//		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalC1[1]));
//		
//		/* Verifica os intevalos da tarefa C [6-7] */
//		Integer[] intervalC2 = map.get("C").get(1);
//		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalC2[0]));
//		Assert.assertEquals(Long.valueOf(7), Long.valueOf(intervalC2[1]));
		
	}
	
	@Ignore
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase3() throws DeadlineNotSatisfiedException{
		
		this.setTasksCase3();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(3, map.keySet().size());
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(1, map.get("A").size());
		
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(intervalA1[1]));
		
		
		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(2, map.get("B").size());
		
		/* Verifica os intevalos da tarefa B [3-6] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(3), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalB1[1]));
		
		/* Verifica os intevalos da tarefa B [6-7] */
		Integer[] intervalB2 = map.get("B").get(1);
		Assert.assertEquals(Long.valueOf(6), Long.valueOf(intervalB2[0]));
		Assert.assertEquals(Long.valueOf(7), Long.valueOf(intervalB2[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(2, map.get("C").size());
		
		/* Verifica os intevalos da tarefa C [9-12] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(9), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalC1[1]));
		
		/* Verifica os intevalos da tarefa C [12-14] */
		Integer[] intervalC2 = map.get("C").get(1);
		Assert.assertEquals(Long.valueOf(12), Long.valueOf(intervalC2[0]));
		Assert.assertEquals(Long.valueOf(14), Long.valueOf(intervalC2[1]));	
	}
	
	@Ignore
	@Test(expected=DeadlineNotSatisfiedException.class)
	public void testCase4() throws DeadlineNotSatisfiedException{
		this.setTasksCase4();
		this.scheduler.simulate();
	}
	
	@Ignore
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

		/* Verifica os intevalos da tarefa A [0-5] */
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalA1[1]));
		
		/* Verifica os intevalos da tarefa A [20-25] */
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(25), Long.valueOf(intervalA2[1]));
		

		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(1, map.get("B").size());

		/* Verifica os intevalos da tarefa A [0-5] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(18), Long.valueOf(intervalB1[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(2, map.get("C").size());

		/* Verifica os intevalos da tarefa A [0-5] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalC1[1]));
				
		/* Verifica os intevalos da tarefa A [0-5] */
		Integer[] intervalC2 = map.get("C").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalC2[0]));
		Assert.assertEquals(Long.valueOf(15), Long.valueOf(intervalC2[1]));
			
	}
	
}
