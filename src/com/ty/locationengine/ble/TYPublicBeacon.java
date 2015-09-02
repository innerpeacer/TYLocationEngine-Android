package com.ty.locationengine.ble;

import com.ty.mapdata.TYLocalPoint;

public class TYPublicBeacon extends TYBeacon {
	private TYLocalPoint location;
	private String roomID;

	public TYPublicBeacon(String uuid, int major, int minor, String tag,
			String gid) {
		super(uuid, major, minor, tag, TYBeaconType.PUBLIC);
		this.roomID = gid;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String rID) {
		this.roomID = rID;
	}

	public TYLocalPoint getLocation() {
		return location;
	}

	public void setLocation(TYLocalPoint location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "NPPublicBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
