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

import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChartsUtilTest {
	
	private AreaChart<Number, Number> chart;
	private ChartsUtil chartUtil = ChartsUtil.getInstance();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before
	public void createChart(){
		
		 NumberAxis xAxis = new NumberAxis(1, 10, 1);
         NumberAxis yAxis = new NumberAxis(0,2,1);
         this.chart = new AreaChart<Number,Number>(xAxis,yAxis);
		
		 XYChart.Series serieA = new XYChart.Series();
         serieA.setName("A");
         serieA.getData().add(new XYChart.Data(0, 0));
         serieA.getData().add(new XYChart.Data(0, 1));
         serieA.getData().add(new XYChart.Data(3, 1));
         serieA.getData().add(new XYChart.Data(5, 1));
         serieA.getData().add(new XYChart.Data(5, 0));
         serieA.getData().add(new XYChart.Data(7, 0));
         serieA.getData().add(new XYChart.Data(7, 1));
         serieA.getData().add(new XYChart.Data(9, 1));
         serieA.getData().add(new XYChart.Data(9, 0));
         
         XYChart.Series serieB = new XYChart.Series();
         serieB.setName("B");
         serieB.getData().add(new XYChart.Data(2, 0));
         serieB.getData().add(new XYChart.Data(2, 1));
         
         serieB.getData().add(new XYChart.Data(4, 1));
         serieB.getData().add(new XYChart.Data(4, 0));
         
         serieB.getData().add(new XYChart.Data(5, 0));
         serieB.getData().add(new XYChart.Data(5, 1));
         serieB.getData().add(new XYChart.Data(6, 1));
         serieB.getData().add(new XYChart.Data(7, 1));
         serieB.getData().add(new XYChart.Data(7, 0));
                     
         chart.getData().addAll(serieA,serieB);
	}

	@Test
	public void testMapSize(){
		Map<String, List<Integer[]>> map = chartUtil.getMapWithXIntervals(this.chart);
		Assert.assertEquals(2, map.keySet().size());
	}
	
	@Test
	public void testSerieAIntervals(){
		Map<String, List<Integer[]>> map = chartUtil.getMapWithXIntervals(this.chart);
		Assert.assertTrue(map.containsKey("A"));
		
		List<Integer[]> ocurrences = map.get("A");
		Assert.assertEquals(2, ocurrences.size());
		
		/* Validando Intervalos: [0-5] */
		Assert.assertEquals(Long.valueOf(0), Long.valueOf(ocurrences.get(0)[0])); 
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(ocurrences.get(0)[1])); 
		
		/* Validando Intervalos: [7-9] */
		Assert.assertEquals(Long.valueOf(7), Long.valueOf(ocurrences.get(1)[0])); 
		Assert.assertEquals(Long.valueOf(9), Long.valueOf(ocurrences.get(1)[1])); 
	}
	
	@Test
	public void testSerieBIntervals(){
		Map<String, List<Integer[]>> map = chartUtil.getMapWithXIntervals(this.chart);
		Assert.assertTrue(map.containsKey("B"));
		
		List<Integer[]> ocurrences = map.get("B");
		Assert.assertEquals(2, ocurrences.size());
		
		/* Validando Intervalos: [2-4] */
		Assert.assertEquals(Long.valueOf(2), Long.valueOf(ocurrences.get(0)[0])); 
		Assert.assertEquals(Long.valueOf(4), Long.valueOf(ocurrences.get(0)[1])); 
		
		/* Validando Intervalos: [7-9] */
		Assert.assertEquals(Long.valueOf(5), Long.valueOf(ocurrences.get(1)[0])); 
		Assert.assertEquals(Long.valueOf(7), Long.valueOf(ocurrences.get(1)[1])); 
	}	
}
