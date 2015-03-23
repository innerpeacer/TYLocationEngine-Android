package cn.nephogram.mapsdk.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 建筑类
 */
public class NPBuilding implements Parcelable {
	public static final String JSON_KEY_BUILDINGS = "Buildings";

	private static final String KEY_BUILDING_ID = "id";
	private static final String KEY_BUILDING_NAME = "name";
	private static final String KEY_BUILDING_ADDRESS = "address";
	private static final String KEY_BUILDING_LONGITUDE = "longitude";
	private static final String KEY_BUILDING_LATITUDE = "latitude";
	private static final String KEY_BUILDING_STATUS = "status";

	private String buildingID;
	private String name;
	private String address;

	private double longitude;
	private double latitude;

	private int status;

	public NPBuilding() {
		super();
	}

	NPBuilding(Parcel in) {
		buildingID = in.readString();
		name = in.readString();
		address = in.readString();

		longitude = in.readDouble();
		latitude = in.readDouble();

		status = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(buildingID);
		dest.writeString(name);
		dest.writeString(address);

		dest.writeDouble(longitude);
		dest.writeDouble(latitude);

		dest.writeInt(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof NPBuilding) {
			if (this.buildingID.equals(((NPBuilding) obj).getBuildingID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<NPBuilding> CREATOR = new Creator<NPBuilding>() {
		@Override
		public NPBuilding[] newArray(int size) {
			return new NPBuilding[size];
		}

		@Override
		public NPBuilding createFromParcel(Parcel source) {
			return new NPBuilding(source);
		}
	};

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_BUILDING_ID)) {
				setBuildingID(jsonObject.optString(KEY_BUILDING_ID));
			}
			if (!jsonObject.isNull(KEY_BUILDING_NAME)) {
				setName(jsonObject.optString(KEY_BUILDING_NAME));
			}
			if (!jsonObject.isNull(KEY_BUILDING_ADDRESS)) {
				setAddress(jsonObject.optString(KEY_BUILDING_ADDRESS));
			}
			if (!jsonObject.isNull(KEY_BUILDING_LONGITUDE)) {
				setLongitude(jsonObject.optDouble(KEY_BUILDING_LONGITUDE));
			}
			if (!jsonObject.isNull(KEY_BUILDING_LATITUDE)) {
				setLatitude(jsonObject.optDouble(KEY_BUILDING_LATITUDE));
			}
			if (!jsonObject.isNull(KEY_BUILDING_STATUS)) {
				setStatus(jsonObject.optInt(KEY_BUILDING_STATUS));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_BUILDING_ID, buildingID);
			jsonObject.put(KEY_BUILDING_NAME, name);
			jsonObject.put(KEY_BUILDING_ADDRESS, address);
			jsonObject.put(KEY_BUILDING_LONGITUDE, longitude);
			jsonObject.put(KEY_BUILDING_LATITUDE, latitude);
			jsonObject.put(KEY_BUILDING_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	protected void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setAddress(String address) {
		this.address = address;
	}

	protected void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	protected void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	protected void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 建筑名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 建筑地址
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 建筑经度
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 建筑纬度
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 建筑状态
	 */
	public int getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "MarektID = " + buildingID + ", MarketName = " + name;
	}

	/**
	 * 从外部存储目录解析所有建筑信息列表
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * 
	 * @return 建筑类数组
	 */
	public static List<NPBuilding> parseBuildingFromFiles(Context context,
			String path, String cityID) {

		List<NPBuilding> buildings = new ArrayList<NPBuilding>();

		try {
			FileInputStream inStream = new FileInputStream(new File(path));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(NPBuilding.JSON_KEY_BUILDINGS)) {
				JSONArray array = jsonObject
						.getJSONArray(NPBuilding.JSON_KEY_BUILDINGS);
				for (int i = 0; i < array.length(); i++) {
					NPBuilding building = new NPBuilding();
					building.parseJson(array.optJSONObject(i));
					buildings.add(building);
				}
			}
			inputReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buildings;
	}

	/**
	 * 从assets目录解析所有建筑信息列表
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * 
	 * @return 建筑类数组
	 */
	public static List<NPBuilding> parseBuildingFromAssets(Context context,
			String path, String cityID) {

		List<NPBuilding> buildings = new ArrayList<NPBuilding>();

		try {
			InputStream inStream = context.getAssets().open(path);
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(NPBuilding.JSON_KEY_BUILDINGS)) {
				JSONArray array = jsonObject
						.getJSONArray(NPBuilding.JSON_KEY_BUILDINGS);
				for (int i = 0; i < array.length(); i++) {
					NPBuilding building = new NPBuilding();
					building.parseJson(array.optJSONObject(i));
					buildings.add(building);
				}
			}
			inputReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return buildings;
	}

	/**
	 * 从外部存储目录按ID解析特定建筑信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingID
	 *            建筑ID
	 * @return 建筑类
	 */
	public static NPBuilding parseFromFilesById(Context context, String path,
			String cityID, String buildingID) {
		List<NPBuilding> buildings = parseBuildingFromFiles(context, path,
				cityID);

		for (int i = 0; i < buildings.size(); i++) {
			NPBuilding building = buildings.get(i);
			if (building.getBuildingID().equals(buildingID)) {
				return building;
			}
		}
		return null;
	}

	/**
	 * 从assets目录按ID解析特定建筑信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingID
	 *            建筑ID
	 * @return 建筑类
	 */
	public static NPBuilding parseBuildingFromAssetsById(Context context,
			String path, String cityID, String buildingID) {
		List<NPBuilding> buildings = parseBuildingFromAssets(context, path,
				cityID);

		for (int i = 0; i < buildings.size(); i++) {
			NPBuilding building = buildings.get(i);
			if (building.getBuildingID().equals(buildingID)) {
				return building;
			}
		}
		return null;
	}

	/**
	 * 从外部存储目录按名称解析特定建筑信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingName
	 *            建筑名称
	 * 
	 * @return 建筑类
	 */
	public static NPBuilding parseBuildingFromFilesByName(Context context,
			String path, String cityID, String buildingName) {
		List<NPBuilding> buildings = parseBuildingFromFiles(context, path,
				cityID);

		for (int i = 0; i < buildings.size(); i++) {
			NPBuilding building = buildings.get(i);
			if (building.getName().equals(buildingName)) {
				return building;
			}
		}
		return null;
	}

	/**
	 * 从assets目录按名称解析特定建筑信息
	 * 
	 * @param context
	 *            Context
	 * @param path
	 *            文件路径
	 * @param cityID
	 *            城市ID
	 * @param buildingName
	 *            建筑名称
	 * 
	 * @return 建筑类
	 */
	public static NPBuilding parseBuildingFromAssetsByName(Context context,
			String path, String cityID, String buildingName) {
		List<NPBuilding> buildings = parseBuildingFromAssets(context, path,
				cityID);

		for (int i = 0; i < buildings.size(); i++) {
			NPBuilding building = buildings.get(i);
			if (building.getName().equals(buildingName)) {
				return building;
			}
		}
		return null;
	}
}
