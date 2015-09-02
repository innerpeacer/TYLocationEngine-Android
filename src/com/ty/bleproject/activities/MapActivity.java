package com.ty.bleproject.activities;

import java.util.List;

import android.os.Bundle;

import com.esri.core.geometry.Point;
import com.ty.bleproject.R;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYPoi;

public class MapActivity extends BaseMapViewActivity {
	static final String TAG = MapActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// mapView.setHighlightPoiOnSelection(true);
	}

	@Override
	public void initContentViewID() {
		contentViewID = R.layout.activity_map_view;
	}

	int index = 0;

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {

		// NPPoi poi = mapView.extractRoomPoiOnCurrentFloor(mappoint.getX(),
		// mappoint.getY());
		//
		// Log.i(TAG, poi + "\n");
		// if (poi != null) {
		// mapView.highlightPoi(poi);
		// }
		// Log.i(TAG, mapView.getScale() + "");
	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {
		// Log.i(TAG, "onPoiSelected: ");
		// for (NPPoi poi : poiList) {
		// Log.i(TAG, poi + "\n");
		// }

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		super.onFinishLoadingFloor(mapView, mapInfo);
	}
}
