package com.ty.bleproject.activities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LicenseManager {
	static Map<String, Map<String, String>> allLicenseDictionary = null;

	public static void loadContent(String content) {
		if (allLicenseDictionary == null) {
			parseAllLicenses(content);
		}
	}

	static Map<String, String> getLicenseForBuilding(String buildingID) {
		return allLicenseDictionary.get(buildingID);
	}

	static void parseAllLicenses(String content) {
		allLicenseDictionary = new HashMap<String, Map<String, String>>();
		try {
			JSONArray array = new JSONArray(content);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.optJSONObject(i);

				Map<String, String> dict = new HashMap<String, String>();
				dict.put("UserID", object.optString("UserID"));
				dict.put("BuildingID", object.optString("BuildingID"));
				dict.put("License", object.optString("License"));
				dict.put("Expiration Date", object.optString("Expiration Date"));

				allLicenseDictionary.put(object.optString("BuildingID"), dict);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
