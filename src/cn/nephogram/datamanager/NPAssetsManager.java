package cn.nephogram.datamanager;

public class NPAssetsManager {

	private static String MAP_DIR = "MapFiles";

	private static String JSON_FILE_FLOOR = MAP_DIR + "/%s_FLOOR.json";
	private static String JSON_FILE_ROOM = MAP_DIR + "/%s_ROOM.json";
	private static String JSON_FILE_ASSET = MAP_DIR + "/%s_ASSET.json";
	private static String JSON_FILE_FACILITY = MAP_DIR + "/%s_FACILITY.json";
	private static String JSON_FILE_LABEL = MAP_DIR + "/%s_LABEL.json";

	private static String JSON_FILE_RENDERING_SCHEME = MAP_DIR
			+ "/RenderingScheme.json";

	private static String JSON_FILE_CITY = MAP_DIR + "/Cities.json";
	private static String JSON_FILE_BUILDING = MAP_DIR
			+ "/Buildings_City_%s.json";
	private static String JSON_FILE_MAPINFO = MAP_DIR
			+ "/MapInfo_Building_%s.json";

	public static String getFloorFilePath(String mapID) {
		return String.format(JSON_FILE_FLOOR, mapID);
	}

	public static String getRoomFilePath(String mapID) {
		return String.format(JSON_FILE_ROOM, mapID);
	}

	public static String getAssetFilePath(String mapID) {
		return String.format(JSON_FILE_ASSET, mapID);
	}

	public static String getFacilityFilePath(String mapID) {
		return String.format(JSON_FILE_FACILITY, mapID);
	}

	public static String getLabelFilePath(String mapID) {
		return String.format(JSON_FILE_LABEL, mapID);
	}

	public static String getRenderingSchemeFilePath() {
		return JSON_FILE_RENDERING_SCHEME;
	}

	public static String getCityJsonPath() {
		return JSON_FILE_CITY;
	}

	public static String getBuildingJsonPath(String cityID) {
		return String.format(JSON_FILE_BUILDING, cityID);
	}

	public static String getMapInfoJsonPath(String marketID) {
		return String.format(JSON_FILE_MAPINFO, marketID);
	}

}
