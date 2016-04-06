package com.ty.bleproject.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;

import com.ty.locationengine.ibeacon.Beacon;
import com.ty.locationengine.ibeacon.BeaconManager;
import com.ty.locationengine.ibeacon.BeaconManager.RangingListener;
import com.ty.locationengine.ibeacon.BeaconRegion;

public class TestActivity extends Activity implements RangingListener {
	static final String TAG = TestActivity.class.getSimpleName();
	private BeaconManager beaconManager;
	private BeaconRegion beaconRegion;

	public static long startTime;

	Map<String, Beacon> beaconMap = new HashMap<String, Beacon>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(this);

		beaconMap.clear();

		this.beaconRegion = new BeaconRegion("TuYa",
				"ECB33B47-781F-4C16-8513-73FCBB7134F2", 60002, null);
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					startTime = System.currentTimeMillis();
					beaconManager.startRanging(beaconRegion);
				} catch (RemoteException e) {
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");

		Date date = new Date(System.currentTimeMillis());
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, date.toString() + "_"
				+ System.currentTimeMillis() + ".txt");

		StringBuffer buffer = new StringBuffer();
		for (Beacon b : beaconMap.values()) {
			buffer.append(b.getMacAddress()).append("\t").append(b.getMajor())
					.append("\t").append(b.getMinor()).append("\n");
		}
		writeFileSdcard(file.toString(), buffer.toString());

		beaconManager.disconnect();
	}

	public void writeFileSdcard(String fileName, String message) {
		try {
			// FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onBeaconsDiscovered(BeaconRegion region, List<Beacon> beacons) {
		// Log.i(TAG, "onBeaconsDiscovered");
		for (Beacon beacon : beacons) {
			Log.i(TAG, beacon.getMacAddress() + "\t" + beacon.getMajor() + "\t"
					+ beacon.getMinor());
			if (!beaconMap.containsKey(beacon.getMacAddress())) {
				beaconMap.put(beacon.getMacAddress(), beacon);
			}
		}
	}
}
