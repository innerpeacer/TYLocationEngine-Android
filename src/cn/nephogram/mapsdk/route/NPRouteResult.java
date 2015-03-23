package cn.nephogram.mapsdk.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

@SuppressLint("UseSparseArrays")
public class NPRouteResult {

	private Map<Integer, Polyline> routeGraphicDict = new HashMap<Integer, Polyline>();
	private List<Integer> routeFloorArray = new ArrayList<Integer>();;

	public NPRouteResult(Map<Integer, Polyline> rgDict, List<Integer> rfArray) {
		routeGraphicDict.putAll(rgDict);
		routeFloorArray.addAll(rfArray);
	}

	public Polyline getRouteOnFloor(int floorIndex) {
		return routeGraphicDict.get(floorIndex);
	}

	public Point getFirstPointOnFloor(int floorIndex) {
		Point result = null;

		Polyline line = routeGraphicDict.get(floorIndex);
		if (line != null && line.getPathCount() > 0 && line.getPointCount() > 0) {
			result = line.getPoint(line.getPathStart(0));
		}

		return result;
	}

	public Point getLastPointOnFloor(int floorIndex) {
		Point result = null;

		Polyline line = routeGraphicDict.get(floorIndex);
		if (line != null && line.getPathCount() > 0 && line.getPointCount() > 0) {
			result = line.getPoint(line.getPathEnd(0) - 1);
		}
		return result;
	}

	public boolean isFirstFloor(int floorIndex) {
		return routeFloorArray.get(0) == floorIndex;
	}

	public boolean isLastFloor(int floorIndex) {
		return routeFloorArray.get(routeFloorArray.size() - 1) == floorIndex;
	}

	public Integer getPreviousFloor(int floorIndex) {
		int index = routeFloorArray.indexOf(floorIndex);

		if (index != -1 || index == 0) {
			return null;
		}
		return routeFloorArray.get(index - 1);
	}

	public Integer getNextFloor(int floorIndex) {
		int index = routeFloorArray.indexOf(floorIndex);
		if (index == -1 || index == routeFloorArray.size() - 1) {
			return null;
		}
		return routeFloorArray.get(index + 1);
	}
}
