package cn.nephogram.locationengine;

import cn.nephogram.ibeacon.sdk.Beacon;

public class NPBeaconKey {
	private static final int CONSTANT_HUNDRED_THROUSAND = 100000;

	public static Integer beaconKeyForBeacon(Beacon beacon) {
		return beacon.getMajor() * CONSTANT_HUNDRED_THROUSAND
				+ beacon.getMinor();
	}

	public static Integer beaconKeyForNPBeacon(NPBeacon beacon) {
		return beacon.getMajor() * CONSTANT_HUNDRED_THROUSAND
				+ beacon.getMinor();
	}

}
