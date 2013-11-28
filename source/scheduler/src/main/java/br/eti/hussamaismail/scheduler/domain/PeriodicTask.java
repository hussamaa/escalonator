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
along with Foobar. If not, see <http://www.gnu.org/licenses/>.
*/
package br.eti.hussamaismail.scheduler.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeriodicTask extends Task implements Cloneable {
	
	private Logger log = LoggerFactory.getLogger(PeriodicTask.class);

	private int period;
	private int deadline;
	private double responseTime;

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
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

	@Override
	public String toString() {
		return "PeriodicTask [computationTime=" + this.getComputationTime() + ", period="
				+ period + ", deadline=" + deadline + ", responseTime="
				+ responseTime + ", currentProcessed=" + this.getCurrentProcessed()
				+ ", getName()=" + getName() + "]";
	}
	
	public PeriodicTask clone() {
		PeriodicTask pClonedTask = null;
		try{
			pClonedTask = (PeriodicTask) super.clone();
		}catch(CloneNotSupportedException e){
			log.error("Nao foi possivel clonar a PeriodicTask: " + this);
		}		
		return pClonedTask;
	}
}
