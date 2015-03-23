package cn.nephogram.ibeacon.sdk;

import java.util.LinkedList;
import java.util.Queue;

public class ALMovingAverage {
	private static final double LimitedScale = 0.2;

	private int window;
	private Queue<Double> queue;
	private double average;

	public ALMovingAverage(int window) {
		this.window = window;
		queue = new LinkedList<Double>();
		average = 0.0;
	}

	public void push(double value) {
		int size = queue.size();
		assert (size <= window);

		double limitedValue = value;
		if (average != 0) {
			double highLimit = average * (1 + LimitedScale);
			double lowLimit = average * (1 - LimitedScale);
			if (value > highLimit) {
				limitedValue = highLimit;
			}
			if (value < lowLimit) {
				limitedValue = lowLimit;
			}
		}

		double sum = average * size;

		if (size == window) {
			double tmp = queue.poll().doubleValue();
			sum -= tmp;
			size--;
		}

		average = (sum + limitedValue) / (size + 1);
		boolean retval = queue.offer(value);
		assert (retval);
	}

	public double getAverage() {
		return average;
	}

}
