package cn.nephogram.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import cn.nephogram.app.DataManager;
import cn.nephogram.ble.R;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.ibeacon.sdk.BeaconManager;
import cn.nephogram.locationengine.NPLocationManager;
import cn.nephogram.locationengine.NPLocationManager.NPLocationManagerListener;

public class MainActivity extends Activity implements NPLocationManagerListener {
	static final String TAG = MainActivity.class.getSimpleName();

	static final int REQUEST_ENABLE_BT = 1234;

	// =====================================

	private TextView textView;
	private List<String> logList;

	BeaconManager beaconManager;
	NPLocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		textView = (TextView) findViewById(R.id.textView);

		logList = new ArrayList<String>();

		addToLog("onCreate...");

		beaconManager = new BeaconManager(this);

		locationManager = new NPLocationManager(this,
				DataManager.getBeaconDBPath());
		locationManager.setBeaconRegion(DataManager.getRegion());
		locationManager.addLocationEngineListener(this);
	}

	@Override
	public void didFailUpdateLocation(NPLocationManager locationManager) {
		addToLog("didFailUpdateLocation: " + System.currentTimeMillis()
				/ 1000.0f);
	}

	@Override
	public void didUpdateLocation(NPLocationManager locationManager,
			NPLocalPoint lp) {
		addToLog("didUpdateLocation: " + lp.toString());
	}

	void addToLog(String log) {

		if (logList.size() >= 25) {
			logList.remove(0);
		}
		logList.add(log);

		StringBuffer buffer = new StringBuffer();
		for (String s : logList) {
			buffer.append(s).append("\n");
		}
		textView.setText(buffer.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();

		// checkBluetooth();
		locationManager.startUpdateLocation();
	}

	void checkBluetooth() {
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			locationManager.startUpdateLocation();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.stopUpdateLocation();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				locationManager.startUpdateLocation();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	static {
		System.loadLibrary("BLELocationEngine");
	}
}
