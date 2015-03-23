package cn.nephogram.mapsdk.route;

import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.mapsdk.data.NPMapInfo.MapExtent;
import cn.nephogram.mapsdk.data.NPMapSize;

import com.esri.core.geometry.Point;

public class NPRoutePointConverter {

	private MapExtent baseExtent;
	private NPMapSize baseOffset;

	public NPRoutePointConverter(MapExtent extent, NPMapSize offset) {
		this.baseExtent = extent;
		this.baseOffset = offset;
	}

	public Point getRoutePointFromLocalPoint(NPLocalPoint lp) {
		double newX = lp.getX() + baseOffset.getX() * (lp.getFloor() - 1);
		return new Point(newX, lp.getY());
	}

	public NPLocalPoint getLocalPointFromRoutePoint(Point routePoint) {
		double xOffset = routePoint.getX() - baseExtent.getXmin();

		double grid = xOffset / baseOffset.getX();
		int index = (int) Math.floor(grid);

		double originX = routePoint.getX() - index * baseOffset.getX();
		double originY = routePoint.getY();
		int floor = index + 1;

		return new NPLocalPoint(originX, originY, floor);
	}

	public boolean checkPointValidity(NPLocalPoint point) {
		if (point.getX() >= baseExtent.getXmin()
				&& point.getX() <= baseExtent.getXmax()
				&& point.getY() >= baseExtent.getYmin()
				&& point.getY() <= baseExtent.getYmax()) {
			return true;
		} else {
			return false;
		}
	}
}