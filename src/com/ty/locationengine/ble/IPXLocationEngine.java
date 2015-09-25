package com.ty.locationengine.ble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.ty.locationengine.ble.IPHeadingDetector.TYHeadingListener;
import com.ty.locationengine.ble.IPStepDector.TYStepListener;
import com.ty.locationengine.ibeacon.Beacon;
import com.ty.locationengine.ibeacon.BeaconManager;
import com.ty.locationengine.ibeacon.BeaconManager.RangingListener;
import com.ty.locationengine.ibeacon.BeaconRegion;
import com.ty.locationengine.ibeacon.BeaconUtils;
import com.ty.locationengine.swig.ILocationEngine;
import com.ty.locationengine.swig.IPXAlgorithmType;
import com.ty.locationengine.swig.IPXPoint;
import com.ty.locationengine.swig.IPXPublicBeacon;
import com.ty.locationengine.swig.IPXScannedBeacon;
import com.ty.locationengine.swig.TYLocationEngine;
import com.ty.locationengine.swig.VectorOfPublicBeacon;
import com.ty.locationengine.swig.VectorOfScannedBeaconPointer;
import com.ty.mapdata.TYLocalPoint;

class IPXLocationEngine implements RangingListener, TYStepListener,
		TYHeadingListener {
	static final String TAG = IPXLocationEngine.class.getSimpleName();
	static final int REQUEST_ENABLE_BT = 1234;

	final Context context;
	private BeaconManager beaconManager;

	private SparseArray<IPXPublicBeacon> publicBeaconArray;
	private List<Beacon> scannedBeacons = new ArrayList<Beacon>();

	private ILocationEngine locationEngine;

	private IPStepDector stepDetector;
	private IPHeadingDetector headingDetector;
	private boolean isLocating;

	private String dbPath;

	private BeaconRegion beaconRegion;

	private boolean limitBeaconNumber = true;
	private static final int DEFAULT_MAX_BEACON_NUMBER_FOR_PROCESSING = 9;
	private int maxBeaconNumberForProcessing = DEFAULT_MAX_BEACON_NUMBER_FOR_PROCESSING;

	private int RSSI_LEVEL_THRESHOLD = -90;
	// private int BEACON_NUMBER_FOR_LEVEL_CHECK = 3;
	private int rssiThrehold = RSSI_LEVEL_THRESHOLD;

	public IPXLocationEngine(Context context, String beaconDBPath) {
		this.context = context;
		this.dbPath = beaconDBPath;

		locationEngine = TYLocationEngine
				.CreateIPXStepBaseTriangulationEngine(IPXAlgorithmType.IPXQuadraticWeighting);
		initAlgorithm();

		// for (int i = 0; i < publicBeaconArray.size(); ++i) {
		// NPXPublicBeacon pb = publicBeaconArray.valueAt(i);
		// Log.i(TAG, "Major: " + pb.getMajor() + ", Minor " +
		// pb.getMinor());
		// }

		beaconManager = new BeaconManager(context);
		beaconManager.setRangingListener(this);

		stepDetector = new IPMovingAverageStepDetector(context);
		stepDetector.registerStepListener(this);

		// headingDetector = new NPAMGFusedHeadingDetector(context);
		headingDetector = new IPMagneticHeadingDetector(context);
		headingDetector.registerHeadingListener(this);

		isLocating = false;
	}

	public void setBeaconRegion(BeaconRegion region) {
		this.beaconRegion = region;
	}

	public void onStepEvent(IPStepEvent event) {
		locationEngine.addStepEvent();
	};

	@Override
	public void onHeadingChanged(float newHeading) {
		notifyHeadingChanged(newHeading);
	}

	public void start() {
		if (isLocating) {
			return;
		}

		isLocating = true;
		startRanging();
		stepDetector.start();
		headingDetector.start();
	}

	public void stop() {
		if (!isLocating) {
			return;
		}

		isLocating = false;
		locationEngine.reset();
		stopRanging();
		stepDetector.stop();
		headingDetector.stop();
	}

	private void initAlgorithm() {
		// addToLog("initAlgorithm...");

		publicBeaconArray = new SparseArray<IPXPublicBeacon>();
		VectorOfPublicBeacon pbVector = new VectorOfPublicBeacon();

		TYBeaconDBAdapter db = new TYBeaconDBAdapter(context, dbPath);
		db.open();
		List<TYPublicBeacon> pbList = db.getAllLocationingBeacons();
		for (TYPublicBeacon pb : pbList) {
			// addToLog(pb.toString());

			IPXPoint location = new IPXPoint(pb.getLocation().getX(), pb
					.getLocation().getY(), pb.getLocation().getFloor());
			IPXPublicBeacon xpb = new IPXPublicBeacon(pb.getUUID(),
					pb.getMajor(), pb.getMinor(), location);
			publicBeaconArray.put(IPBeaconKey.beaconKeyForNPBeacon(pb), xpb);

			pbVector.add(xpb);
		}
		db.close();

		// addToLog(pbVector.size() + " beacon in database...");

		locationEngine.Initilize(pbVector);
	}

	public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> beacons) {
		if (beacons.size() == 0) {
			return;
		}
		// addToLog("onBeaconsDiscovered: " + beacons.size());

		preprocessBeacons(beacons);

		if (scannedBeacons.size() == 0) {
			return;
		}

		{
			Beacon firstBeacon = scannedBeacons.get(0);
			if (firstBeacon.getRssi() < rssiThrehold) {
				return;
			}
		}

		VectorOfScannedBeaconPointer sbpVector = new VectorOfScannedBeaconPointer();

		// boolean limitBeaconNumber = false;
		if (limitBeaconNumber) {
			int index = Math.min(scannedBeacons.size(),
					maxBeaconNumberForProcessing);
			for (int i = 0; i < index; ++i) {
				Beacon b = scannedBeacons.get(i);
				IPXScannedBeacon sb = new IPXScannedBeacon(
						b.getProximityUUID(), b.getMajor(), b.getMinor(),
						b.getRssi(), BeaconUtils.computeAccuracy(b),
						IPXProximityConverter.fromProximity(BeaconUtils
								.computeProximity(b)));
				sbpVector.add(sb);
			}
		} else {
			for (Beacon b : scannedBeacons) {
				IPXScannedBeacon sb = new IPXScannedBeacon(
						b.getProximityUUID(), b.getMajor(), b.getMinor(),
						b.getRssi(), BeaconUtils.computeAccuracy(b),
						IPXProximityConverter.fromProximity(BeaconUtils
								.computeProximity(b)));
				sbpVector.add(sb);
			}
		}

		// Log.i(TAG, "scanned beacon vector: " + sbpVector.size());

		locationEngine.processBeacons(sbpVector);
		IPXPoint p = locationEngine.getLocation();

		if (p.equal_point(TYLocationEngine.getINVALID_POINT())) {
			// Log.i(TAG, "INVALID_POINT");
		} else {
			TYLocalPoint lp = new TYLocalPoint(p.getX(), p.getY(), p.getFloor());

			int currentFloor = calculateCurrentFloor();
			lp.setFloor(currentFloor);

			notifyLocationChanged(lp);
			// Log.i(TAG,
			// lp.getX() + ", " + lp.getY() + " in Floor " + lp.getFloor());
		}
	}

	@SuppressLint("UseSparseArrays")
	int calculateCurrentFloor() {
		int index = Math.min(9, scannedBeacons.size());

		HashMap<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < index; ++i) {
			Beacon b = scannedBeacons.get(i);
			IPXPublicBeacon pb = publicBeaconArray.get(IPBeaconKey
					.beaconKeyForBeacon(b));
			if (!frequencyMap.keySet().contains(pb.getLocation().getFloor())) {
				frequencyMap.put(pb.getLocation().getFloor(), 0);
			}

			int count = frequencyMap.get(pb.getLocation().getFloor());
			frequencyMap.put(pb.getLocation().getFloor(), count + 1);
		}

		int maxCount = 0;
		int maxFloor = 1;
		for (int floor : frequencyMap.keySet()) {
			int floorCount = frequencyMap.get(floor);
			if (floorCount > maxCount) {
				maxCount = floorCount;
				maxFloor = floor;
			}
		}

		return maxFloor;
	}

	private void preprocessBeacons(List<Beacon> beacons) {
		scannedBeacons.clear();
		for (int i = 0; i < beacons.size(); ++i) {
			Beacon b = beacons.get(i);
			if (b.getRssi() < 0) {
				scannedBeacons.add(b);
			}
		}

		List<Beacon> toRemove = new ArrayList<Beacon>();
		for (Beacon beacon : scannedBeacons) {
			if (publicBeaconArray.indexOfKey(IPBeaconKey
					.beaconKeyForBeacon(beacon)) < 0) {
				toRemove.add(beacon);
			}
		}
		scannedBeacons.removeAll(toRemove);
		Collections.sort(scannedBeacons, BEACON_ACCURACY_COMPARATOR);

		// addToLog("scannedBeacons: " + scannedBeacons.size());
	}

	private static final Comparator<Beacon> BEACON_ACCURACY_COMPARATOR = new Comparator<Beacon>() {
		public int compare(Beacon lhs, Beacon rhs) {
			return Double.compare(BeaconUtils.computeAccuracy(lhs),
					BeaconUtils.computeAccuracy(rhs));
		}
	};

	private void startRanging() {
		connectToService();
	}

	private void stopRanging() {
		try {
			beaconManager.stopRanging(beaconRegion);
			beaconManager.disconnect();
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}
	}

	private void connectToService() {
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(beaconRegion);
				} catch (RemoteException e) {
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});
	}

	interface IPXLocationEngineListener {
		// void locationChanged(IPXLocationEngine engine, TYLocalPoint lp);
		void locationChanged(TYLocalPoint lp);

		// void headingChanged(IPXLocationEngine engine, double newHeading);
		void headingChanged(double newHeading);

	}

	private List<IPXLocationEngineListener> locationListeners = new ArrayList<IPXLocationEngineListener>();

	public void addLocationEngineListener(IPXLocationEngineListener listener) {
		if (!locationListeners.contains(listener)) {
			locationListeners.add(listener);
		}
	}

	public void removeLocationEngineListener(IPXLocationEngineListener listener) {
		if (locationListeners.contains(listener)) {
			locationListeners.remove(listener);
		}
	}

	private void notifyLocationChanged(TYLocalPoint lp) {
		for (IPXLocationEngineListener listener : locationListeners) {
			listener.locationChanged(lp);
		}
	}

	private void notifyHeadingChanged(double newHeading) {
		for (IPXLocationEngineListener listener : locationListeners) {
			listener.headingChanged(newHeading);
		}
	}

	public void setLimitBeaconNumber(boolean limitBeaconNumber) {
		this.limitBeaconNumber = limitBeaconNumber;
	}

	public void setMaxBeaconNumberForProcessing(int maxBeaconNumberForProcessing) {
		this.maxBeaconNumberForProcessing = maxBeaconNumberForProcessing;
	}

	public void setRssiThreshold(int threshold) {
		rssiThrehold = threshold;
	}

	void addToLog(String log) {
		Log.i(TAG, log);
	}
}
