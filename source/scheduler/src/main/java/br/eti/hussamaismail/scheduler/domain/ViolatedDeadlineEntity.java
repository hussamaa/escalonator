package br.eti.hussamaismail.scheduler.domain;

import javafx.scene.chart.Chart;

/**
 * Classe que mantem as caracteristicas
 * de uma violacao de deadline.
 * 
 * @author Hussama Ismail
 *
 */
public class ViolatedDeadlineEntity {

	private int position;
	private Task task;
	private Chart generatedChart;

	public Chart getGeneratedChart() {
		return generatedChart;
	}

	public void setGeneratedChart(Chart generatedChart) {
		this.generatedChart = generatedChart;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
}
