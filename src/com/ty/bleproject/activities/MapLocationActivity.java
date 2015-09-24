package com.ty.bleproject.activities;

import android.os.Bundle;
import android.util.Log;

import com.esri.core.geometry.Point;
import com.ty.bleproject.R;
import com.ty.bleproject.app.DataManager;
import com.ty.locationengine.ble.TYLocationManager;
import com.ty.locationengine.ble.TYLocationManager.TYLocationManagerListener;
import com.ty.locationengine.ibeacon.BeaconManager;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView.TYMapViewMode;

public class MapLocationActivity extends BaseMapViewActivity implements
		TYLocationManagerListener {
	static final String TAG = MapLocationActivity.class.getSimpleName();

	BeaconManager beaconManager;
	TYLocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		beaconManager = new BeaconManager(this);
		Log.i(TAG, TYMapEnvironment.getDirectoryForBuilding(currentBuilding));

		locationManager = new TYLocationManager(this, currentBuilding);

		locationManager.setBeaconRegion(DataManager.getRegion());
		locationManager.addLocationEngineListener(this);

		mapView.setMapMode(TYMapViewMode.TYMapViewModeDefault);
	}

	@Override
	public void didFailUpdateLocation(TYLocationManager locationManager) { // resultLayer.removeAll();
		mapView.removeLocation();
	}

	@Override
	public void didUpdateLocation(TYLocationManager locationManager,
			TYLocalPoint lp) {
		if (mapView.getCurrentMapInfo().getFloorNumber() != lp.getFloor()) {
			for (TYMapInfo info : mapInfos) {
				if (info.getFloorNumber() == lp.getFloor()) {
					currentMapInfo = info;
					mapView.setFloor(info);
					setTitle(String.format("%s-%s", currentBuilding.getName(),
							currentMapInfo.getFloorName()));
					break;
				}
			}
		}

		mapView.showLocation(lp);
		mapView.centerAt(new Point(lp.getX(), lp.getY()), true);
	}

	@Override
	public void didUpdateDeviceHeading(TYLocationManager locationManager,
			double newHeading) {
		// Log.i(TAG, "didUpdateDeviceHeading: " + newHeading);
		mapView.processDeviceRotation(newHeading);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_location;
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.startUpdateLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.stopUpdateLocation();
	}

	static {
		System.loadLibrary("TYLocationEngine");
	}
}
