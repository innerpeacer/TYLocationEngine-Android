package cn.nephogram.locationengine.stepdetector;


public class NPStep extends NPVector2 {

	private long timestamp;

	public NPStep(double sx, double sy) {
		super(sx, sy);
	}

	public NPStep(double sx, double sy, long t) {
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
