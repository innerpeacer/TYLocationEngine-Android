package com.ty.bleproject.app;

import com.ty.locationengine.ibeacon.BeaconRegion;

public class DataManager {

	// Office
	static final String UUID_002100100 = "FDA50693-A4E2-4FB1-AFCF-C6EB07647825";
	static final BeaconRegion REGION_00210100 = new BeaconRegion("TuYa",
			UUID_002100100, 10011, null);
	static final String UsedUUID = UUID_002100100;
	static final BeaconRegion UsedRegion = REGION_00210100;

	// // 四行仓库
	// static final String UUID_00210004 =
	// "058285BF-8FC5-46FC-81AB-915E9508B0D7";
	// static final BeaconRegion REGION_00210004 = new BeaconRegion("TuYa",
	// UUID_00210004, 1, null);
	// static final String UsedUUID = UUID_00210004;
	// static final BeaconRegion UsedRegion = REGION_00210004;
	//
	// public static String getUUID() {
	// return UsedUUID;
	// }

	public static BeaconRegion getRegion() {
		return UsedRegion;
	}

}
