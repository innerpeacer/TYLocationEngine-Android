package cn.nephogram.datamanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;

public class NPFileManager {
	public static final String ROOT_DIR = Environment
			.getExternalStorageDirectory() + "/Nephogram/";

	private static String MAP_DIR = ROOT_DIR + "MapFiles";

	private static String JSON_FILE_FLOOR = MAP_DIR + "/%s_FLOOR.json";
	private static String JSON_FILE_ROOM = MAP_DIR + "/%s_ROOM.json";
	private static String JSON_FILE_ASSET = MAP_DIR + "/%s_ASSET.json";
	private static String JSON_FILE_FACILITY = MAP_DIR + "/%s_FACILITY.json";
	private static String JSON_FILE_LABEL = MAP_DIR + "/%s_LABEL.json";

	private static String JSON_FILE_RENDERING_SCHEME = MAP_DIR
			+ "/RenderingScheme.json";

	private static String JSON_FILE_CITY = MAP_DIR + "/Cities.json";
	private static String JSON_FILE_MARKET = MAP_DIR
			+ "/Buildings_City_%s.json";
	private static String JSON_FILE_MAPINFO = MAP_DIR
			+ "/MapInfo_Building_%s.json";

	public static boolean fileExist(String path) {
		return new File(path).exists() ? true : false;
	}

	public static File getFileFromFilePath(String filename) {
		File file = new File(filename);
		if (!file.isAbsolute()) {
			File root = Environment.getExternalStorageDirectory();
			file = new File(root, filename);
		}
		return file;
	}

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

	public static String getMarketJsonPath(String cityID) {
		return String.format(JSON_FILE_MARKET, cityID);
	}

	public static String getMapInfoJsonPath(String marketID) {
		return String.format(JSON_FILE_MAPINFO, marketID);
	}

	public static byte[] readByteFromFile(String path) {
		RandomAccessFile input;
		try {
			input = new RandomAccessFile(path, "rw");
			long filelength = input.length();
			byte output[] = new byte[(int) filelength];
			byte buffer[] = new byte[1024];

			int c;
			int offset = 0;
			while ((c = input.read(buffer)) != -1) {
				System.arraycopy(buffer, 0, output, offset, c);
				offset += c;
			}

			input.close();
			return output;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
