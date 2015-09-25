package com.ty.bleproject.app;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.ty.bleproject.activities.FileHelper;
import com.ty.locationengine.ibeacon.BeaconRegion;

public class TYRegionManager {
	static final String TAG = TYRegionManager.class.getSimpleName();

	static final String KEY_BEACON_REGION = "BeaconRegion";
	static final String KEY_CITY_ID = "cityID";
	static final String KEY_BUILDING_ID = "buildingID";
	static final String KEY_NAME = "name";
	static final String KEY_UUID = "uuid";
	static final String KEY_MAJOR = "major";

	Map<String, BeaconRegion> allBeaconRegionDictionary = new HashMap<String, BeaconRegion>();

	public TYRegionManager(Context context) {
		parseAllRegions(FileHelper.readStringFromAsset(context,
				"BeaconRegion.json"));
	}

	public BeaconRegion getBeaconRegion(String buildingID) {
		return allBeaconRegionDictionary.get(buildingID);
	}

	void parseAllRegions(String content) {
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.optJSONArray(KEY_BEACON_REGION);

			for (int i = 0; i < array.length(); ++i) {
				JSONObject json = array.optJSONObject(i);

				String buildingID = json.optString(KEY_BUILDING_ID);
				String uuid = json.optString(KEY_UUID);

				BeaconRegion region = null;
				if (json.isNull(KEY_MAJOR)) {
					region = new BeaconRegion(buildingID, uuid, null, null);
				} else {
					Integer major = json.optInt(KEY_MAJOR);
					region = new BeaconRegion(buildingID, uuid, major, null);
				}
				allBeaconRegionDictionary.put(buildingID, region);
			}
		} catch (Exception e) {

		}
	}
}
