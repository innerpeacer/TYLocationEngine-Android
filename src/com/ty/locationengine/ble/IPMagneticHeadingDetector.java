package com.ty.locationengine.ble;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

class IPMagneticHeadingDetector extends IPHeadingDetector {
	private Sensor mMagneticSensor;

	public IPMagneticHeadingDetector(Context context) {
		super(context);
		mMagneticSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	@Override
	public void start() {
		if (mMagneticSensor != null) {
			mSensorManager.registerListener(this, mMagneticSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			double x = event.values[0];
			double y = event.values[1];

			IPVector2 v2 = new IPVector2(x, y);

			notifyOnHeadingChanged(-(float) v2.getAngle());
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
