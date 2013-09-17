package br.eti.hussamaismail.scheduler.domain;

import java.util.List;

import br.eti.hussamaismail.scheduler.util.TasksUtil;

public abstract class DynamicScheduler implements Scheduler {

	private List<PeriodicTask> tasks;
	private TasksUtil tasksUtil;

	public DynamicScheduler() {
		this.tasksUtil = TasksUtil.getInstance();
	}

	public List<PeriodicTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<PeriodicTask> tasks) {
		this.tasks = tasks;
	}

	public TasksUtil getTasksUtil() {
		return tasksUtil;
	}

}
