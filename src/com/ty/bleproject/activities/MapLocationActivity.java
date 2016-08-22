package com.ty.bleproject.activities;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol;
import com.ty.bleproject.R;
import com.ty.bleproject.app.TYRegionManager;
import com.ty.locationengine.ble.TYBeacon;
import com.ty.locationengine.ble.TYLocationManager;
import com.ty.locationengine.ble.TYLocationManager.TYLocationManagerListener;
import com.ty.locationengine.ble.TYPublicBeacon;
import com.ty.locationengine.ibeacon.BeaconManager;
import com.ty.locationengine.ibeacon.BeaconRegion;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView.TYMapViewMode;

public class MapLocationActivity extends BaseMapViewActivity implements
		TYLocationManagerListener {
	static final String TAG = MapLocationActivity.class.getSimpleName();

	BeaconManager beaconManager;
	TYLocationManager locationManager;
	TYRegionManager regionManager;

	GraphicsLayer hintLayer;
	GraphicsLayer immediateLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		beaconManager = new BeaconManager(this);
		Log.i(TAG, TYMapEnvironment.getDirectoryForBuilding(currentBuilding));

		regionManager = new TYRegionManager(this);
		locationManager = new TYLocationManager(this, currentBuilding);

		locationManager.setBeaconRegion(regionManager
				.getBeaconRegion(currentBuilding.getBuildingID()));
		// locationManager.setBeaconRegion(new BeaconRegion("TuYa",
		// "FDA50693-A4E2-4FB1-AFCF-C6EB07647825", null, null));
		locationManager.setBeaconRegion(new BeaconRegion("Wanda",
				"A3FCE438-627C-42B7-AB72-DC6E55E137AC", 11000, null));
		locationManager.addLocationEngineListener(this);

		mapView.setMapMode(TYMapViewMode.TYMapViewModeDefault);
		// mapView.setMapMode(TYMapViewMode.TYMapViewModeFollowing);

		hintLayer = new GraphicsLayer();
		mapView.addLayer(hintLayer);

		immediateLayer = new GraphicsLayer();
		mapView.addLayer(immediateLayer);
	}

	void showHintRssiForLocationBeacons(List<TYPublicBeacon> beacons) {
		hintLayer.removeAll();

		for (TYPublicBeacon pb : beacons) {
			if (pb.getLocation().getFloor() == currentMapInfo.getFloorNumber()) {
				Point p = new Point(pb.getLocation().getX(), pb.getLocation()
						.getY());
				// RSSI
				{
					String rssi = String.format("%.2f, %d", pb.getAccuracy(),
							pb.getRssi());
					TextSymbol ts = new TextSymbol(10, rssi, Color.BLUE);
					ts.setOffsetX(5);
					ts.setOffsetY(5);
					Graphic graphic = new Graphic(p, ts);
					hintLayer.addGraphic(graphic);
				}
				// Minor
				{
					String minor = pb.getMinor() + "";
					TextSymbol ts = new TextSymbol(10, minor, Color.MAGENTA);
					ts.setOffsetX(5);
					ts.setOffsetY(-5);
					Graphic graphic = new Graphic(p, ts);
					hintLayer.addGraphic(graphic);
				}
				SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.RED, 5,
						STYLE.CIRCLE);
				sms.setOutline(new SimpleLineSymbol(Color.BLACK, 1));
				hintLayer.addGraphic(new Graphic(p, sms));
			}
		}
	}

	@Override
	public void didRangedBeacons(TYLocationManager locationManager,
			List<TYBeacon> beacons) {
		// Log.i(TAG, "didRangedBeacons: " + beacons.size());
		// Log.i(TAG, beacons.toString());
	}

	@Override
	public void didRangedLocationBeacons(TYLocationManager locationManager,
			List<TYPublicBeacon> beacons) {
		// Log.i(TAG, "didRangedLocationBeacons: " + beacons.size());
		// Log.i(TAG, beacons.toString());
		showHintRssiForLocationBeacons(beacons);
	}

	@Override
	public void didFailUpdateLocation(TYLocationManager locationManager) {
		// Log.i(TAG, "didFailUpdateLocation Location: ");

		// resultLayer.removeAll();
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
		Log.i(TAG, "Location: " + lp);
	}

	@Override
	public void didUpdateImmediateLocation(TYLocationManager locationManager,
			TYLocalPoint lp) {
		// Log.i(TAG, "Immediate Location: " + lp);
		immediateLayer.removeAll();
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.GREEN, 10,
				STYLE.CIRCLE);
		Graphic graphic = new Graphic(new Point(lp.getX(), lp.getY()), sms);
		immediateLayer.addGraphic(graphic);
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
