/*
 * IPXGeometryCalculator.h
 *
 *  Created on: 2014-9-5
 *      Author: innerpeacer
 */

#ifndef IPXGEOMETRYCALCULATOR_H_
#define IPXGEOMETRYCALCULATOR_H_

#include "../Entity/IPXPoint.h"

using namespace Innerpeacer::BLELocationEngine;

IPXPoint scalePointWithCenter(IPXPoint center, IPXPoint scaledPoint,
		double length);

#endif /* IPXGEOMETRYCALCULATOR_H_ */
