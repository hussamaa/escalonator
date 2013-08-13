package br.eti.hussamaismail.scheduler.domain;

import javax.swing.JOptionPane;

import javafx.scene.chart.Chart;

public class LeastLaxityScheduler extends StaticScheduler {

	private long partTime;
	
	public long getPartTime() {
		return partTime;
	}

	public void setPartTime(long partTime) {
		this.partTime = partTime;
	}

	@Override
	public Chart simulate() {
		JOptionPane.showMessageDialog(null, "TODO");
		return null;
	}

}
