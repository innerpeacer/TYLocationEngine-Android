package com.ty.bleproject.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ty.bleproject.R;
import com.ty.bleproject.app.TYRegionManager;
import com.ty.bleproject.app.TYUserDefaults;
import com.ty.locationengine.ble.TYBeacon;
import com.ty.locationengine.ble.TYLocationManager;
import com.ty.locationengine.ble.TYLocationManager.TYLocationManagerListener;
import com.ty.locationengine.ble.TYPublicBeacon;
import com.ty.locationengine.ibeacon.BeaconManager;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYBuildingManager;

public class MainActivity extends Activity implements TYLocationManagerListener {
	static final String TAG = MainActivity.class.getSimpleName();

	static final int REQUEST_ENABLE_BT = 1234;

	// =====================================

	private TextView textView;
	private List<String> logList;

	TYBuilding currentBuilding;

	BeaconManager beaconManager;
	TYLocationManager locationManager;
	TYRegionManager regionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView);

		logList = new ArrayList<String>();

		addToLog("onCreate...");

		TYUserDefaults pref = new TYUserDefaults(this);
		String cityID = pref.getDefaultCityID();
		String buildingID = pref.getDefaultBuildingID();
		currentBuilding = TYBuildingManager.parseBuildingFromFilesById(this,
				cityID, buildingID);

		beaconManager = new BeaconManager(this);
		regionManager = new TYRegionManager(this);

		locationManager = new TYLocationManager(this, currentBuilding);
		locationManager.setBeaconRegion(regionManager
				.getBeaconRegion(currentBuilding.getBuildingID()));
		locationManager.addLocationEngineListener(this);
	}

	@Override
	public void didRangedBeacons(TYLocationManager locationManager,
			List<TYBeacon> beacons) {

	}

	@Override
	public void didRangedLocationBeacons(TYLocationManager locationManager,
			List<TYPublicBeacon> beacons) {

	}

	@Override
	public void didFailUpdateLocation(TYLocationManager locationManager) {
		addToLog("didFailUpdateLocation: " + System.currentTimeMillis()
				/ 1000.0f);
	}

	@Override
	public void didUpdateLocation(TYLocationManager locationManager,
			TYLocalPoint lp) {
		addToLog("didUpdateLocation: " + lp.toString());
	}

	@Override
	public void didUpdateImmediateLocation(TYLocationManager locationManager,
			TYLocalPoint lp) {
		addToLog("Immediate Location: " + lp);
	}

	@Override
	public void didUpdateDeviceHeading(TYLocationManager locationManager,
			double newHeading) {
		addToLog("didUpdateDeviceHeading: " + newHeading);

	}

	void addToLog(String log) {

		if (logList.size() >= 25) {
			logList.remove(0);
		}
		logList.add(log);

		StringBuffer buffer = new StringBuffer();
		for (String s : logList) {
			buffer.append(s).append("\n");
		}
		textView.setText(buffer.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// checkBluetooth();
		locationManager.startUpdateLocation();
	}

	void checkBluetooth() {
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			locationManager.startUpdateLocation();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.stopUpdateLocation();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				locationManager.startUpdateLocation();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	static {
		System.loadLibrary("TYLocationEngine");
	}
}
