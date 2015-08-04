package cn.nephogram.app;

import android.os.Environment;
import cn.nephogram.ibeacon.sdk.Region;

public class DataManager {
	static final String ROOT_DIR = Environment.getExternalStorageDirectory()
			+ "/NephogramTest/";
	static String MAP_DIR = ROOT_DIR + "MapFiles";

	static final String DEFAULT_BUILDING_ID_002100001 = "002100001";
	static final String DEFAULT_BUILDING_ID_002100002 = "002100002";
	static final String DEFAULT_BUILDING_ID_002100004 = "002100004";

	static final String BEACON_DB_FILE_002100001 = ROOT_DIR + "beacon-of.db";
	static final String BEACON_DB_FILE_002100002 = ROOT_DIR + "beacon-jz.db";
	static final String BEACON_DB_FILE_002100004 = ROOT_DIR + "beacon-yd.db";

	// Office
	static final String UUID_002100001 = "E2879308-4FA3-4F30-AC22-19ECDCB0D8C8";
	static final Region REGION_002100001 = new Region("Nephogram",
			UUID_002100001, 1, null);

	// JinZhong
	static final String UUID_002100002 = "A640BA53-68A6-4A9B-80AE-6F3E8E59D31F";
	static final Region REGION_002100002 = new Region("Nephogram",
			UUID_002100002, 1, null);

	// Yude
	static final String UUID_002100004 = "A640BA53-68A6-4A9B-80AE-6F3E8E59D31F";
	static final Region REGION_002100004 = new Region("Nephogram",
			UUID_002100004, 2, null);

	static final String UsedUUID = UUID_002100001;
	static final Region UsedRegion = REGION_002100001;
	static final String UsedBeaconPath = BEACON_DB_FILE_002100001;
	static final String UsedBuildingID = DEFAULT_BUILDING_ID_002100001;

	// static final String UsedUUID = UUID_002100002;
	// static final Region UsedRegion = REGION_002100002;
	// static final String UsedBeaconPath = BEACON_DB_FILE_002100002;
	// static final String UsedBuildingID = DEFAULT_BUILDING_ID_002100002;

	// static final String UsedUUID = UUID_002100004;
	// static final Region UsedRegion = REGION_002100004;
	// static final String UsedBeaconPath = BEACON_DB_FILE_002100004;
	// static final String UsedBuildingID = DEFAULT_BUILDING_ID_002100004;

	public static String getUUID() {
		return UsedUUID;
	}

	public static Region getRegion() {
		return UsedRegion;
	}

	public static String getBeaconDBPath() {
		return UsedBeaconPath;
	}

	public static String getBuildingID() {
		return UsedBuildingID;
	}
}
