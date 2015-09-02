package com.ty.bleproject.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.esri.core.geometry.Point;
import com.ty.bleproject.R;
import com.ty.bleproject.settings.AppSettings;
import com.ty.mapsdk.TYBuilding;
import com.ty.mapsdk.TYMapEnvironment;
import com.ty.mapsdk.TYMapInfo;
import com.ty.mapsdk.TYMapView;
import com.ty.mapsdk.TYMapView.TYMapViewListenser;
import com.ty.mapsdk.TYPoi;
import com.ty.mapsdk.TYRenderingScheme;

public abstract class BaseMapViewActivity extends Activity implements
		TYMapViewListenser {
	static final String TAG = BaseMapViewActivity.class.getSimpleName();

	TYMapView mapView;
	ListView floorListView;

	TYBuilding currentBuilding;
	List<TYMapInfo> mapInfos;
	TYMapInfo currentMapInfo;

	int currentFloorIndex;

	int contentViewID;

	TYRenderingScheme renderingScheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TYMapEnvironment.initMapEnvironment();

		getCurrentBuilding();

		initContentViewID();
		setContentView(contentViewID);

		initMapLayout();
	}

	public abstract void initContentViewID();

	private void getCurrentBuilding() {
		AppSettings pref = new AppSettings(this);
		String cityID = pref.getDefaultCityID();
		String buildingID = pref.getDefaultBuildingID();

		currentBuilding = TYBuilding.parseBuildingFromFilesById(this, cityID,
				buildingID);
		mapInfos = TYMapInfo.parseMapInfoFromFiles(this, cityID, buildingID);
	}

	private void initMapLayout() {
		mapView = (TYMapView) findViewById(R.id.map);
		mapView.setEsriLogoVisible(false);

		floorListView = (ListView) findViewById(R.id.list_floor);

		if (mapInfos.size() == 0) {
			floorListView.setVisibility(View.GONE);
			return;
		} else if (mapInfos.size() == 1) {
			floorListView.setVisibility(View.GONE);

			currentMapInfo = mapInfos.get(0);
			currentFloorIndex = 0;
		} else if (mapInfos.size() > 1) {

			currentFloorIndex = 0;
			currentMapInfo = mapInfos.get(0);

			for (int i = 0; i < mapInfos.size(); i++) {
				TYMapInfo info = mapInfos.get(i);
				// if (info.getFloorName().equalsIgnoreCase("F1")) {
				if (info.getFloorNumber() == 7) {
					currentFloorIndex = i;
					currentMapInfo = info;
					break;
				}
				// }
			}

			FloorListAdatper fAdatper = new FloorListAdatper(this);
			fAdatper.setData(mapInfos);
			floorListView.setAdapter(fAdatper);

			floorListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == currentFloorIndex) {
						return;
					}

					changedToFloor(position);
				}
			});
		}

		setTitle(String.format("%s-%s", currentBuilding.getName(),
				currentMapInfo.getFloorName()));

		mapView.init(currentBuilding);
		mapView.setFloor(currentMapInfo);

		mapView.addMapListener(this);
	}

	protected void changedToFloor(int index) {
		currentFloorIndex = index;
		currentMapInfo = mapInfos.get(index);

		setTitle(String.format("%s-%s", currentBuilding.getName(),
				currentMapInfo.getFloorName()));
		mapView.setFloor(currentMapInfo);
	}

	@Override
	public void onClickAtPoint(TYMapView mapView, Point mappoint) {

	}

	@Override
	public void onPoiSelected(TYMapView mapView, List<TYPoi> poiList) {

	}

	@Override
	public void onFinishLoadingFloor(TYMapView mapView, TYMapInfo mapInfo) {
		// Log.i(TAG, "" + mapInfo);
	}

	@Override
	public void mapViewDidZoomed(TYMapView mapView) {

	}

	@Override
	protected void onResume() {
		super.onResume();

		mapView.unpause();
	}

	@Override
	protected void onPause() {
		super.onPause();

		mapView.pause();
	}
}