package com.ty.locationengine.ble;

import com.ty.locationengine.ibeacon.Beacon;

public class IPBeaconKey {
	private static final int CONSTANT_HUNDRED_THROUSAND = 100000;

	public static Integer beaconKeyForBeacon(Beacon beacon) {
		return beacon.getMajor() * CONSTANT_HUNDRED_THROUSAND
				+ beacon.getMinor();
	}

	public static Integer beaconKeyForNPBeacon(TYBeacon beacon) {
		return beacon.getMajor() * CONSTANT_HUNDRED_THROUSAND
				+ beacon.getMinor();
	}

}
