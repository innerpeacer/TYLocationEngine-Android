package cn.nephogram.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.nephogram.app.DataManager;
import cn.nephogram.ble.R;
import cn.nephogram.datamanager.NPAssetsManager;
import cn.nephogram.mapsdk.NPMapEnvironment;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.mapsdk.NPMapView.NPMapViewListenser;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.data.NPBuilding;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.poi.NPPoi;

import com.esri.core.geometry.Point;

public abstract class BaseMapViewActivity extends Activity implements
		NPMapViewListenser {
	static final String TAG = BaseMapViewActivity.class.getSimpleName();

	NPMapView mapView;
	ListView floorListView;

	NPBuilding currentBuilding;
	List<NPMapInfo> mapInfos;
	NPMapInfo currentMapInfo;

	int currentFloorIndex;

	int contentViewID;

	NPRenderingScheme renderingScheme;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NPMapEnvironment.initMapEnvironment();

		getCurrentBuilding();

		initContentViewID();
		setContentView(contentViewID);

		initMapLayout();
	}

	public abstract void initContentViewID();

	private void getCurrentBuilding() {
		// AppSettings pref = new AppSettings(this);
		// String cityID = pref.getDefaultCityID();
		// String buildingID = pref.getDefaultBuildingID();

		String cityID = "0021";

		currentBuilding = NPBuilding.parseBuildingFromAssetsById(this,
				NPAssetsManager.getBuildingJsonPath(cityID), cityID,
				DataManager.getBuildingID());
		mapInfos = NPMapInfo
				.parseMapInfoFromAssets(this, NPAssetsManager
						.getMapInfoJsonPath(DataManager.getBuildingID()),
						DataManager.getBuildingID());

	}

	private void initMapLayout() {
		mapView = (NPMapView) findViewById(R.id.map);
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
				NPMapInfo info = mapInfos.get(i);
				if (info.getFloorName().equalsIgnoreCase("F1")) {
					currentFloorIndex = i;
					currentMapInfo = info;
					break;
				}
			}

			CAFloorListAdatper fAdatper = new CAFloorListAdatper(this);
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

		renderingScheme = new NPRenderingScheme(this,
				NPAssetsManager.getRenderingSchemeFilePath(), true);
		mapView.init(renderingScheme);
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
	public void onClickAtPoint(NPMapView mapView, Point mappoint) {

	}

	@Override
	public void onPoiSelected(NPMapView mapView, List<NPPoi> poiList) {

	}

	@Override
	public void onFinishLoadingFloor(NPMapView mapView, NPMapInfo mapInfo) {
		// Log.i(TAG, "" + mapInfo);
	}
}
