package com.ty.locationengine.ble;

class IPStepEvent {

	private final double probability;
	private final double duration;

	public IPStepEvent(double probability, double duration) {
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
