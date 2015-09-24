package com.ty.locationengine.ibeacon;

import android.os.Messenger;

public class IPMonitoringRegion extends IPRangingRegion {
	public static final int NOT_SEEN = -1;
	private long lastSeenTimeMillis = -1L;
	private boolean wasInside;

	public IPMonitoringRegion(BeaconRegion region, Messenger replyTo) {
		super(region, replyTo);
	}

	public boolean markAsSeen(long currentTimeMillis) {
		this.lastSeenTimeMillis = currentTimeMillis;
		if (!this.wasInside) {
			this.wasInside = true;
			return true;
		}
		return false;
	}

	public boolean isInside(long currentTimeMillis) {
		return (this.lastSeenTimeMillis != -1L)
				&& (currentTimeMillis - this.lastSeenTimeMillis < BeaconService.EXPIRATION_MILLIS);
	}

	public boolean didJustExit(long currentTimeMillis) {
		if ((this.wasInside) && (!isInside(currentTimeMillis))) {
			this.lastSeenTimeMillis = -1L;
			this.wasInside = false;
			return true;
		}
		return false;
	}

}