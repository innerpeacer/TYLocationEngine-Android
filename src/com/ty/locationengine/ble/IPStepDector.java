package com.ty.locationengine.ble;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

abstract class IPStepDector implements SensorEventListener {
	protected List<TYStepListener> mStepListeners = new ArrayList<TYStepListener>();
	private Context context;
	protected SensorManager mSensorManager;

	// protected Sensor mSensor;

	public abstract void reset();

	public IPStepDector(Context context) {
		this.context = context;
		mSensorManager = (SensorManager) this.context
				.getSystemService(Context.SENSOR_SERVICE);
	}

	public void registerStepListener(TYStepListener listener) {
		if (listener != null) {
			if (!mStepListeners.contains(listener)) {
				mStepListeners.add(listener);
			}
		}
	}

	public void unregisterStepListener(TYStepListener listener) {
		if (mStepListeners.contains(listener)) {
			mStepListeners.remove(listener);
		}
	}

	public abstract void start();

	public abstract void stop();

	// public void start() {
	// if (mSensor != null) {
	// mSensorManager.registerListener(this, mSensor,
	// SensorManager.SENSOR_DELAY_FASTEST);
	// }
	// }

	// public void stop() {
	// mSensorManager.unregisterListener(this);
	// }

	public void onSensorChanged(SensorEvent event) {

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	protected void notifyOnStepEvent(IPStepEvent event) {
		for (TYStepListener listener : mStepListeners) {
			listener.onStepEvent(event);
		}
	}

	// public static StepDector stepDetectorFactory(String detectorName) {
	// if (detectorName.equals("moving_average")) {
	// return new MovingAverageStepDetector();
	// } else if (detectorName.equals("null")) {
	// return null;
	// }
	//
	// return new StepDector();
	// }

	public interface TYStepListener {
		public void onStepEvent(IPStepEvent event);
	}

}
