package com.ty.locationengine.ibeacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BeaconUtils {
	public static final String TAG = BeaconUtils.class.getSimpleName();
	public static final int MANUFACTURER_SPECIFIC_DATA = 255;

	public static Beacon beaconFromLeScan(BluetoothDevice device, int rssi,
			byte[] scanRecord) {
		String scanRecordAsHex = IPHashCode.fromBytes(scanRecord).toString();

		for (int i = 0; i < scanRecord.length; i++) {
			int payloadLength = unsignedByteToInt(scanRecord[i]);
			if ((payloadLength == 0) || (i + 1 >= scanRecord.length)) {
				break;
			}

			if (unsignedByteToInt(scanRecord[(i + 1)]) != 255) {
				i += payloadLength;
			} else {
				// if (payloadLength == 26) {
				if ((unsignedByteToInt(scanRecord[(i + 2)]) == 76)
						&& (unsignedByteToInt(scanRecord[(i + 3)]) == 0)
						&& (unsignedByteToInt(scanRecord[(i + 4)]) == 2)
						&& (unsignedByteToInt(scanRecord[(i + 5)]) == 21)) {
					String proximityUUID = String.format("%s-%s-%s-%s-%s",
							new Object[] { scanRecordAsHex.substring(18, 26),
									scanRecordAsHex.substring(26, 30),
									scanRecordAsHex.substring(30, 34),
									scanRecordAsHex.substring(34, 38),
									scanRecordAsHex.substring(38, 50) });

					int major = unsignedByteToInt(scanRecord[(i + 22)]) * 256
							+ unsignedByteToInt(scanRecord[(i + 23)]);
					int minor = unsignedByteToInt(scanRecord[(i + 24)]) * 256
							+ unsignedByteToInt(scanRecord[(i + 25)]);
					int measuredPower = scanRecord[(i + 26)];

					// Log.i(TAG, "Minor: " + minor + ": " + measuredPower);

					return new Beacon(proximityUUID, device.getName(),
							device.getAddress(), major, minor, measuredPower,
							rssi);
				}

				return null;
				// }

				// return null;
			}
		}

		return null;
	}

	public static String normalizeProximityUUID(String proximityUUID) {
		String withoutDashes = proximityUUID.replace("-", "").toLowerCase();

		IPPreconditions.checkArgument(withoutDashes.length() == 32,
				"Proximity UUID must be 32 characters without dashes");

		return String.format(
				"%s-%s-%s-%s-%s",
				new Object[] { withoutDashes.substring(0, 8),
						withoutDashes.substring(8, 12),
						withoutDashes.substring(12, 16),
						withoutDashes.substring(16, 20),
						withoutDashes.substring(20, 32) });
	}

	public static boolean isBeaconInRegion(Beacon beacon, BeaconRegion region) {
		return ((region.getProximityUUID() == null) || (beacon
				.getProximityUUID().equals(region.getProximityUUID())))
				&& ((region.getMajor() == null) || (beacon.getMajor() == region
						.getMajor().intValue()))
				&& ((region.getMinor() == null) || (beacon.getMinor() == region
						.getMinor().intValue()));
	}

	// public static double computeAccuracy(Beacon beacon) {
	// if (beacon.getRssi() == 0) {
	// return -1.0D;
	// }
	//
	// double ratio = beacon.getRssi() / beacon.getMeasuredPower();
	// double rssiCorrection = 0.96D + Math.pow(Math.abs(beacon.getRssi()),
	// 3.0D) % 10.0D / 150.0D;
	//
	// if (ratio <= 1.0D) {
	// return Math.pow(ratio, 9.98D) * rssiCorrection;
	// }
	// return (0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
	// }

	public static double computeAccuracy(Beacon beacon) {
		if (beacon.getRssi() == 0) {
			return -1.0D;
		}

		double ratio = beacon.getRssi() * 1.0 / beacon.getMeasuredPower();
		double rssiCorrection = 0.96D + Math.pow(Math.abs(beacon.getRssi()),
				3.0D) % 10.0D / 150.0D;

		if (ratio <= 1.0D) {
			return Math.pow(ratio, 9.98D) * rssiCorrection;
		}
		return (0.103D + 0.89978D * Math.pow(ratio, 7.71D)) * rssiCorrection;
	}

	// public static double computeAccuracy(Beacon beacon) {
	// if (beacon.getRssi() == 0) {
	// return -1.0D;
	// }
	//
	// return RssiToDistance.getDistanceFromRssi(beacon.getRssi());
	//
	// // if (beacon.getRssi() == 0) {
	// // return -1.0;
	// // }
	// //
	// // double ratio = beacon.getRssi() / beacon.getMeasuredPower();
	// // if (ratio < 1.0) {
	// // return Math.pow(ratio, 10);
	// // } else {
	// // double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
	// // return accuracy;
	// // }
	// }

	public static Proximity proximityFromAccuracy(double accuracy) {
		if (accuracy < 0.0D) {
			return Proximity.UNKNOWN;
		}
		if (accuracy < 0.5D) {
			return Proximity.IMMEDIATE;
		}
		if (accuracy <= 3.0D) {
			return Proximity.NEAR;
		}
		return Proximity.FAR;
	}

	public static Proximity computeProximity(Beacon beacon) {
		return proximityFromAccuracy(computeAccuracy(beacon));
	}

	public static int parseInt(String numberAsString) {
		try {
			return Integer.parseInt(numberAsString);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	public static int normalize16BitUnsignedInt(int value) {
		return Math.max(1, Math.min(value, 65535));
	}

	public static void restartBluetooth(Context context,
			final RestartCompletedListener listener) {
		BluetoothManager bluetoothManager = (BluetoothManager) context
				.getSystemService("bluetooth");
		final BluetoothAdapter adapter = bluetoothManager.getAdapter();

		IntentFilter intentFilter = new IntentFilter(
				"android.bluetooth.adapter.action.STATE_CHANGED");
		context.registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				if ("android.bluetooth.adapter.action.STATE_CHANGED"
						.equals(intent.getAction())) {
					int state = intent.getIntExtra(
							"android.bluetooth.adapter.extra.STATE", -1);
					if (state == 10) {
						adapter.enable();
					} else if (state == 12) {
						context.unregisterReceiver(this);
						listener.onRestartCompleted();
					}
				}
			}

		}, intentFilter);

		adapter.disable();
	}

	private static int unsignedByteToInt(byte value) {
		return value & 0xFF;
	}

	public static enum Proximity {
		UNKNOWN, IMMEDIATE, NEAR, FAR;
	}

	public static abstract interface RestartCompletedListener {
		public abstract void onRestartCompleted();
	}
}
