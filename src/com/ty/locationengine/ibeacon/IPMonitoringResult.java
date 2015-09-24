package com.ty.locationengine.ibeacon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class IPMonitoringResult implements Parcelable {
	public final BeaconRegion region;
	public final BeaconRegion.State state;
	public final List<Beacon> beacons;
	public static final Parcelable.Creator<IPMonitoringResult> CREATOR = new Parcelable.Creator<IPMonitoringResult>() {
		public IPMonitoringResult createFromParcel(Parcel source) {
			ClassLoader classLoader = getClass().getClassLoader();
			BeaconRegion region = (BeaconRegion) source
					.readParcelable(classLoader);
			BeaconRegion.State event = BeaconRegion.State.values()[source
					.readInt()];
			@SuppressWarnings("unchecked")
			List<Beacon> beacons = source.readArrayList(classLoader);
			return new IPMonitoringResult(region, event, beacons);
		}

		public IPMonitoringResult[] newArray(int size) {
			return new IPMonitoringResult[size];
		}
	};

	public IPMonitoringResult(BeaconRegion region, BeaconRegion.State state,
			Collection<Beacon> beacons) {
		this.region = ((BeaconRegion) IPPreconditions.checkNotNull(region,
				"region cannot be null"));
		this.state = ((BeaconRegion.State) IPPreconditions.checkNotNull(state,
				"state cannot be null"));
		this.beacons = new ArrayList<Beacon>(beacons);
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if ((o == null) || (getClass() != o.getClass()))
			return false;

		IPMonitoringResult that = (IPMonitoringResult) o;

		if (this.state != that.state)
			return false;
		if (this.region != null ? !this.region.equals(that.region)
				: that.region != null)
			return false;

		return true;
	}

	public int hashCode() {
		int result = this.region != null ? this.region.hashCode() : 0;
		result = 31 * result + (this.state != null ? this.state.hashCode() : 0);
		return result;
	}

	// public String toString() {
	// return Objects.toStringHelper(this).add("region", this.region)
	// .add("state", this.state.name()).add("beacons", this.beacons)
	// .toString();
	// }

	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "MonitoringResult [region=" + region + ", state=" + state
				+ ", beacons=" + beacons + "]";
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.region, flags);
		dest.writeInt(this.state.ordinal());
		dest.writeList(this.beacons);
	}
}