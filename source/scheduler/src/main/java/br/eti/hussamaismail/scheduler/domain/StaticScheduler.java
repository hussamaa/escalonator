package br.eti.hussamaismail.scheduler.domain;

import java.util.List;

import org.apache.log4j.Logger;

public class StaticScheduler implements Scheduler {

	private List<PeriodicTask> tasks;
	private Logger log = Logger.getLogger(StaticScheduler.class);

	public List<PeriodicTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<PeriodicTask> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Metodo que realiza o teste de escalonabilidade para as tarefas periodicas
	 * adicionadas ao escalonador.
	 * 
	 * Este metodo por padrao o teste do RateMonotonic que consiste na formula
	 * matematica:
	 * 
	 * P1 = SUM(C/T) P2 = N * (2^(1/N) - 1)
	 * 
	 * P1 <= P2
	 * 
	 * @return
	 */
	public Boolean isScalable() {

		log.debug("Inicializando teste de escalonabilidade");

		double p1 = 0, p2;

		for (PeriodicTask pt : this.getTasks()) {
			p1 = p1 + (pt.getComputationTime() / pt.getPeriod());
		}

		p2 = this.getTasks().size()
				* (Math.pow(2, (double) 1 / this.getTasks().size()) - 1);

		log.debug("P1: " + p1 + " | P2: " + p2);

		return p1 <= p2 ? true : false;
	}
	
	/**
	 * Metodo que calcula o tempo maximo de resposta para 
	 * cada tarefa presente no escalonador estatico
	 * 
	 */
	public void calculateMaximumResponseTimeToTheTasks(){

	
	}
}
