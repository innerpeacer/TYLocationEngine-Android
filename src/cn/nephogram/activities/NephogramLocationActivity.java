package cn.nephogram.activities;

import android.os.Bundle;
import android.util.Log;
import cn.nephogram.app.DataManager;
import cn.nephogram.ble.R;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.ibeacon.sdk.BeaconManager;
import cn.nephogram.locationengine.NPLocationManager;
import cn.nephogram.locationengine.NPLocationManager.NPLocationManagerListener;
import cn.nephogram.mapsdk.NPPictureSymbol;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;

public class NephogramLocationActivity extends BaseMapViewActivity implements
		NPLocationManagerListener {
	static final String TAG = NephogramLocationActivity.class.getSimpleName();

	BeaconManager beaconManager;
	NPLocationManager locationManager;

	GraphicsLayer resultLayer;
	NPPictureSymbol locationSymbol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		beaconManager = new BeaconManager(this);

		locationManager = new NPLocationManager(this,
				DataManager.getBeaconDBPath());
		locationManager.setBeaconRegion(DataManager.getRegion());
		locationManager.addLocationEngineListener(this);

		resultLayer = new GraphicsLayer();
		mapView.addLayer(resultLayer);

		locationSymbol = new NPPictureSymbol(getResources().getDrawable(
				R.drawable.location));
		locationSymbol.setWidth(20);
		locationSymbol.setHeight(20);
		resultLayer.setRenderer(new SimpleRenderer(locationSymbol));
	}

	@Override
	public void didFailUpdateLocation(NPLocationManager locationManager) {
		Log.i(TAG, "didFailUpdateLocation: " + System.currentTimeMillis()
				/ 1000.0f);
		resultLayer.removeAll();
	}

	@Override
	public void didUpdateLocation(NPLocationManager locationManager,
			NPLocalPoint lp) {
		Log.i(TAG, "didUpdateLocation: " + lp.toString());

		resultLayer.removeAll();

		// Graphic g = new Graphic();
		resultLayer.addGraphic(new Graphic(new Point(lp.getX(), lp.getY()),
				null));
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
