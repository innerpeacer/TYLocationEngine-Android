package cn.nephogram.activities;

import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import cn.nephogram.ble.R;
import cn.nephogram.mapsdk.NPMapView;
import cn.nephogram.settings.AppSettings;

public class MainListActivity extends HelperListActivity {
	static final String TAG = MainListActivity.class.getSimpleName();

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppSettings settings = new AppSettings(this);

		settings.setDefaultCityID("0021");
		settings.setDefaultBuildingID("002100001");

		setTitle(getResources().getString(R.string.app_name));

		// NPMapView.useAsset = true;
		NPMapView.useAsset = false;

	};

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair(getResources().getString(
						R.string.map_activity_title), new Intent(this,
						NephogramMapActivity.class)),
				new IntentPair(getResources().getString(
						R.string.map_activity_location), new Intent(this,
						NephogramLocationActivity.class)),
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

}