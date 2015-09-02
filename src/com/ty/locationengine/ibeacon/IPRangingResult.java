package com.ty.locationengine.ibeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


final class IPRangingResult implements Parcelable {
	public final BeaconRegion region;
	public final List<Beacon> beacons;
	public static final Parcelable.Creator<IPRangingResult> CREATOR = new Parcelable.Creator<IPRangingResult>() {
		public IPRangingResult createFromParcel(Parcel source) {
			ClassLoader classLoader = getClass().getClassLoader();
			BeaconRegion region = (BeaconRegion) source.readParcelable(classLoader);
			@SuppressWarnings("unchecked")
			List<Beacon> beacons = source.readArrayList(classLoader);

			return new IPRangingResult(region, beacons);
		}

		public IPRangingResult[] newArray(int size) {
			return new IPRangingResult[size];
		}
	};

	public IPRangingResult(BeaconRegion region, Collection<Beacon> beacons) {
		this.region = IPPreconditions.checkNotNull(region,
				"region cannot be null");
		this.beacons = Collections.unmodifiableList(new ArrayList<Beacon>(
				IPPreconditions.checkNotNull(beacons, "beacons cannot be null")));
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		IPRangingResult that = (IPRangingResult) o;

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