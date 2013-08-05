package br.eti.hussamaismail.scheduler.domain;

import org.apache.log4j.Logger;

import br.eti.hussamaismail.scheduler.exception.TaskNotScalableException;

public abstract class MonotonicScheduler extends StaticScheduler {

	private Logger log = Logger.getLogger(MonotonicScheduler.class);
	private static final int MAX_CONVERGENCE_ATTEMPTS = 10;
	
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
	 * Metodo que calcula o tempo maximo de resposta para cada tarefa presente
	 * no escalonador estatico
	 * @throws TaskNotScalableException 
	 * 
	 */
	public void calculateMaximumResponseTimeToTheTasks() throws TaskNotScalableException {

		if ((this.getTasks() == null) && (this.getTasks().isEmpty())){
			log.debug("Não existem tarefas adicionadas ao escalonador");
			return;
		}
		
		for(int i = (this.getTasks().size() - 1); i >= 0 ; i--){
			PeriodicTask actualTask = this.getTasks().get(i);	
			log.debug("Calculando tempo máximo de resposta para tarefa: " + actualTask.getName() + " - " +actualTask);
			double actualValue = actualTask.getComputationTime();
			int attempts = 0;
			while (true){
				double accumulator = 0;
				for (int j = 0; j < i ; j++){
					PeriodicTask tempTask = this.getTasks().get(j);
					accumulator = accumulator + Math.ceil(actualValue / tempTask.getPeriod()) * tempTask.getComputationTime(); 
				}
				double newValue = actualTask.getComputationTime() + accumulator;
				if (newValue != actualValue){
					actualValue = newValue;
				}else{
					break;
				}
				attempts++;
				if (attempts == MAX_CONVERGENCE_ATTEMPTS){
					throw new TaskNotScalableException(actualTask);
				}
			}
			log.debug("O Tempo máximo de resposta da tarefa '" + actualTask.getName() + "' é: " + actualValue);
			actualTask.setResponseTime(actualValue);
		}
	}	
}
