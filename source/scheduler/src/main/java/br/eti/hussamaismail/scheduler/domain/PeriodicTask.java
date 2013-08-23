package br.eti.hussamaismail.scheduler.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PeriodicTask extends Task {

	private int computationTime;
	private int period;
	private int deadline;
	private int responseTime;
	private int currentProcessed;
	private int activationTime;
	
	public int getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(int activationTime) {
		this.activationTime = activationTime;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public int getComputationTime() {
		return computationTime;
	}

	public void setComputationTime(int computationTime) {
		this.computationTime = computationTime;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}
	
	public void process(int processTime){
		this.currentProcessed = new BigDecimal(currentProcessed + processTime).setScale(2, RoundingMode.HALF_UP).intValue(); 
	}

	public int getRemaining(){
		return this.computationTime - this.currentProcessed;
	}
	
	public void reset(){
		this.currentProcessed = 0;
	}
	
	@Override
	public String toString() {
		return "PeriodicTask [computationTime=" + computationTime + ", period="
				+ period + ", deadline=" + deadline + ", responseTime="
				+ responseTime + ", currentProcessed=" + currentProcessed
				+ ", getName()=" + getName() + "]";
	}

}
