/*
 * NPXScannedBeacon.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef NPXSCANNEDBEACON_H_
#define NPXSCANNEDBEACON_H_

#include "NPXBeacon.h"

using namespace std;

namespace Nephogram {
namespace BLELocationEngine {

typedef enum {
	NPXProximityUnknwon = 0, NPXProximityImmediate, NPXProximityNear, NPXProximityFar
} NPXProximity;

class NPXScannedBeacon: public NPXBeacon {
public:
	NPXScannedBeacon(const char *uuid, uint16_t major, uint16_t minor, int rssi,
			double accuracy, NPXProximity proximity) :
			NPXBeacon(uuid, major, minor), rssi(rssi), accuracy(accuracy), proximity(
					proximity) {

	}
	virtual ~NPXScannedBeacon() {
        
    }

	int getRssi() const {
		return rssi;
	}

	double getAccuracy() const {
		return accuracy;
	}

	NPXProximity getProximity() const {
		return proximity;
	}

private:
	int rssi;
	double accuracy;
	NPXProximity proximity;
};
}
}

#endif /* NPXSCANNEDBEACON_H_ */
