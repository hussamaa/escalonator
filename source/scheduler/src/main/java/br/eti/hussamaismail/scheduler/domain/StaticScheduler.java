package br.eti.hussamaismail.scheduler.domain;

import java.util.List;

public abstract class StaticScheduler implements Scheduler {

	private List<PeriodicTask> tasks;
	
	public List<PeriodicTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<PeriodicTask> tasks) {
		this.tasks = tasks;
	}

}
