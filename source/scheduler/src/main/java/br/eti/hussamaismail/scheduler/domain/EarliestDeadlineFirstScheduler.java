package br.eti.hussamaismail.scheduler.domain;

import org.apache.log4j.Logger;

import javafx.scene.chart.Chart;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;

/**
 * Classe responsavel por gerar o diagrama
 * temporal para o algoritmo EDF (Earliest Deadline First)
 * 
 * @author Hussama Ismail
 *
 */
public class EarliestDeadlineFirstScheduler extends DynamicScheduler {

	private Logger log = Logger.getLogger(EarliestDeadlineFirstScheduler.class);
	
	/**
	 * Metodo que realiza o teste de escalonabilidade 
	 * para as tarefas periodicas adicionadas ao escalonador.
	 * 
	 * Para o EDF o numero 1 de P2 representa a utilizacao de 100%
	 * do processador.
	 * 
	 * P1 = SUM(C/T) P2 = 1
	 * 
	 * P1 <= P2
	 * 
	 * @return
	 */
	public Boolean isScalable() {

		log.debug("Inicializando teste de escalonabilidade");

		double p1 = 0, p2 = 1;

		for (PeriodicTask pt : this.getTasks()) {
			p1 = p1 + ((double) pt.getComputationTime() / ((double)pt.getPeriod()));
		}

		log.debug("P1: " + p1 + " | P2: " + p2);

		return p1 <= p2 ? true : false;
	}

	
	@Override
	public Chart simulate() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException {

		if (this.isScalable() == false){
			throw new SchedulabilityConditionNotSatisfiedException();
		}
		
		
		return null;
	}

}
