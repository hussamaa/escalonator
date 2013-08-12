package br.eti.hussamaismail.scheduler.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PeriodicTask extends Task {

	private double computationTime;
	private double period;
	private double deadline;
	private double responseTime;
	private double currentProcessed;
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public double getComputationTime() {
		return computationTime;
	}

	public void setComputationTime(double computationTime) {
		this.computationTime = computationTime;
	}

	public double getPeriod() {
		return period;
	}

	public void setPeriod(double period) {
		this.period = period;
	}

	public double getDeadline() {
		return deadline;
	}

	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}
	
	public void process(double computationTime){
		this.currentProcessed = new BigDecimal(currentProcessed + computationTime).setScale(2, RoundingMode.HALF_UP).doubleValue(); 
	}

	public double getRemaining(){
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
