package cn.nephogram.locationengine.headingdetector;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public abstract class NPHeadingDetector implements SensorEventListener {
	static final float DEFAULT_SENSITIVITY = 10.0f;

	protected List<NPHeadingListener> mHeadingListener = new ArrayList<NPHeadingListener>();
	protected Context mContext;
	protected SensorManager mSensorManager;

	protected float mHeading;
	protected float sensitivity = DEFAULT_SENSITIVITY;
	protected float lastDeviceHeading = 0;

	public NPHeadingDetector(Context context) {
		this.mContext = context;
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
	}

	public void setSensitivity(float s) {
		this.sensitivity = s;
	}

	public float getHeading() {
		return this.mHeading;
	}

	public abstract void start();

	public abstract void stop();

	public void registerHeadingListener(NPHeadingListener listener) {
		if (listener != null) {
			if (!mHeadingListener.contains(listener)) {
				mHeadingListener.add(listener);
			}
		}
	}

	public void unregisterHeadingListener(NPHeadingListener listener) {
		if (mHeadingListener.contains(listener)) {
			mHeadingListener.remove(listener);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {

	}

	protected void notifyOnHeadingChanged(float newHeading) {
		if (Math.abs(newHeading - lastDeviceHeading) >= sensitivity) {
			for (NPHeadingListener listener : mHeadingListener) {
				listener.onHeadingChanged(newHeading);
			}
			lastDeviceHeading = newHeading;
		}
	}

	public interface NPHeadingListener {
		void onHeadingChanged(float newHeading);
	}
}
