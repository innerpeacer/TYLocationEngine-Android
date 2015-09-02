package com.ty.locationengine.ble;

class IPStep extends IPVector2 {

	private long timestamp;

	public IPStep(double sx, double sy) {
		super(sx, sy);
	}

	public IPStep(double sx, double sy, long t) {
		super(sx, sy);
		this.timestamp = t;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long ts) {
		this.timestamp = ts;
	}
}
