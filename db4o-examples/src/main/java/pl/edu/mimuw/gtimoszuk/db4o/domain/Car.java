package pl.edu.mimuw.gtimoszuk.db4o.domain;

public class Car {
	private String model;
	private Pilot pilot;

	public Car(String model) {
		this.model = model;
		this.pilot = null;
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

	public String toString() {
		return model + "[" + pilot + "]";
	}
}