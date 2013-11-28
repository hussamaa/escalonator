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
package br.eti.hussamaismail.scheduler.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.eti.hussamaismail.scheduler.domain.Task;
import br.eti.hussamaismail.scheduler.domain.ViolatedDeadlineEntity;

public class DeadlineNotSatisfiedException extends Exception {
	
	private ViolatedDeadlineEntity violatedDeadlineEntity; 
	
	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(DeadlineNotSatisfiedException.class);

	public DeadlineNotSatisfiedException(Task task) {
		super("Deadline não satisfeito para a tarefa: '"+task.getName()+"'");
		log.error("Deadline não satisfeito para a tarefa: '"+task.getName()+"'");
	}	
	
	public DeadlineNotSatisfiedException(Task task, Integer position) {
		super("Deadline não satisfeito para a tarefa: '"+task.getName()+"' no instante de tempo: " + position);
		log.error("Deadline não satisfeito para a tarefa: '"+task.getName()+"' no instante de tempo: " + position);
	}
	
	public DeadlineNotSatisfiedException(ViolatedDeadlineEntity violatedDeadlineEntity) {
		super("Deadline não satisfeito para a tarefa: '"+violatedDeadlineEntity.getTask().getName()+"' no instante de tempo: " + violatedDeadlineEntity.getPosition());
		log.error("Deadline não satisfeito para a tarefa: '"+violatedDeadlineEntity.getTask().getName()+"' no instante de tempo: " + violatedDeadlineEntity.getPosition());		
		this.violatedDeadlineEntity = violatedDeadlineEntity;
	}

	public ViolatedDeadlineEntity getViolatedDeadlineEntity() {
		return violatedDeadlineEntity;
	}

}
