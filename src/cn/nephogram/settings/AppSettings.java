package cn.nephogram.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings {
	Context context;

	public AppSettings(Context c) {
		this.context = c;
	}

	public String getDefaultCityID() {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		String string = appPrefs.getString("cityID", null);
		return string;
	}

	public String getDefaultBuildingID() {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		String string = appPrefs.getString("buildingID", null);
		return string;
	}

	public boolean setDefaultBuildingID(String buildingID) {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		Editor editor = appPrefs.edit();
		editor.putString("buildingID", buildingID);
		return editor.commit();
	}

	public boolean setDefaultCityID(String cityID) {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		Editor editor = appPrefs.edit();
		editor.putString("cityID", cityID);
		return editor.commit();
	}
}
