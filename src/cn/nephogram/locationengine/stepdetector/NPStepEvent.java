package cn.nephogram.locationengine.stepdetector;

public class NPStepEvent {

	private final double probability;
	private final double duration;

	public NPStepEvent(double probability, double duration) {
		this.probability = probability;
		this.duration = duration;
	}

	public double getDuration() {
		return duration;
	}

	public double getProbability() {
		return probability;
	}

	public String toString() {
		return "StepEvent(probablity = " + probability + ", duration = "
				+ duration + ")";
	}
}
