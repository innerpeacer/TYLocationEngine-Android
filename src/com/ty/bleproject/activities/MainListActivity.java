package com.ty.bleproject.activities;

import java.io.File;
import java.util.Map;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ty.bleproject.R;
import com.ty.bleproject.settings.AppSettings;
import com.ty.mapsdk.TYMapEnvironment;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String mapRootDir = Environment.getExternalStorageDirectory()
				+ "/TuYaBLE/MapEncrypted";
		TYMapEnvironment.setRootDirectoryForMapFiles(mapRootDir);

		if (!new File(mapRootDir).exists()) {
			copyFileIfNeeded();
		}
		// copyFileIfNeeded();

		AppSettings settings = new AppSettings(this);

		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("00210100");
		settings.setDefaultBuildingID("00210004");

		setTitle(getResources().getString(R.string.app_name));

	};

	void copyFileIfNeeded() {
		String sourcePath = "MapEncrypted";
		String targetPath = TYMapEnvironment.getRootDirectoryForMapFiles();

		Log.i(TAG, "source path: " + sourcePath);
		Log.i(TAG, "target path: " + targetPath);

		FileHelper.deleteFile(new File(targetPath));
		FileHelper.copyFolderFromAsset(this, sourcePath, targetPath);
	}

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair(getResources().getString(
						R.string.map_activity_title), new Intent(this,
						MapActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_activity_location), new Intent(this,
						MapLocationActivity.class)),
				new IntentPair(getResources().getString(
						R.string.activity_location), new Intent(this,
						MainActivity.class)) };
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get("Intent");
		startActivity(intent);
	}

	static {
		System.loadLibrary("TYMapSDK");
	}

}