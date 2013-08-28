package br.eti.hussamaismail.scheduler.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.domain.Task;

public class DeadlineNotSatisfiedException extends Exception {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(DeadlineNotSatisfiedException.class);

	public DeadlineNotSatisfiedException(Task task) {
		super();
		log.error("DeadLine not satisfied to task '"+task.getName()+"' - " + task);
	}	
}
