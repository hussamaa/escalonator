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
