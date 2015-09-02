/*
 * IPXGeometryCalculator.cpp
 *
 *  Created on: 2014-9-5
 *      Author: innerpeacer
 */

#include "IPXGeometryCalculator.h"

IPXPoint scalePointWithCenter(IPXPoint center, IPXPoint scaledPoint,
		double length) {
	double distance = IPXPoint::DistanceBetween(center, scaledPoint);

	if (distance <= length) {
		return scaledPoint;
	}

	double scale = length / distance;

	double x = scale * scaledPoint.getX() + (1 - scale) * center.getX();
	double y = scale * scaledPoint.getY() + (1 - scale) * center.getY();

	return IPXPoint(x, y, scaledPoint.getFloor());
}
