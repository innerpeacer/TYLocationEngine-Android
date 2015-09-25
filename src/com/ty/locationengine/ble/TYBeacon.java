package com.ty.locationengine.ble;

/**
 * 图呀Beacon类
 * 
 * @author innerpeacer
 * 
 */
public class TYBeacon {
	protected String UUID;
	protected int major;
	protected int minor;
	protected String tag;
	protected TYBeaconType type;

	/**
	 * Beacon类的构造方法
	 * 
	 * @param uuid
	 *            UUID
	 * @param major
	 *            Major参数
	 * @param minor
	 *            Minor参数
	 * @param tag
	 *            Beacon的Tag，用于标识Beacon，如Mac地址，序列号等
	 * @param type
	 *            Beacon用途类型，如用于定位、营销等
	 */
	public TYBeacon(String uuid, int major, int minor, String tag,
			TYBeaconType type) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = type;
	}

	/**
	 * Beacon类的构造方法
	 * 
	 * @param uuid
	 *            UUID
	 * @param major
	 *            Major参数
	 * @param minor
	 *            Minor参数
	 * @param tag
	 *            Beacon的Tag，用于标识Beacon，如Mac地址，序列号等
	 */
	public TYBeacon(String uuid, int major, int minor, String tag) {
		this.UUID = uuid;
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = TYBeaconType.UNKNOWN;
	}

	/**
	 * 获取Beacon的UUID
	 * 
	 * @return UUID
	 */
	public String getUUID() {
		return UUID;
	}

	/**
	 * 设置Beacon的UUID
	 * 
	 * @param uUID
	 *            UUID
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}

	/**
	 * 获取Beacon的Major值
	 * 
	 * @return Major值
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * 设置Beacon的Major值
	 * 
	 * @param major
	 *            Major参数
	 */
	public void setMajor(int major) {
		this.major = major;
	}

	/**
	 * 获取Beacon的Minor值
	 * 
	 * @return Minor值
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * 设置Beacon的Minor值
	 * 
	 * @param minor
	 *            Minor参数
	 */
	public void setMinor(int minor) {
		this.minor = minor;
	}

	/**
	 * 获取Beacon的Tag
	 * 
	 * @return Tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 设置Beacon的Tag
	 * 
	 * @param tag
	 *            Tag参数
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 获取Beacon的类型
	 * 
	 * @return Beacon的类型
	 */
	public TYBeaconType getType() {
		return type;
	}

	/**
	 * 设置Beacon的类型
	 * 
	 * @param type
	 *            类型参数
	 */
	public void setType(TYBeaconType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NPBeacon [major=" + major + ", minor=" + minor + ", type="
				+ type + "]";
	}

	/**
	 * 判断当前Beacon与目标Beacon是否相同
	 * 
	 * @param beacon
	 *            待判断Beacon
	 * @return 是否相同
	 */
	public boolean equalToBeacon(TYBeacon beacon) {
		if (beacon == null) {
			return false;
		}
		return major == beacon.major && minor == beacon.minor
				&& type == beacon.type;
	}

}
