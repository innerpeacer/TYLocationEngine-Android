/*
 * NPXPoint.cpp
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#include "NPXPoint.h"
#include <math.h>

#define LARGE_DISTANCE 100000000000

using namespace Nephogram::BLELocationEngine;

double NPXPoint::DistanceBetween(const NPXPoint &p1, const NPXPoint &p2) {
	if (p1.floor != p2.floor) {
		return LARGE_DISTANCE;
	}
	return sqrt(pow(p1.x - p2.x, 2) + pow(p1.y - p2.y, 2));

}

double NPXPoint::distanceBetween(const NPXPoint &p2) {
	if (floor != p2.floor) {
		return LARGE_DISTANCE;
	}
	return sqrt(pow(x - p2.x, 2) + pow(y - p2.y, 2));
}
