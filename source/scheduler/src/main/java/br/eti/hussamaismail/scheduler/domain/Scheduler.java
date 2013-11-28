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

import javafx.scene.chart.Chart;
import br.eti.hussamaismail.scheduler.exception.DeadlineNotSatisfiedException;
import br.eti.hussamaismail.scheduler.exception.SchedulabilityConditionNotSatisfiedException;

public interface Scheduler {
	
	/**
	 * Metodo que sera responsavel por gerar o grafico
	 * conforme a tecnica de escalonamento informada 
	 * pelo usuario
	 * 
	 * @return
	 * @throws DeadlineNotSatisfiedException 
	 * @throws SchedulabilityConditionNotSatisfiedException 
	 */
	Chart simulate() throws DeadlineNotSatisfiedException, SchedulabilityConditionNotSatisfiedException;

}