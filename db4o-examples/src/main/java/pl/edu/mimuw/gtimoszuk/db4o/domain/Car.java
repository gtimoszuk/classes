package pl.edu.mimuw.gtimoszuk.db4o.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Car {
	private String model;
	private Pilot pilot;
	private List<SensorReadout> history = new ArrayList<SensorReadout>();

	public Car(String model) {
		this.model = model;
		this.pilot = null;
	}

	public Car(String model, List<SensorReadout> history) {
		this.model = model;
		this.pilot = null;
		this.history = history;
	}

	public Pilot getPilot() {
		return pilot;
	}

	public void setPilot(Pilot pilot) {
		this.pilot = pilot;
	}

	public String getModel() {
		return model;
	}

	public List<SensorReadout> getHistory() {
		return history;
	}

	public void snapshot() {
		history.add(new SensorReadout(poll(), new Date(), this));
	}

	protected double[] poll() {
		int factor = history.size() + 1;
		return new double[] { 0.1d * factor, 0.2d * factor, 0.3d * factor };
	}

	public String toString() {
		return model + "[" + pilot + "]/" + history.size();
	}

}