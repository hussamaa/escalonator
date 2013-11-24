package br.eti.hussamaismail.scheduler.domain;

import java.util.List;

import br.eti.hussamaismail.scheduler.util.TasksUtil;

public abstract class DynamicScheduler implements Scheduler {

	private List<Task> tasks;
	private TasksUtil tasksUtil;

	public DynamicScheduler() {
		this.tasksUtil = TasksUtil.getInstance();
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public TasksUtil getTasksUtil() {
		return tasksUtil;
	}

}
