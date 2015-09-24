package com.ty.locationengine.ble;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

class IPMovingAverageStepDetector extends IPStepDector {
	// private static final String TAG = "MovingAverageStepDetector";

	private static final long NANO = (long) Math.pow(10, 9);
	// private static final long POWER_WINDOW = NANO / 10;
	private static final double MAX_STRIDE_DURATION = 2.0;

	public static final double MA1_WINDOW = 0.2;
	public static final double MA2_WINDOW = 5 * MA1_WINDOW;
	public static final float POWER_CUTOFF_VALUE = 1000.0f;

	private float[] MAValues;
	private IPSensorMovingAverageTD[] MAFilters;
	private IPSensorCumulativeSignalPowerTD CSP_Filter;

	private boolean mMASwapState;
	private boolean mStepDetected;
	private boolean mSignalPowerCutoff;

	private long mLastStepTimestamp;
	private double mStrideDuration;

	private double mWindowMA1;
	private double mWindowMA2;
	// private long mWindowPower;
	private float mPowerCutoff;

	private Sensor mSensor;

	public IPMovingAverageStepDetector(Context context) {
		this(context, MA1_WINDOW, MA2_WINDOW, POWER_CUTOFF_VALUE);
	}

	public IPMovingAverageStepDetector(Context context, double windowMA1,
			double windowMA2, double powerCutoff) {
		super(context);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mWindowMA1 = windowMA1;
		mWindowMA2 = windowMA2;
		mPowerCutoff = (float) powerCutoff;

		MAValues = new float[4];
		mMASwapState = true;
		MAFilters = new IPSensorMovingAverageTD[] {
				new IPSensorMovingAverageTD(mWindowMA1),
				new IPSensorMovingAverageTD(mWindowMA1),
				new IPSensorMovingAverageTD(mWindowMA2) };
		CSP_Filter = new IPSensorCumulativeSignalPowerTD();
		mStepDetected = false;
		mSignalPowerCutoff = true;
	}

	@Override
	public void reset() {
		MAValues = new float[4];
		mMASwapState = true;
		MAFilters = new IPSensorMovingAverageTD[] {
				new IPSensorMovingAverageTD(mWindowMA1),
				new IPSensorMovingAverageTD(mWindowMA1),
				new IPSensorMovingAverageTD(mWindowMA2) };
		CSP_Filter.reset();
		mStepDetected = false;
		mSignalPowerCutoff = true;
	}

	@Override
	public void start() {
		if (mSensor != null) {
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	public void stop() {
		mSensorManager.unregisterListener(this);
	};

	class MovingAverageStepDetectorState {
		float[] values;
		boolean[] states;
		double duration;

		MovingAverageStepDetectorState(float[] values, boolean[] states,
				double duration) {
			this.values = values;
			this.states = states;
			this.duration = duration;
		}
	}

	public MovingAverageStepDetectorState getState() {
		return new MovingAverageStepDetectorState(new float[] { MAValues[0],
				MAValues[1], MAValues[2], MAValues[3] }, new boolean[] {
				mStepDetected, mSignalPowerCutoff }, mStrideDuration);
	}

	public float getPowerThreshold() {
		return mPowerCutoff;
	}

	private double getStrideDuration() {
		long currentStepTimestamp = System.nanoTime();
		double strideDuration;
		strideDuration = (double) (currentStepTimestamp - mLastStepTimestamp)
				/ NANO;
		if (strideDuration > MAX_STRIDE_DURATION) {
			strideDuration = Double.NaN;
		}
		mLastStepTimestamp = currentStepTimestamp;
		return strideDuration;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			processAccelerometerValues(event.timestamp, event.values);
		}
	}

	private void processAccelerometerValues(long timestamp, float[] values) {
		float value = values[2];

		MAValues[0] = value;

		for (int i = 1; i < 3; i++) {
			MAFilters[i].push(timestamp, value);
			MAValues[i] = (float) MAFilters[i].getAverage();
			value = MAValues[i];
		}

		mStepDetected = false;
		boolean newSwapState = MAValues[1] > MAValues[2];
		if (newSwapState != mMASwapState) {
			mMASwapState = newSwapState;
			if (mMASwapState) {
				mStepDetected = true;
			}
		}

		CSP_Filter.push(timestamp, MAValues[1] - MAValues[2]);
		MAValues[3] = (float) CSP_Filter.getValue();
		mSignalPowerCutoff = MAValues[3] < mPowerCutoff;

		if (mStepDetected) {
			CSP_Filter.reset();
		}

		if (mStepDetected && !mSignalPowerCutoff) {
			mStrideDuration = getStrideDuration();
			notifyOnStepEvent(new IPStepEvent(1.0, mStrideDuration));
		}
	}
}
