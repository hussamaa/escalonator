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