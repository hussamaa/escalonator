package br.fucapi.hussamaismail.scheduler.exception;

import br.eti.hussamaismail.scheduler.domain.Task;

public class TaskNotScalableException extends Exception {

	private static final long serialVersionUID = 1L;

	public TaskNotScalableException(Task task){
		super();
		System.out.println("A tarefa '"+task.getName()+"' não pode ser escalonada");		
	}
	
}
