package cn.nephogram.locationengine;

public class NPBeacon {
	protected String UUID;
	protected int major;
	protected int minor;
	protected String tag;
	protected NPBeaconType type;

	public NPBeacon(String uuid, int major, int minor, String tag,
			NPBeaconType type) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = type;
	}

	public NPBeacon(String uuid, int major, int minor, String tag) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = NPBeaconType.UNKNOWN;
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

	public NPBeaconType getType() {
		return type;
	}

	public void setType(NPBeaconType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NPBeacon [major=" + major + ", minor=" + minor + ", type="
				+ type + "]";
	}

	public boolean equalToBeacon(NPBeacon beacon) {
		if (beacon == null) {
			return false;
		}
		return major == beacon.major && minor == beacon.minor
				&& type == beacon.type;
	}

}
