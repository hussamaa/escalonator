package br.fucapi.hussamaismail.scheduler.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import br.eti.hussamaismail.scheduler.domain.LeastLaxityScheduler;
import br.eti.hussamaismail.scheduler.domain.PeriodicTask;
import br.fucapi.hussamaismail.scheduler.util.ChartsUtil;

/**
 * Classe de testes unitarios utilizada
 * para validar o algoritmo implementado
 * para geracao do diagrama temporal de leastLaxity
 * 
 * @author Hussama Ismail
 *
 */
@RunWith(JUnit4.class)
public class LeastLaxitySchedulerTest {

	private LeastLaxityScheduler scheduler;
	private ChartsUtil chartsUtil;

	@Before
	public void init() {
		this.chartsUtil = ChartsUtil.getInstance();
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
		
		this.scheduler.setPartSize(10);
		
		this.scheduler.getTasks().add(t1);
		this.scheduler.getTasks().add(t2);
		this.scheduler.getTasks().add(t3);
		this.scheduler.getTasks().add(t4);
		this.scheduler.getTasks().add(t5);
	}
	
	@SuppressWarnings({"unchecked" })
	@Test
	public void testCase1(){
		
		this.setTasksCase1();
		AreaChart<Number,Number> temporalDiagram = (AreaChart<Number,Number>) this.scheduler.simulate();
		Map<String, List<Integer[]>> map = chartsUtil.getMapWithXIntervals(temporalDiagram);
		
		/* Verifica quantas tarefas foram criadas no mapa */
		Assert.assertEquals(5, map.keySet().size());
		
		
		/* Verifica quantas vezes a tarefa 'A' aparece no grafico */
		Assert.assertEquals(3, map.get("A").size());
		
		/* Verifica os intevalos da tarefa A [0-10] */
		Integer[] intervalA1 = map.get("A").get(0);
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(intervalA1[0]));
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalA1[1]));
		
		/* Verifica os intevalos da tarefa A [10-20] */
		Integer[] intervalA2 = map.get("A").get(1);
		Assert.assertEquals(Long.valueOf(10), Long.valueOf(intervalA2[0]));
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalA2[1]));
		
		/* Verifica os intevalos da tarefa A [30-40] */
		Integer[] intervalA3 = map.get("A").get(2);
		Assert.assertEquals(Long.valueOf(30), Long.valueOf(intervalA3[0]));
		Assert.assertEquals(Long.valueOf(40), Long.valueOf(intervalA3[1]));
		

		/* Verifica quantas vezes a tarefa 'B' aparece no grafico */
		Assert.assertEquals(1, map.get("B").size());
		
		/* Verifica os intevalos da tarefa B [20-30] */
		Integer[] intervalB1 = map.get("B").get(0);
		Assert.assertEquals(Long.valueOf(20), Long.valueOf(intervalB1[0]));
		Assert.assertEquals(Long.valueOf(30), Long.valueOf(intervalB1[1]));
		
		
		/* Verifica quantas vezes a tarefa 'C' aparece no grafico */
		Assert.assertEquals(3, map.get("C").size());
		
		/* Verifica os intevalos da tarefa C [40-50] */
		Integer[] intervalC1 = map.get("C").get(0);
		Assert.assertEquals(Long.valueOf(40), Long.valueOf(intervalC1[0]));
		Assert.assertEquals(Long.valueOf(50), Long.valueOf(intervalC1[1]));
		
		/* Verifica os intevalos da tarefa C [50-60] */
		Integer[] intervalC2 = map.get("C").get(1);
		Assert.assertEquals(Long.valueOf(50), Long.valueOf(intervalC2[0]));
		Assert.assertEquals(Long.valueOf(60), Long.valueOf(intervalC2[1]));
		
		/* Verifica os intevalos da tarefa C [60-70] */
		Integer[] intervalC3 = map.get("C").get(2);
		Assert.assertEquals(Long.valueOf(60), Long.valueOf(intervalC3[0]));
		Assert.assertEquals(Long.valueOf(70), Long.valueOf(intervalC3[1]));
		
		
		/* Verifica quantas vezes a tarefa 'E' aparece no grafico */
		Assert.assertEquals(1, map.get("E").size());
		
		/* Verifica os intevalos da tarefa E [70-80] */
		Integer[] intervalE1 = map.get("E").get(0);
		Assert.assertEquals(Long.valueOf(70), Long.valueOf(intervalE1[0]));
		Assert.assertEquals(Long.valueOf(80), Long.valueOf(intervalE1[1]));
		
		
		/* Verifica quantas vezes a tarefa 'D' aparece no grafico */
		Assert.assertEquals(4, map.get("D").size());
		
		/* Verifica os intevalos da tarefa D [80-90] */
		Integer[] intervalD1 = map.get("D").get(0);
		Assert.assertEquals(Long.valueOf(80), Long.valueOf(intervalD1[0]));
		Assert.assertEquals(Long.valueOf(90), Long.valueOf(intervalD1[1]));
		
		/* Verifica os intevalos da tarefa D [90-100] */
		Integer[] intervalD2 = map.get("D").get(1);
		Assert.assertEquals(Long.valueOf(90), Long.valueOf(intervalD2[0]));
		Assert.assertEquals(Long.valueOf(100), Long.valueOf(intervalD2[1]));
		
		/* Verifica os intevalos da tarefa D [100-110] */
		Integer[] intervalD3 = map.get("D").get(2);
		Assert.assertEquals(Long.valueOf(100), Long.valueOf(intervalD3[0]));
		Assert.assertEquals(Long.valueOf(110), Long.valueOf(intervalD3[1]));
		
		/* Verifica os intevalos da tarefa C [110-120] */
		Integer[] intervalD4 = map.get("D").get(3);
		Assert.assertEquals(Long.valueOf(110), Long.valueOf(intervalD4[0]));
		Assert.assertEquals(Long.valueOf(120), Long.valueOf(intervalD4[1]));
		
	}
	
}
