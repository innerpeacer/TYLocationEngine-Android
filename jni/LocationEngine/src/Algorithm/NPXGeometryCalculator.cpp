/*
 * NPXGeometryCalculator.cpp
 *
 *  Created on: 2014-9-5
 *      Author: innerpeacer
 */

#include "NPXGeometryCalculator.h"

NPXPoint scalePointWithCenter(NPXPoint center, NPXPoint scaledPoint,
		double length) {
	double distance = NPXPoint::DistanceBetween(center, scaledPoint);

	if (distance <= length) {
		return scaledPoint;
	}

	double scale = length / distance;

	double x = scale * scaledPoint.getX() + (1 - scale) * center.getX();
	double y = scale * scaledPoint.getY() + (1 - scale) * center.getY();

	return NPXPoint(x, y, scaledPoint.getFloor());
}
