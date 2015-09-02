package com.ty.locationengine.ibeacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

public class BeaconService extends Service {

	private static final double TIME_WINDOW = 10.0f;

	// private static final String TAG = (BeaconService.class.getName());
	private static final String TAG = (BeaconService.class.getSimpleName());
	public static final int MSG_START_RANGING = 1;
	public static final int MSG_STOP_RANGING = 2;
	public static final int MSG_RANGING_RESPONSE = 3;
	public static final int MSG_START_MONITORING = 4;
	public static final int MSG_STOP_MONITORING = 5;
	public static final int MSG_MONITORING_RESPONSE = 6;
	public static final int MSG_REGISTER_ERROR_LISTENER = 7;
	public static final int MSG_ERROR_RESPONSE = 8;
	public static final int MSG_SET_FOREGROUND_SCAN_PERIOD = 9;
	public static final int MSG_SET_BACKGROUND_SCAN_PERIOD = 10;

	public static final int ERROR_COULD_NOT_START_LOW_ENERGY_SCANNING = -1;
	static final long EXPIRATION_MILLIS = TimeUnit.SECONDS.toMillis(10L);

	public static final String SCAN_START_ACTION_NAME = "startScan";
	public static final String AFTER_SCAN_ACTION_NAME = "afterScan";
	private static final Intent SCAN_START_INTENT = new Intent("startScan");
	private static final Intent AFTER_SCAN_INTENT = new Intent("afterScan");

	private final Messenger messenger;
	private final BluetoothAdapter.LeScanCallback leScanCallback;
	private final ConcurrentHashMap<Beacon, Long> beaconsFoundInScanCycle;
	private final ConcurrentHashMap<Beacon, IPRssiMovingAverageTD> beaconAverageRssi;

	private final List<IPRangingRegion> rangedRegions;
	private final List<IPMonitoringRegion> monitoredRegions;
	private BluetoothAdapter adapter;
	private AlarmManager alarmManager;
	private HandlerThread handlerThread;
	private Handler handler;
	private Runnable afterScanCycleTask;
	private boolean scanning;
	private Messenger errorReplyTo;
	private BroadcastReceiver bluetoothBroadcastReceiver;
	private BroadcastReceiver scanStartBroadcastReceiver;
	private PendingIntent scanStartBroadcastPendingIntent;
	private BroadcastReceiver afterScanBroadcastReceiver;
	private PendingIntent afterScanBroadcastPendingIntent;
	private IPScanPeriodData foregroundScanPeriod;
	private IPScanPeriodData backgroundScanPeriod;

	public BeaconService() {
		this.handlerThread = new HandlerThread("BeaconServiceThread", 10);
		this.handlerThread.start();

		IncomingHandler mIncomingHandler = new IncomingHandler(
				this.handlerThread.getLooper());
		this.messenger = new Messenger(mIncomingHandler);

		this.leScanCallback = new InternalLeScanCallback();

		this.beaconsFoundInScanCycle = new ConcurrentHashMap<Beacon, Long>();
		this.beaconAverageRssi = new ConcurrentHashMap<Beacon, IPRssiMovingAverageTD>();

		this.rangedRegions = new ArrayList<IPRangingRegion>();

		this.monitoredRegions = new ArrayList<IPMonitoringRegion>();

		this.foregroundScanPeriod = new IPScanPeriodData(
				TimeUnit.SECONDS.toMillis(1L), TimeUnit.SECONDS.toMillis(0L));

		this.backgroundScanPeriod = new IPScanPeriodData(
				TimeUnit.SECONDS.toMillis(5L), TimeUnit.SECONDS.toMillis(30L));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service Started");

		return super.onStartCommand(intent, flags, startId);
	}

