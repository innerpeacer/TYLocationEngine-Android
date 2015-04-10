package cn.nephogram.activities;

import android.os.Bundle;
import android.util.Log;
import cn.nephogram.app.DataManager;
import cn.nephogram.ble.R;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.ibeacon.sdk.BeaconManager;
import cn.nephogram.locationengine.NPLocationManager;
import cn.nephogram.locationengine.NPLocationManager.NPLocationManagerListener;
import cn.nephogram.mapsdk.NPMapView.NPMapViewMode;
import cn.nephogram.mapsdk.NPPictureSymbol;

import com.esri.core.geometry.Point;

public class NephogramLocationActivity extends BaseMapViewActivity implements
		NPLocationManagerListener {
	static final String TAG = NephogramLocationActivity.class.getSimpleName();

	BeaconManager beaconManager;
	NPLocationManager locationManager;

	NPPictureSymbol locationSymbol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		beaconManager = new BeaconManager(this);
		Log.i(TAG, DataManager.getBeaconDBPath());

		locationManager = new NPLocationManager(this,
				DataManager.getBeaconDBPath());

		locationManager.setBeaconRegion(DataManager.getRegion());
		locationManager.addLocationEngineListener(this);

		locationSymbol = new NPPictureSymbol(getResources().getDrawable(
				R.drawable.location_arrow));
		locationSymbol.setWidth(30);
		locationSymbol.setHeight(30);

		mapView.setLocationSymbol(locationSymbol);
		mapView.setMapMode(NPMapViewMode.NPMapViewModeFollowing);
	}

	@Override
	public void didFailUpdateLocation(NPLocationManager locationManager) { // resultLayer.removeAll();
		mapView.removeLocation();
	}

	@Override
	public void didUpdateLocation(NPLocationManager locationManager,
			NPLocalPoint lp) {
		mapView.showLocation(lp);
		mapView.centerAt(new Point(lp.getX(), lp.getY()), true);
	}

	@Override
	public void didUpdateDeviceHeading(NPLocationManager locationManager,
			double newHeading) {
		Log.i(TAG, "didUpdateDeviceHeading: " + newHeading);
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
		System.loadLibrary("BLELocationEngine");
	}
}
