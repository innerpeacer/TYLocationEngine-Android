package com.ty.locationengine.ble;

import com.ty.mapdata.TYLocalPoint;

/**
 * 公共Beacon类，当前用于表示固定部署用于定位的beacon
 * 
 * @author innerpeacer
 * 
 */
public class TYPublicBeacon extends TYBeacon {
	private TYLocalPoint location;
	private String roomID;

	/**
	 * 公共Beacon类的构造函数
	 * 
	 * @param uuid
	 *            UUID
	 * @param major
	 *            Major参数
	 * @param minor
	 *            Minor参数
	 * @param tag
	 *            Tag参数
	 * @param gid
	 *            部署位置所属房间或商铺的地理ID，可以为空
	 */
	public TYPublicBeacon(String uuid, int major, int minor, String tag,
			String gid) {
		super(uuid, major, minor, tag, TYBeaconType.PUBLIC);
		this.roomID = gid;
	}

	/**
	 * 获取Beacon部署位置所属房间或商铺的地理ID
	 * 
	 * @return 房间或商铺的地理ID
	 */
	public String getRoomID() {
		return roomID;
	}

	/**
	 * 获取Beacon部署位置所属房间或商铺的地理ID
	 * 
	 * @param rID
	 *            房间或商铺的地理ID
	 */
	public void setRoomID(String rID) {
		this.roomID = rID;
	}

	/**
	 * 获取Beacon所部署的位置
	 * 
	 * @return Beacon位置
	 */
	public TYLocalPoint getLocation() {
		return location;
	}

	/**
	 * 设置Beacon所部署的位置
	 * 
	 * @param location
	 *            位置点
	 */
	public void setLocation(TYLocalPoint location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "NPPublicBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
