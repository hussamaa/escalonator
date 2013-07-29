package br.eti.hussamaismail.scheduler.domain;

public class PeriodicTask extends Task {

	private double computationTime;
	private double period;
	private double deadline;
	private double responseTime;

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

	@Override
	public String toString() {
		return "PeriodicTask [computationTime=" + computationTime + ", period="
				+ period + ", deadline=" + deadline + ", responseTime="
				+ responseTime + "]";
	}

}
