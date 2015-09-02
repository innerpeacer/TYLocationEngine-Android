package com.ty.locationengine.ble;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

class IPSensorMovingAverageTD {

	class Sample {
		long delta;
		double value;

		Sample(long delta, double value) {
			this.delta = delta;
			this.value = value;
		}
	}

	private long window;
	private Queue<Sample> queue;
	private long lastTimestamp;
	private long timeIntervalContained;
	private double sum;
	private double average;

	private long NANO = (long) Math.pow(10, 9);

	public IPSensorMovingAverageTD(double windowSize) {
		this.window = (long) (windowSize * NANO);
		queue = new LinkedList<Sample>();
		queue.offer(new Sample(0, 0.0));
		lastTimestamp = 0;
		timeIntervalContained = 0;
		average = 0.0;
	}

	public Collection<Sample> getQueue() {
		return queue;
	}

	public void push(long timestamp, double value) {

		if (lastTimestamp == 0) {
			lastTimestamp = timestamp;
			return;
		}

		while (timeIntervalContained > window) {
			Sample tmp = queue.poll();
			sum -= tmp.value * tmp.delta;
			timeIntervalContained -= tmp.delta;
		}

		long newDelta = timestamp - lastTimestamp;
		boolean inserted = queue.offer(new Sample(newDelta, value));

		if (!inserted) {
			throw new RuntimeException("value not inserted");
		}

		sum += value * newDelta;
		timeIntervalContained += newDelta;
		average = sum / timeIntervalContained;

		lastTimestamp = timestamp;

	}

	public void clear() {
		queue.clear();
		queue.offer(new Sample(0, 0.0));
		lastTimestamp = 0;
		sum = 0;
		timeIntervalContained = 0;
		average = 0.0;
	}

	public double getAverage() {
		return average;
	}

	public double getRate() {
		return queue.size() * Math.pow(10, 9) / timeIntervalContained;
	}

}
