/*
 * ILocationEngine.h
 *
 *  Created on: 2014-9-3
 *      Author: innerpeacer
 */

#ifndef ILOCATIONENGINE_H_
#define ILOCATIONENGINE_H_

#include <vector>
#include "../Entity/NPXScannedBeacon.h"
#include "../Entity/NPXPoint.h"
#include "../Entity/NPXPublicBeacon.h"
#include "../Algorithm/NPXAlgorithmType.h"

using namespace Nephogram::BLELocationEngine;

namespace Nephogram {
    namespace BLELocationEngine {
        struct ILocationEngine {
        public:
            ILocationEngine() {};
            virtual void Initilize(const vector<NPXPublicBeacon> &beacons) = 0;
            virtual void processBeacons(vector<const NPXScannedBeacon *> &beacons) = 0;
            virtual void addStepEvent() = 0;
            virtual void reset() = 0;
            virtual NPXPoint getLocation() const = 0;
            virtual ~ILocationEngine() {};
        };
    }
}

ILocationEngine *CreateNPXStepBaseTriangulationEngine(NPXAlgorithmType type);

#endif /* ILOCATIONENGINE_H_ */
