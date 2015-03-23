package cn.nephogram.ibeacon.sdk.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import cn.nephogram.ibeacon.sdk.Beacon;
import cn.nephogram.ibeacon.sdk.Region;
import cn.nephogram.ibeacon.sdk.internal.Preconditions;

public final class RangingResult implements Parcelable {
	public final Region region;
	public final List<Beacon> beacons;
	public static final Parcelable.Creator<RangingResult> CREATOR = new Parcelable.Creator<RangingResult>() {
		public RangingResult createFromParcel(Parcel source) {
			ClassLoader classLoader = getClass().getClassLoader();
			Region region = (Region) source.readParcelable(classLoader);
			@SuppressWarnings("unchecked")
			List<Beacon> beacons = source.readArrayList(classLoader);

			return new RangingResult(region, beacons);
		}

		public RangingResult[] newArray(int size) {
			return new RangingResult[size];
		}
	};

	public RangingResult(Region region, Collection<Beacon> beacons) {
		this.region = Preconditions.checkNotNull(region,
				"region cannot be null");
		this.beacons = Collections.unmodifiableList(new ArrayList<Beacon>(
				Preconditions.checkNotNull(beacons, "beacons cannot be null")));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		RangingResult that = (RangingResult) o;

		if (!this.beacons.equals(that.beacons))
			return false;
		if (!this.region.equals(that.region))
			return false;

		return true;
	}

	public int hashCode() {
		int result = this.region.hashCode();
		result = 31 * result + this.beacons.hashCode();
		return result;
	}

	// public String toString() {
	// return Objects.toStringHelper(this).add("region", this.region)
	// .add("beacons", this.beacons).toString();
	// }

	@Override
	public String toString() {
		return "RangingResult [region=" + region + ", beacons=" + beacons + "]";
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.region, flags);
		dest.writeList(this.beacons);
	}
}