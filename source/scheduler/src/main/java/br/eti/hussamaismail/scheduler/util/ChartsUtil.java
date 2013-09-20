package br.eti.hussamaismail.scheduler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * Classe responsavel por conter os metodos
 * de apoio para teste dos graficos de diagrama
 * temporal gerados pela aplicacao.
 * 
 * @author Hussama Ismail
 *
 */
public class ChartsUtil {
	
	private static ChartsUtil instance;
	
	private ChartsUtil(){		
	}
	
	/**
	 * Metodo getInstance referente ao padrao de projeto 
	 * 'Singleton' aplicado a classe ChartsUtil.
	 * 
	 * @return
	 */
	public static ChartsUtil getInstance(){
		if (instance == null){
			instance = new ChartsUtil();
		}		
		return instance;
	}
	
	 /**
	  * Metodo que devolve um mapa contendo as ocorrencias
	  * das tasks no eixo X.
	  * 
	  * Exemplo:
	  * 
	  * A tarefa 'A' foi executada no intervalo [0-5] e [8-9] a tarefa B
	  * no intervalo [5-8]
	  * 
	  * O metodo gera um mapa com a chave String e como valor um ArrayList
	  * contendo vetores de Integer com 2 posicoes, onde a primeira se refere ao inicio
	  * e a segunda o fim.
	  * 
	  * Para o exemplo seria devolvido a seguinte informacao:
	  * 
	  * [A = [{0,5},{8,9}], B = [{5,8}]];
	  * 
	  * @param temporalDiagramChart
	  * @param maxValue - Seta até que posicao do eixo X será montado no mapa
	  * @return
	  */
	 public Map<String,List<Integer[]>> getMapWithXIntervals(AreaChart<Number, Number> temporalDiagramChart, int maxValue){	
		 Map<String,List<Integer[]>> map = new HashMap<String, List<Integer[]>>();
		 
		 /* Percorre todos as tasks que compoem o grafico */
    	for (Series<Number, Number> task : temporalDiagramChart.getData()){

    		/* Verifica se o mapa ja contem uma instancia com o nome da task, caso nao, a cria*/
			if (!map.containsKey(task.getName())) {
				map.put(task.getName(), new ArrayList<Integer[]>());
			}
			
			int start = -1;
			int finish = -1;
			Iterator<Data<Number, Number>> taskOcurrences = task.getData().iterator();
			
			/* Percorre todos os registros por tasks, e encontra os seus intervalos */
			while (taskOcurrences.hasNext()) {
				Data<Number, Number> ocurrence = taskOcurrences.next();
				if ((start == -1) && ocurrence.getYValue().intValue() == 1) {
					start = ocurrence.getXValue().intValue();
					continue;
				}

				if ((start != -1) && ocurrence.getYValue().intValue() != 1) {
					finish = ocurrence.getXValue().intValue();
				}
				
				if ((start != -1) && (finish != -1)) {
					if (((maxValue != -1) && (start < maxValue)) || (maxValue == -1)){
						Integer[] interval = { start, finish };
						map.get(task.getName()).add(interval);
						start = -1;
						finish = -1;
					}
				}
			}
    	}
        return map;
    }
	 
	/**
	 * Chama o metodo de geracao do mapa
	 * passando o parametro -1 para maxValue, ou seja
	 * varre o eixo inteiro.
	 * 
	 * @param temporalDiagramChart
	 * @return
	 */
	public Map<String,List<Integer[]>> getMapWithXIntervals(AreaChart<Number, Number> temporalDiagramChart){	
		return getMapWithXIntervals(temporalDiagramChart, -1);
	}
	
	/**
	 * Metodo que reforma o "gráfico" agrupando
	 * as tarefas sequenciais menores.
	 * 
	 * Ex: T1 tem intervalos [1-2] [2-3], este método
	 * arruma T1 com intervalo [1-3].
	 * 
	 * Este método foi criado para facilitar no desenvolvimento
	 * das técnicas para o gráfico, ficando mais simples discretizar de 1 em 1
	 * em seguida arrumar o gráfico
	 * 
	 * @param temporalDiagramChart
	 */
	public void chartReform(AreaChart<Number, Number> temporalDiagramChart){

		Map<String, List<Integer[]>> mapWithXIntervals = getMapWithXIntervals(temporalDiagramChart);
		List<Series<Number, Number>> adjustedSeries = new ArrayList<Series<Number, Number>>();
		
        Set<String> tasks = mapWithXIntervals.keySet();
        for (String task : tasks) {
			
        	Series<Number, Number> adjustedTaskSerie = new XYChart.Series<Number, Number>();
        	adjustedTaskSerie.setName(task);
        	
        	int inicio = -1;
        	int fim = -1;
        	List<Integer[]> intervals = mapWithXIntervals.get(task);
        	
        	for (Integer[] intervalo : intervals) {
				if (inicio == -1){
					inicio = intervalo[0];
					fim = intervalo[1];
					continue;
				}
				else if (inicio != -1 && fim == intervalo[0]){
					fim = intervalo[1];
					continue;
				}else{
					adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(inicio, 0));
					adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(inicio, 1));
					adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(fim, 1));
					adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(fim, 0));

					inicio = intervalo[0];
					fim = intervalo[1];
				}
        	}
        	            	
        	adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(inicio, 0));
        	adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(inicio, 1));
        	adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(fim, 1));
        	adjustedTaskSerie.getData().add(new XYChart.Data<Number, Number>(fim, 0));
			
        	adjustedSeries.add(adjustedTaskSerie);
		}
		
        temporalDiagramChart.getData().removeAll(temporalDiagramChart.getData());
        temporalDiagramChart.getData().addAll(adjustedSeries);		
	}
}
