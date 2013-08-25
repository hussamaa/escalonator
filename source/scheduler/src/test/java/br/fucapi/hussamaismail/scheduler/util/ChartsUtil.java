package br.fucapi.hussamaismail.scheduler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.scene.chart.AreaChart;
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
	  * @return
	  */
	 public Map<String,List<Integer[]>> getMapWithXIntervals(AreaChart<Number, Number> temporalDiagramChart){	
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
					Integer[] interval = { start, finish };
					map.get(task.getName()).add(interval);
					start = -1;
					finish = -1;
				}
			}
    	}
        return map;
    }
}
