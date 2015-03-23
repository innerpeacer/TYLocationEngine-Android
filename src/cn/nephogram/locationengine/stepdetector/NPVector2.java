package cn.nephogram.locationengine.stepdetector;

public class NPVector2 {
	private double x;
	private double y;

	public NPVector2() {
		this.x = 0.0;
		this.y = 1.0;
	}

	public NPVector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static NPVector2 Scale(NPVector2 v, double scale) {
		return new NPVector2(v.x * scale, v.y * scale);
	}

	public static float AngleBetween(NPVector2 v1, NPVector2 v2) {
		double cos = InnerProduct(v1, v2) / (v1.getLength() * v2.getLength());
		double rad = Math.acos(cos);
		return (float) ((rad * 180) / Math.PI);
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public static NPVector2 Abstract(NPVector2 v1, NPVector2 v2) {
		return new NPVector2(v1.x - v2.x, v1.y - v2.y);
	}

	public static NPVector2 Add(NPVector2 v1, NPVector2 v2) {
		return new NPVector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static double InnerProduct(NPVector2 v1, NPVector2 v2) {
		return (v1.x * v2.x + v1.y * v2.y);
	}

	public NPVector2 normalized() {
		double length = this.getLength();
		return new NPVector2(x / length, y / length);
	}

	public double getAngle() {
		// y axis positive = 0.0 degree;
		// x axis positive = 90.0 degree;
		// y axis negative = 180.0 degree;
		// x axis negative = -90.0 degree;
		if (y == 0.0 && x >= 0.0) {
			return 90.0f;
		}

		if (y == 0.0 && x < 0.0) {
			return -90.0f;
		}

		double rad = Math.atan(x / y);
		double angle = (rad * 180) / Math.PI;
		if (y < 0) {
			if (x >= 0) {
				angle = (rad * 180) / Math.PI + 180;
			} else {
				angle = (rad * 180) / Math.PI - 180;
			}
		}

		return angle;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}
}
