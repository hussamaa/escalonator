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

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Classe abstrata que representa uma
 * tarefa de um sistema de tempo real.
 * 
 * @author Hussama Ismail
 *
 */
public abstract class Task {

	private String name;
	private int activationTime;
	private int computationTime;
	private int currentProcessed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(int activationTime) {
		this.activationTime = activationTime;
	}

	public int getComputationTime() {
		return computationTime;
	}

	public void setComputationTime(int computationTime) {
		this.computationTime = computationTime;
	}

	public void process(int processTime) {
		this.currentProcessed = new BigDecimal(currentProcessed + processTime)
				.setScale(2, RoundingMode.HALF_UP).intValue();
	}

	public int getRemaining() {
		return this.getComputationTime() - this.currentProcessed;
	}

	public int getCurrentProcessed() {
		return currentProcessed;
	}

	public void setCurrentProcessed(int currentProcessed) {
		this.currentProcessed = currentProcessed;
	}
	
	public void reset() {
		this.setCurrentProcessed(0);
	}
}