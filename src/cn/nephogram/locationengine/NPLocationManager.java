package cn.nephogram.locationengine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.ibeacon.sdk.Region;
import cn.nephogram.locationengine.NPXLocationEngine.NPXLocationEngineListener;

public class NPLocationManager implements NPXLocationEngineListener {

	private static final double DEFAULT_REQUEST_TIME_OUT = 4.0f;
	private static final long DEFAULT_CHECK_INERVAL = 1000;

	private double requestTimeOut = DEFAULT_REQUEST_TIME_OUT;

	private NPXLocationEngine locationEngine;
	private Region beaconRegion;
	private String beaconPath;

	private NPLocalPoint lastLocation;

	private boolean isLocating;

	long lastTimeLocationUpdated;
	private Handler locationUpdatingCheckerHandler = new Handler();
	private Runnable locationUpdatingCheckerRunnable = new Runnable() {

		@Override
		public void run() {
			if (isLocating) {
				long now = System.currentTimeMillis();
				if (now - lastTimeLocationUpdated > (long) (requestTimeOut * 1000)) {
					notifyFailUpdateLocation();
				}
				locationUpdatingCheckerHandler.postDelayed(
						locationUpdatingCheckerRunnable, DEFAULT_CHECK_INERVAL);
			}
		}
	};

	public NPLocationManager(Context context, String beaconDBPath) {
		this.beaconPath = beaconDBPath;
		locationEngine = new NPXLocationEngine(context, beaconPath);
		locationEngine.addLocationEngineListener(this);
	}

	public void startUpdateLocation() {
		if (isLocating) {
			return;
		}

		isLocating = true;
		locationEngine.start();

		locationUpdatingCheckerHandler.postDelayed(
				locationUpdatingCheckerRunnable, DEFAULT_CHECK_INERVAL);
		lastTimeLocationUpdated = System.currentTimeMillis();
	}

	public void stopUpdateLocation() {
		if (!isLocating) {
			return;
		}

		isLocating = false;
		locationEngine.stop();
	}

	public NPLocalPoint getLastLocation() {
		return lastLocation;
	}

	// public NPLocalPoint getDirectioLocation() {
	//
	// }

	@Override
	public void locationChanged(NPXLocationEngine engine, NPLocalPoint lp) {
		lastTimeLocationUpdated = System.currentTimeMillis();

		if (lp.getFloor() == 0) {
			if (lastLocation == null) {
				lp.setFloor(1);
			} else {
				lp.setFloor(lastLocation.getFloor());
			}
		}

		notifyLocationUpdated(lp);
		lastLocation = lp;
	}

	public void setBeaconRegion(Region region) {
		beaconRegion = region;
		locationEngine.setBeaconRegion(beaconRegion);
	}

	public void setRequestTimeOut(double rt) {
		this.requestTimeOut = rt;
	}

	public double getRequestTimeOut() {
		return requestTimeOut;
	}

	public interface NPLocationManagerListener {
		void didUpdateLocation(NPLocationManager locationManager,
				NPLocalPoint lp);

		void didFailUpdateLocation(NPLocationManager locationManager);
	}

	private List<NPLocationManagerListener> locationListeners = new ArrayList<NPLocationManagerListener>();

	public void addLocationEngineListener(NPLocationManagerListener listener) {
		if (!locationListeners.contains(listener)) {
			locationListeners.add(listener);
		}
	}

	public void removeLocationEngineListener(NPLocationManagerListener listener) {
		if (locationListeners.contains(listener)) {
			locationListeners.remove(listener);
		}
	}

	private void notifyLocationUpdated(NPLocalPoint lp) {
		for (NPLocationManagerListener listener : locationListeners) {
			listener.didUpdateLocation(this, lp);
		}
	}

	private void notifyFailUpdateLocation() {
		for (NPLocationManagerListener listener : locationListeners) {
			listener.didFailUpdateLocation(this);
		}
	}

}
