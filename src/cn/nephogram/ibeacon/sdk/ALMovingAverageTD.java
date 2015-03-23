package cn.nephogram.ibeacon.sdk;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class ALMovingAverageTD {
	// private static final String TAG = "ALMovingAverageTD";
	private static final double LimitedScale = 0.2;

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

	// private long NANO = (long) Math.pow(10, 9);
	private long THOUSAND = (long) Math.pow(10, 3);

	public ALMovingAverageTD(double windowSize) {
		// this.window = (long) (windowSize * NANO);
		this.window = (long) (windowSize * THOUSAND);

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
		// Log.i(TAG, "Push " + timestamp);
		if (lastTimestamp == 0) {
			lastTimestamp = timestamp;
			// Log.i(TAG, "Return " + lastTimestamp);
			return;
		}

		while (timeIntervalContained > window) {
			Sample tmp = queue.poll();
			sum -= tmp.value * tmp.delta;
			timeIntervalContained -= tmp.delta;

			// Log.i(TAG, "POP");
		}

		long newDelta = timestamp - lastTimestamp;

		double limitedValue = value;
		if (timeIntervalContained > 0) {
			double currentAverage = sum / timeIntervalContained;
			if (currentAverage != 0) {
				double highLimit = currentAverage * (1 - LimitedScale);
				double lowLimit = currentAverage * (1 + LimitedScale);
				if (value > highLimit) {
					limitedValue = highLimit;
				}
				if (value < lowLimit) {
					limitedValue = lowLimit;
				}
			}
		}
		// boolean inserted = queue.offer(new Sample(newDelta, value));
		boolean inserted = queue.offer(new Sample(newDelta, limitedValue));

		if (!inserted) {
			throw new RuntimeException("value not inserted");
		}

		sum += value * newDelta;
		timeIntervalContained += newDelta;
		average = sum / timeIntervalContained;

		lastTimestamp = timestamp;

	}

	public double getAverage() {
		return average;
	}

	public double getRate() {
		// return queue.size() * Math.pow(10, 9) / timeIntervalContained;
		return queue.size() * THOUSAND / timeIntervalContained;

	}

}
