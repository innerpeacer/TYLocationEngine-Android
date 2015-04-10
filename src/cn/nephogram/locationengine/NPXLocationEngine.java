package cn.nephogram.locationengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.ibeacon.sdk.Beacon;
import cn.nephogram.ibeacon.sdk.BeaconManager;
import cn.nephogram.ibeacon.sdk.BeaconManager.RangingListener;
import cn.nephogram.ibeacon.sdk.Region;
import cn.nephogram.ibeacon.sdk.Utils;
import cn.nephogram.locationengine.headingdetector.NPHeadingDetector;
import cn.nephogram.locationengine.headingdetector.NPHeadingDetector.NPHeadingListener;
import cn.nephogram.locationengine.headingdetector.NPMagneticHeadingDetector;
import cn.nephogram.locationengine.stepdetector.NPMovingAverageStepDetector;
import cn.nephogram.locationengine.stepdetector.NPStepDector;
import cn.nephogram.locationengine.stepdetector.NPStepDector.NPStepListener;
import cn.nephogram.locationengine.stepdetector.NPStepEvent;
import cn.nephogram.locationengine.swig.BLELocationEngine;
import cn.nephogram.locationengine.swig.ILocationEngine;
import cn.nephogram.locationengine.swig.NPXAlgorithmType;
import cn.nephogram.locationengine.swig.NPXPoint;
import cn.nephogram.locationengine.swig.NPXPublicBeacon;
import cn.nephogram.locationengine.swig.NPXScannedBeacon;
import cn.nephogram.locationengine.swig.VectorOfPublicBeacon;
import cn.nephogram.locationengine.swig.VectorOfScannedBeaconPointer;

