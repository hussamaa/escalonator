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