	public void onCreate() {
		super.onCreate();
		// L.i("Creating service");
		Log.i(TAG, "Service Created");

		this.alarmManager = ((AlarmManager) getSystemService("alarm"));
		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
		this.adapter = bluetoothManager.getAdapter();
		this.afterScanCycleTask = new AfterScanCycleTask();

		// this.handlerThread = new HandlerThread("BeaconServiceThread", 10);
		// this.handlerThread.start();
		this.handler = new Handler(this.handlerThread.getLooper());

		this.bluetoothBroadcastReceiver = createBluetoothBroadcastReceiver();
		this.scanStartBroadcastReceiver = createScanStartBroadcastReceiver();
		this.afterScanBroadcastReceiver = createAfterScanBroadcastReceiver();
		registerReceiver(this.bluetoothBroadcastReceiver, new IntentFilter(
				"android.bluetooth.adapter.action.STATE_CHANGED"));
		registerReceiver(this.scanStartBroadcastReceiver, new IntentFilter(
				"startScan"));
		registerReceiver(this.afterScanBroadcastReceiver, new IntentFilter(
				"afterScan"));
		this.afterScanBroadcastPendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, AFTER_SCAN_INTENT, 0);
		this.scanStartBroadcastPendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, SCAN_START_INTENT, 0);
	}

	public void onDestroy() {
		// L.i("Service destroyed");
		Log.i(TAG, "Service Destroyed");
		unregisterReceiver(this.bluetoothBroadcastReceiver);
		unregisterReceiver(this.scanStartBroadcastReceiver);
		unregisterReceiver(this.afterScanBroadcastReceiver);

		if (this.adapter != null) {
			stopScanning();
		}

		removeAfterScanCycleCallback();
		this.handlerThread.quit();

		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return this.messenger.getBinder();
	}

	private void startRanging(IPRangingRegion rangingRegion) {
		checkNotOnUiThread();
		Log.v(TAG, "Start ranging: " + rangingRegion.region);
		IPPreconditions.checkNotNull(this.adapter,
				"Bluetooth adapter cannot be null");
		this.rangedRegions.add(rangingRegion);
		startScanning();
	}

	private void stopRanging(String regionId) {
		Log.v(TAG, "Stopping ranging: " + regionId);
		checkNotOnUiThread();
		Iterator<IPRangingRegion> iterator = this.rangedRegions.iterator();
		while (iterator.hasNext()) {
			IPRangingRegion rangingRegion = (IPRangingRegion) iterator.next();
			if (regionId.equals(rangingRegion.region.getIdentifier())) {
				iterator.remove();
			}
		}
		if ((this.rangedRegions.isEmpty()) && (this.monitoredRegions.isEmpty())) {
			removeAfterScanCycleCallback();
			stopScanning();
			this.beaconsFoundInScanCycle.clear();
			this.beaconAverageRssi.clear();
		}
	}

	public void startMonitoring(IPMonitoringRegion monitoringRegion) {
		checkNotOnUiThread();
		Log.v(TAG, "Starting monitoring: " + monitoringRegion.region);
		IPPreconditions.checkNotNull(this.adapter,
				"Bluetooth adapter cannot be null");
		this.monitoredRegions.add(monitoringRegion);
		startScanning();
	}

	public void stopMonitoring(String regionId) {
		Log.v(TAG, "Stopping monitoring: " + regionId);
		checkNotOnUiThread();
		Iterator<IPMonitoringRegion> iterator = this.monitoredRegions.iterator();
		while (iterator.hasNext()) {
			IPMonitoringRegion monitoringRegion = (IPMonitoringRegion) iterator
					.next();
			if (regionId.equals(monitoringRegion.region.getIdentifier())) {
				iterator.remove();
			}
		}
		if ((this.monitoredRegions.isEmpty()) && (this.rangedRegions.isEmpty())) {
			removeAfterScanCycleCallback();
			stopScanning();
			this.beaconsFoundInScanCycle.clear();
			this.beaconAverageRssi.clear();
		}
	}

	private void startScanning() {
		if (this.scanning) {
			Log.d(TAG, "Scanning already in progress, not starting one more");
			return;
		}
		if ((this.monitoredRegions.isEmpty()) && (this.rangedRegions.isEmpty())) {
			Log.d(TAG, "Not starting scanning, no monitored on ranged regions");
			return;
		}
		if (!this.adapter.isEnabled()) {
			Log.d(TAG, "Bluetooth is disabled, not starting scanning");
			return;
		}
		if (!this.adapter.startLeScan(this.leScanCallback)) {
			Log.wtf(TAG, "Bluetooth adapter did not start le scan");
			sendError(Integer.valueOf(-1));
			return;
		}
		this.scanning = true;
		removeAfterScanCycleCallback();
		setAlarm(this.afterScanBroadcastPendingIntent, scanPeriodTimeMillis());
	}

	private void stopScanning() {
		try {
			this.scanning = false;
			this.adapter.stopLeScan(this.leScanCallback);
		} catch (Exception e) {
			Log.wtf(TAG, "BluetoothAdapter throws unexpected exception", e);
		}
	}

	private void sendError(Integer errorId) {
		if (this.errorReplyTo != null) {
			Message errorMsg = Message.obtain(null, 8);
			errorMsg.obj = errorId;
			try {
				this.errorReplyTo.send(errorMsg);
			} catch (RemoteException e) {
				Log.e(TAG, "Error while reporting message, funny right?", e);
			}
		}
	}

	private long scanPeriodTimeMillis() {
		if (!this.rangedRegions.isEmpty()) {
			return this.foregroundScanPeriod.scanPeriodMillis;
		}
		return this.backgroundScanPeriod.scanPeriodMillis;
	}

	private long scanWaitTimeMillis() {
		if (!this.rangedRegions.isEmpty()) {
			return this.foregroundScanPeriod.waitTimeMillis;
		}
		return this.backgroundScanPeriod.waitTimeMillis;
	}

	private void setAlarm(PendingIntent pendingIntent, long delayMillis) {
		this.alarmManager.set(2, SystemClock.elapsedRealtime() + delayMillis,
				pendingIntent);
	}

	private void checkNotOnUiThread() {
		// Preconditions
		// .checkArgument(Looper.getMainLooper().getThread() != Thread
		// .currentThread(),
		// "This cannot be run on UI thread, starting BLE scan can be expensive");

		IPPreconditions.checkNotNull(Boolean.valueOf(this.handlerThread
				.getLooper() == Looper.myLooper()),
				"It must be executed on service's handlerThread");
	}

	private BroadcastReceiver createBluetoothBroadcastReceiver() {
		return new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				if ("android.bluetooth.adapter.action.STATE_CHANGED"
						.equals(intent.getAction())) {
					int state = intent.getIntExtra(
							"android.bluetooth.adapter.extra.STATE", -1);
					if (state == 10)
						BeaconService.this.handler.post(new Runnable() {
							public void run() {
								Log.i(TAG,
										"Bluetooth is OFF: stopping scanning");
								BeaconService.this
										.removeAfterScanCycleCallback();
								BeaconService.this.stopScanning();
								BeaconService.this.beaconsFoundInScanCycle
										.clear();
								BeaconService.this.beaconAverageRssi.clear();
							}

						});
					else if (state == 12)
						BeaconService.this.handler.post(new Runnable() {
							public void run() {
								if ((!BeaconService.this.monitoredRegions
										.isEmpty())
										|| (!BeaconService.this.rangedRegions
												.isEmpty())) {
									Log.i(TAG,
											String.format(
													"Bluetooth is ON: resuming scanning (monitoring: %d ranging:%d)",
													new Object[] {
															Integer.valueOf(BeaconService.this.monitoredRegions
																	.size()),
															Integer.valueOf(BeaconService.this.rangedRegions
																	.size()) }));

									BeaconService.this.startScanning();
								}
							}

						});
				}
			}

		};
	}

	private void removeAfterScanCycleCallback() {
		this.handler.removeCallbacks(this.afterScanCycleTask);
		this.alarmManager.cancel(this.afterScanBroadcastPendingIntent);
		this.alarmManager.cancel(this.scanStartBroadcastPendingIntent);
	}

	private BroadcastReceiver createAfterScanBroadcastReceiver() {
		return new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				BeaconService.this.handler
						.post(BeaconService.this.afterScanCycleTask);
			}

		};
	}

	private BroadcastReceiver createScanStartBroadcastReceiver() {
		return new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				BeaconService.this.handler.post(new Runnable() {
					public void run() {
						BeaconService.this.startScanning();
					}
				});
			}
		};
	}

	private class InternalLeScanCallback implements
			BluetoothAdapter.LeScanCallback {
		private InternalLeScanCallback() {
		}

		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			BeaconService.this.checkNotOnUiThread();
			Beacon beacon = BeaconUtils.beaconFromLeScan(device, rssi, scanRecord);

			if ((beacon == null) || (!IPTuYaBeacons.isEstimoteBeacon(beacon))) {
				Log.d(TAG, "Device " + device + " is not an Estimote beacon");
				return;
			}

			if (!BeaconService.this.beaconAverageRssi.keySet().contains(beacon)) {
				BeaconService.this.beaconAverageRssi.put(beacon,
						new IPRssiMovingAverageTD(TIME_WINDOW));
				// Log.i(TAG, "Add New: " + beacon.getMacAddress());
			} else {
				// Log.i(TAG, "Already Contain");
			}
			BeaconService.this.beaconAverageRssi.get(beacon).push(
					System.currentTimeMillis(), rssi);

			BeaconService.this.beaconsFoundInScanCycle.put(beacon,
					Long.valueOf(System.currentTimeMillis()));

		}

	}

	private class IncomingHandler extends Handler {
		// private IncomingHandler() {
		// }
		public IncomingHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			final int what = msg.what;
			final Object obj = msg.obj;
			final Messenger replyTo = msg.replyTo;
			BeaconService.this.handler.post(new Runnable() {
				public void run() {
					switch (what) {
					case 1:
						IPRangingRegion rangingRegion = new IPRangingRegion(
								(BeaconRegion) obj, replyTo);
						BeaconService.this.startRanging(rangingRegion);
						break;
					case 2:
						String rangingRegionId = (String) obj;
						BeaconService.this.stopRanging(rangingRegionId);
						break;
					case 4:
						IPMonitoringRegion monitoringRegion = new IPMonitoringRegion(
								(BeaconRegion) obj, replyTo);
						BeaconService.this.startMonitoring(monitoringRegion);
						break;
					case 5:
						String monitoredRegionId = (String) obj;
						BeaconService.this.stopMonitoring(monitoredRegionId);
						break;
					case 7:
						BeaconService.this.errorReplyTo = replyTo;
						break;
					case 9:
						Log.d(TAG, "Setting foreground scan period: "
								+ BeaconService.this.foregroundScanPeriod);
						BeaconService.this.foregroundScanPeriod = ((IPScanPeriodData) obj);
						break;
					case 10:
						Log.d(TAG, "Setting background scan period: "
								+ BeaconService.this.backgroundScanPeriod);
						BeaconService.this.backgroundScanPeriod = ((IPScanPeriodData) obj);
						break;
					case 3:

					case 6:

					case 8:

					default:
						Log.d(TAG, "Unknown message: what=" + what + " obj="
								+ obj);
					}
				}

			});
		}

	}

	private class AfterScanCycleTask implements Runnable {
		private AfterScanCycleTask() {
		}

		private void processRanging() {
			for (IPRangingRegion rangedRegion : BeaconService.this.rangedRegions)
				rangedRegion.processFoundBeacons(
						BeaconService.this.beaconsFoundInScanCycle,
						BeaconService.this.beaconAverageRssi);
		}

		private List<IPMonitoringRegion> findEnteredRegions(long currentTimeMillis) {
			List<IPMonitoringRegion> didEnterRegions = new ArrayList<IPMonitoringRegion>();
			for (Entry<Beacon, Long> entry : BeaconService.this.beaconsFoundInScanCycle
					.entrySet()) {
				for (IPMonitoringRegion monitoringRegion : matchingMonitoredRegions((Beacon) entry
						.getKey())) {
					monitoringRegion.processFoundBeacons(
							BeaconService.this.beaconsFoundInScanCycle,
							BeaconService.this.beaconAverageRssi);
					if (monitoringRegion.markAsSeen(currentTimeMillis)) {
						didEnterRegions.add(monitoringRegion);
					}
				}
			}
			return didEnterRegions;
		}

		private List<IPMonitoringRegion> matchingMonitoredRegions(Beacon beacon) {
			List<IPMonitoringRegion> results = new ArrayList<IPMonitoringRegion>();
			for (IPMonitoringRegion monitoredRegion : BeaconService.this.monitoredRegions) {
				if (BeaconUtils.isBeaconInRegion(beacon, monitoredRegion.region)) {
					results.add(monitoredRegion);
				}
			}
			return results;
		}

		private void removeNotSeenBeacons(long currentTimeMillis) {
			for (IPRangingRegion rangedRegion : BeaconService.this.rangedRegions) {
				rangedRegion.removeNotSeenBeacons(currentTimeMillis);
			}
			for (IPMonitoringRegion monitoredRegion : BeaconService.this.monitoredRegions)
				monitoredRegion.removeNotSeenBeacons(currentTimeMillis);
		}

		private List<IPMonitoringRegion> findExitedRegions(long currentTimeMillis) {
			List<IPMonitoringRegion> didExitMonitors = new ArrayList<IPMonitoringRegion>();
			for (IPMonitoringRegion monitoredRegion : BeaconService.this.monitoredRegions) {
				if (monitoredRegion.didJustExit(currentTimeMillis)) {
					didExitMonitors.add(monitoredRegion);
				}
			}
			return didExitMonitors;
		}

		private void invokeCallbacks(List<IPMonitoringRegion> enteredMonitors,
				List<IPMonitoringRegion> exitedMonitors) {
			for (IPRangingRegion rangingRegion : BeaconService.this.rangedRegions) {
				try {
					Message rangingResponseMsg = Message.obtain(null, 3);
					rangingResponseMsg.obj = new IPRangingResult(
							rangingRegion.region,
							rangingRegion.getSortedBeacons());
					rangingRegion.replyTo.send(rangingResponseMsg);
				} catch (RemoteException e) {
					Log.e(TAG, "Error while delivering responses", e);
				}
			}
			for (IPMonitoringRegion didEnterMonitor : enteredMonitors) {
				Message monitoringResponseMsg = Message.obtain(null, 6);
				monitoringResponseMsg.obj = new IPMonitoringResult(
						didEnterMonitor.region, BeaconRegion.State.INSIDE,
						didEnterMonitor.getSortedBeacons());
				try {
					didEnterMonitor.replyTo.send(monitoringResponseMsg);
				} catch (RemoteException e) {
					Log.e(TAG, "Error while delivering responses", e);
				}
			}
			for (IPMonitoringRegion didEnterMonitor : exitedMonitors) {
				Message monitoringResponseMsg = Message.obtain(null, 6);
				monitoringResponseMsg.obj = new IPMonitoringResult(
						didEnterMonitor.region, BeaconRegion.State.OUTSIDE,
						Collections.<Beacon> emptyList());

				try {
					didEnterMonitor.replyTo.send(monitoringResponseMsg);
				} catch (RemoteException e) {
					Log.e(TAG, "Error while delivering responses", e);
				}
			}
		}

		public void run() {
			BeaconService.this.checkNotOnUiThread();
			long now = System.currentTimeMillis();
			BeaconService.this.stopScanning();
			processRanging();
			List<IPMonitoringRegion> enteredRegions = findEnteredRegions(now);
			List<IPMonitoringRegion> exitedRegions = findExitedRegions(now);
			removeNotSeenBeacons(now);
			BeaconService.this.beaconsFoundInScanCycle.clear();
			// BeaconService.this.beaconAverageRssi.clear();
			invokeCallbacks(enteredRegions, exitedRegions);
			if (BeaconService.this.scanWaitTimeMillis() == 0L)
				BeaconService.this.startScanning();
			else
				BeaconService.this.setAlarm(
						BeaconService.this.scanStartBroadcastPendingIntent,
						BeaconService.this.scanWaitTimeMillis());
		}

	}

}
