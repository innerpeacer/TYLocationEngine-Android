package cn.nephogram.locationengine.headingdetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import cn.nephogram.locationengine.stepdetector.NPVector2;

public class NPMagneticHeadingDetector extends NPHeadingDetector {
	private Sensor mMagneticSensor;

	public NPMagneticHeadingDetector(Context context) {
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

			NPVector2 v2 = new NPVector2(x, y);

			notifyOnHeadingChanged(-(float) v2.getAngle());
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
