package br.eti.hussamaismail.scheduler.domain;

import javafx.scene.chart.Chart;

public interface Scheduler {
	
	/**
	 * Metodo que sera responsavel por gerar o grafico
	 * conforme a tecnica de escalonamento informada 
	 * pelo usuario
	 * 
	 * @return
	 */
	Chart simulate();

}
