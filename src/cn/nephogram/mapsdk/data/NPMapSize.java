package cn.nephogram.mapsdk.data;

/**
 * 地图尺寸,对应地图所示区域实际大小
 */
public class NPMapSize {
	double x;
	double y;

	public NPMapSize(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}