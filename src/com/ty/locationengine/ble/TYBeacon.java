package com.ty.locationengine.ble;

public class TYBeacon {
	protected String UUID;
	protected int major;
	protected int minor;
	protected String tag;
	protected TYBeaconType type;

	public TYBeacon(String uuid, int major, int minor, String tag,
			TYBeaconType type) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = type;
	}

	public TYBeacon(String uuid, int major, int minor, String tag) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = TYBeaconType.UNKNOWN;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public TYBeaconType getType() {
		return type;
	}

	public void setType(TYBeaconType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NPBeacon [major=" + major + ", minor=" + minor + ", type="
				+ type + "]";
	}

	public boolean equalToBeacon(TYBeacon beacon) {
		if (beacon == null) {
			return false;
		}
		return major == beacon.major && minor == beacon.minor
				&& type == beacon.type;
	}

}
