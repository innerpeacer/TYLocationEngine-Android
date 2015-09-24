package com.ty.locationengine.ibeacon;

public class IPTuYaBeacons {
	public static final String TUYA_PROXIMITY_UUID = "94171fa9-234c-4cf1-87da-4a9ba674561d";
	public static final String TUYA_MAC_PROXIMITY_UUID = "08D4A950-80F0-4D42-A14B-D53E063516E6";
	public static final String TUYA_IOS_PROXIMITY_UUID = "8492E75F-4FD6-469D-B132-043FE94921D8";

	public static boolean isIOSBeacon(Beacon beacon) {
		return TUYA_IOS_PROXIMITY_UUID.equalsIgnoreCase(beacon
				.getProximityUUID())
				|| TUYA_IOS_PROXIMITY_UUID
						.equals("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
	}

	public static boolean isMacBeacon(Beacon beacon) {
		return TUYA_MAC_PROXIMITY_UUID.equalsIgnoreCase(beacon
				.getProximityUUID());
	}

	public static boolean isOriginalEstimoteUuid(Beacon beacon) {
		return TUYA_PROXIMITY_UUID.equalsIgnoreCase(beacon.getProximityUUID());
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
