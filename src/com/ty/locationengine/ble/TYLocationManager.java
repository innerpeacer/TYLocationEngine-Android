package com.ty.locationengine.ble;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ty.locationengine.ble.IPXLocationEngine.IPXLocationEngineListener;
import com.ty.locationengine.ibeacon.BeaconRegion;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYMapEnvironment;

public class TYLocationManager implements IPXLocationEngineListener {
	static final String TAG = TYLocationManager.class.getSimpleName();

	private static final double DEFAULT_REQUEST_TIME_OUT = 4.0f;
	private static final long DEFAULT_CHECK_INERVAL = 1000;

	private double requestTimeOut = DEFAULT_REQUEST_TIME_OUT;

	private IPXLocationEngine locationEngine;
	private BeaconRegion beaconRegion;
	private String beaconPath;

	private TYLocalPoint lastLocation;

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

	// TYLocationManager(Context context, String beaconDBPath) {
	// this.beaconPath = beaconDBPath;
	// locationEngine = new IPXLocationEngine(context, beaconPath);
	// locationEngine.addLocationEngineListener(this);
	// }

	public TYLocationManager(Context context, TYBuilding building) {
		// if (!building.getBuildingID().equalsIgnoreCase("00210100")) {
		// return;
		// }

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date invalidDate = dateFormat.parse("2017-10-11");

			Date now = new Date();
			boolean isInvalid = now.after(invalidDate);
			if (isInvalid) {
				return;
			}
		} catch (ParseException e) {
			return;
		}

		String dbName = String.format("%s_Beacon.db", building.getBuildingID());
		this.beaconPath = new File(
				TYMapEnvironment.getDirectoryForBuilding(building), dbName)
				.toString();
		locationEngine = new IPXLocationEngine(context, beaconPath);
		locationEngine.addLocationEngineListener(this);
	}

	public void startUpdateLocation() {
		if (isLocating) {
			return;
		}

		Log.i(TAG, "startUpdateLocation");

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

	public TYLocalPoint getLastLocation() {
		return lastLocation;
	}

	// public NPLocalPoint getDirectioLocation() {
	//
	// }

	public void setLimitBeaconNumber(boolean limitBeaconNumber) {
		// this.limitBeaconNumber = limitBeaconNumber;
		locationEngine.setLimitBeaconNumber(limitBeaconNumber);
	}

	public void setMaxBeaconNumberForProcessing(int maxBeaconNumberForProcessing) {
		// this.maxBeaconNumberForProcessing = maxBeaconNumberForProcessing;
		locationEngine
				.setMaxBeaconNumberForProcessing(maxBeaconNumberForProcessing);
	}

	public void setRssiThreshold(int threshold) {
		locationEngine.setRssiThreshold(threshold);
	}

	// @Override
	// public void locationChanged(IPXLocationEngine engine, TYLocalPoint lp) {
	// lastTimeLocationUpdated = System.currentTimeMillis();
	//
	// if (lp.getFloor() == 0) {
	// if (lastLocation == null) {
	// lp.setFloor(1);
	// } else {
	// lp.setFloor(lastLocation.getFloor());
	// }
	// }
	//
	// notifyLocationUpdated(lp);
	// lastLocation = lp;
	// }

	// @Override
	// public void headingChanged(IPXLocationEngine engine, double newHeading) {
	// notifyHeadingUpdated(newHeading);
	// }

	@Override
	public void locationChanged(TYLocalPoint lp) {
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

	@Override
	public void headingChanged(double newHeading) {
		notifyHeadingUpdated(newHeading);
	}

	public void setBeaconRegion(BeaconRegion region) {
		beaconRegion = region;
		locationEngine.setBeaconRegion(beaconRegion);
	}

	public void setRequestTimeOut(double rt) {
		this.requestTimeOut = rt;
	}

	public double getRequestTimeOut() {
		return requestTimeOut;
	}

	public interface TYLocationManagerListener {
		void didUpdateLocation(TYLocationManager locationManager,
				TYLocalPoint lp);

		void didFailUpdateLocation(TYLocationManager locationManager);

		void didUpdateDeviceHeading(TYLocationManager locationManager,
				double newHeading);
	}

	private List<TYLocationManagerListener> locationListeners = new ArrayList<TYLocationManagerListener>();

	public void addLocationEngineListener(TYLocationManagerListener listener) {
		if (!locationListeners.contains(listener)) {
			locationListeners.add(listener);
		}
	}

	public void removeLocationEngineListener(TYLocationManagerListener listener) {
		if (locationListeners.contains(listener)) {
			locationListeners.remove(listener);
		}
	}

	private void notifyLocationUpdated(TYLocalPoint lp) {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didUpdateLocation(this, lp);
		}
	}

	private void notifyFailUpdateLocation() {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didFailUpdateLocation(this);
		}
	}

	private void notifyHeadingUpdated(double newHeading) {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didUpdateDeviceHeading(this, newHeading);
		}
	}
}
