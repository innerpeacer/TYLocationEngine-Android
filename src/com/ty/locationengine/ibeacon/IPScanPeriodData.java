package com.ty.locationengine.ibeacon;

import android.os.Parcel;
import android.os.Parcelable;

final class IPScanPeriodData implements Parcelable {
	public final long scanPeriodMillis;
	public final long waitTimeMillis;
	public static final Parcelable.Creator<IPScanPeriodData> CREATOR = new Parcelable.Creator<IPScanPeriodData>() {
		public IPScanPeriodData createFromParcel(Parcel source) {
			long scanPeriodMillis = source.readLong();
			long waitTimeMillis = source.readLong();
			return new IPScanPeriodData(scanPeriodMillis, waitTimeMillis);
		}

		public IPScanPeriodData[] newArray(int size) {
			return new IPScanPeriodData[size];
		}
	};

	public IPScanPeriodData(long scanPeriodMillis, long waitTimeMillis) {
		this.scanPeriodMillis = scanPeriodMillis;
		this.waitTimeMillis = waitTimeMillis;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		IPScanPeriodData that = (IPScanPeriodData) o;

		if (this.scanPeriodMillis != that.scanPeriodMillis)
			return false;
		if (this.waitTimeMillis != that.waitTimeMillis)
			return false;

		return true;
	}

	public int hashCode() {
		int result = (int) (this.scanPeriodMillis ^ this.scanPeriodMillis >>> 32);
		result = 31 * result
				+ (int) (this.waitTimeMillis ^ this.waitTimeMillis >>> 32);
		return result;
	}

	// public String toString() {
	// return Objects.toStringHelper(this)
	// .add("scanPeriodMillis", this.scanPeriodMillis)
	// .add("waitTimeMillis", this.waitTimeMillis).toString();
	// }

	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "ScanPeriodData [scanPeriodMillis=" + scanPeriodMillis
				+ ", waitTimeMillis=" + waitTimeMillis + "]";
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.scanPeriodMillis);
		dest.writeLong(this.waitTimeMillis);
	}

}
