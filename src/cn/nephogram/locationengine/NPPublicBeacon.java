package cn.nephogram.locationengine;

import cn.nephogram.data.NPLocalPoint;

public class NPPublicBeacon extends NPBeacon {
	private NPLocalPoint location;
	private String shopGid;

	public NPPublicBeacon(String uuid, int major, int minor, String tag,
			String gid) {
		super(uuid, major, minor, tag, NPBeaconType.PUBLIC);
		this.shopGid = gid;
	}

	public String getShopGid() {
		return shopGid;
	}

	public void setShopGid(String shopGid) {
		this.shopGid = shopGid;
	}

	public NPLocalPoint getLocation() {
		return location;
	}

	public void setLocation(NPLocalPoint location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "NPPublicBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
