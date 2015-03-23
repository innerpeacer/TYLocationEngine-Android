package cn.nephogram.ibeacon.sdk.utils;

import cn.nephogram.ibeacon.sdk.Beacon;

public class EstimoteBeacons {
	public static final String ESTIMOTE_PROXIMITY_UUID = "94171fa9-234c-4cf1-87da-4a9ba674561d";
	public static final String ESTIMOTE_MAC_PROXIMITY_UUID = "08D4A950-80F0-4D42-A14B-D53E063516E6";
	public static final String ESTIMOTE_IOS_PROXIMITY_UUID = "8492E75F-4FD6-469D-B132-043FE94921D8";

	public static boolean isIOSBeacon(Beacon beacon) {
		return ESTIMOTE_IOS_PROXIMITY_UUID.equalsIgnoreCase(beacon
				.getProximityUUID())
				|| ESTIMOTE_IOS_PROXIMITY_UUID
						.equals("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
	}

	public static boolean isMacBeacon(Beacon beacon) {
		return ESTIMOTE_MAC_PROXIMITY_UUID.equalsIgnoreCase(beacon
				.getProximityUUID());
	}

	public static boolean isOriginalEstimoteUuid(Beacon beacon) {
		return ESTIMOTE_PROXIMITY_UUID.equalsIgnoreCase(beacon
				.getProximityUUID());
	}

	public static boolean isValidName(String name) {
		return ("estimote".equalsIgnoreCase(name))
				|| ("est".equalsIgnoreCase(name));
	}

	public static boolean isEstimoteBeacon(Beacon beacon) {
		// return (isMacBeacon(beacon)) || (isIOSBeacon(beacon))
		// || (isOriginalEstimoteUuid(beacon))
		// || (isValidName(beacon.getName()));
		return true;
	}
}