public class NPXLocationEngine implements RangingListener, NPStepListener,
		NPHeadingListener {
	static final String TAG = NPXLocationEngine.class.getSimpleName();
	static final int REQUEST_ENABLE_BT = 1234;

	// private static final String DEFAULT_UUID =
	// "E2879308-4FA3-4F30-AC22-19ECDCB0D8C8";
	// private static final Region DEFAULT_REGION = new Region("Nephogram",
	// DEFAULT_UUID, 1, null);

	final Context context;
	private BeaconManager beaconManager;

	private SparseArray<NPXPublicBeacon> publicBeaconArray;
	private List<Beacon> scannedBeacons = new ArrayList<Beacon>();

	private ILocationEngine locationEngine;

	private NPStepDector stepDetector;
	private NPHeadingDetector headingDetector;
	private boolean isLocating;

	private String dbPath;

	private Region beaconRegion;

	private boolean limitBeaconNumber = true;
	private static final int DEFAULT_MAX_BEACON_NUMBER_FOR_PROCESSING = 9;
	private int maxBeaconNumberForProcessing = DEFAULT_MAX_BEACON_NUMBER_FOR_PROCESSING;

	private int RSSI_LEVEL_THRESHOLD = -75;
	private int BEACON_NUMBER_FOR_LEVEL_CHECK = 3;
	private int rssiThrehold = RSSI_LEVEL_THRESHOLD;

	public NPXLocationEngine(Context context, String beaconDBPath) {
		this.context = context;
		this.dbPath = beaconDBPath;

		locationEngine = BLELocationEngine
				.CreateNPXStepBaseTriangulationEngine(NPXAlgorithmType.NPXQuadraticWeighting);
		initAlgorithm();

		for (int i = 0; i < publicBeaconArray.size(); ++i) {
			NPXPublicBeacon pb = publicBeaconArray.valueAt(i);
			// Log.i(TAG, "Major: " + pb.getMajor() + ", Minor " +
			// pb.getMinor());
		}

		beaconManager = new BeaconManager(context);
		beaconManager.setRangingListener(this);

		stepDetector = new NPMovingAverageStepDetector(context);
		stepDetector.registerStepListener(this);

		// headingDetector = new NPAMGFusedHeadingDetector(context);
		headingDetector = new NPMagneticHeadingDetector(context);
		headingDetector.registerHeadingListener(this);

		isLocating = false;
	}

	public void setBeaconRegion(Region region) {
		this.beaconRegion = region;
	}

	public void onStepEvent(NPStepEvent event) {
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

		publicBeaconArray = new SparseArray<NPXPublicBeacon>();
		VectorOfPublicBeacon pbVector = new VectorOfPublicBeacon();

		NPBeaconDBAdapter db = new NPBeaconDBAdapter(context, dbPath);
		db.open();
		List<NPPublicBeacon> pbList = db.getAllNephogramBeacons();
		for (NPPublicBeacon pb : pbList) {
			addToLog(pb.toString());

			NPXPoint location = new NPXPoint(pb.getLocation().getX(), pb
					.getLocation().getY(), pb.getLocation().getFloor());
			NPXPublicBeacon xpb = new NPXPublicBeacon(pb.getUUID(),
					pb.getMajor(), pb.getMinor(), location);
			publicBeaconArray.put(NPBeaconKey.beaconKeyForNPBeacon(pb), xpb);

			pbVector.add(xpb);
		}
		db.close();

		// addToLog(pbVector.size() + " beacon in database...");

		locationEngine.Initilize(pbVector);
	}

	public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
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
				NPXScannedBeacon sb = new NPXScannedBeacon(
						b.getProximityUUID(), b.getMajor(), b.getMinor(),
						b.getRssi(), Utils.computeAccuracy(b),
						NPXProximityConverter.fromProximity(Utils
								.computeProximity(b)));
				sbpVector.add(sb);
			}
		} else {
			for (Beacon b : scannedBeacons) {
				NPXScannedBeacon sb = new NPXScannedBeacon(
						b.getProximityUUID(), b.getMajor(), b.getMinor(),
						b.getRssi(), Utils.computeAccuracy(b),
						NPXProximityConverter.fromProximity(Utils
								.computeProximity(b)));
				sbpVector.add(sb);
			}
		}

		// Log.i(TAG, "scanned beacon vector: " + sbpVector.size());

		locationEngine.processBeacons(sbpVector);
		NPXPoint p = locationEngine.getLocation();

		if (p.equal_point(BLELocationEngine.getINVALID_POINT())) {
			// Log.i(TAG, "INVALID_POINT");
		} else {
			NPLocalPoint lp = new NPLocalPoint(p.getX(), p.getY(), p.getFloor());

			int currentFloor = calculateCurrentFloor();
			lp.setFloor(currentFloor);

			notifyLocationChanged(lp);
			// Log.i(TAG,
			// lp.getX() + ", " + lp.getY() + " in Floor " + lp.getFloor());
		}
	}

	int calculateCurrentFloor() {
		int index = Math.min(9, scannedBeacons.size());

		HashMap<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < index; ++i) {
			Beacon b = scannedBeacons.get(i);
			NPXPublicBeacon pb = publicBeaconArray.get(NPBeaconKey
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
			if (publicBeaconArray.indexOfKey(NPBeaconKey
					.beaconKeyForBeacon(beacon)) < 0) {
				toRemove.add(beacon);
			}
		}
		scannedBeacons.removeAll(toRemove);
		Collections.sort(scannedBeacons, BEACON_ACCURACY_COMPARATOR);

		addToLog("scannedBeacons: " + scannedBeacons.size());
	}

	private static final Comparator<Beacon> BEACON_ACCURACY_COMPARATOR = new Comparator<Beacon>() {
		public int compare(Beacon lhs, Beacon rhs) {
			return Double.compare(Utils.computeAccuracy(lhs),
					Utils.computeAccuracy(rhs));
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

	public interface NPXLocationEngineListener {
		void locationChanged(NPXLocationEngine engine, NPLocalPoint lp);

		void headingChanged(NPXLocationEngine engine, double newHeading);
	}

	private List<NPXLocationEngineListener> locationListeners = new ArrayList<NPXLocationEngineListener>();

	public void addLocationEngineListener(NPXLocationEngineListener listener) {
		if (!locationListeners.contains(listener)) {
			locationListeners.add(listener);
		}
	}

	public void removeLocationEngineListener(NPXLocationEngineListener listener) {
		if (locationListeners.contains(listener)) {
			locationListeners.remove(listener);
		}
	}

	private void notifyLocationChanged(NPLocalPoint lp) {
		for (NPXLocationEngineListener listener : locationListeners) {
			listener.locationChanged(this, lp);
		}
	}

	private void notifyHeadingChanged(double newHeading) {
		for (NPXLocationEngineListener listener : locationListeners) {
			listener.headingChanged(this, newHeading);
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
