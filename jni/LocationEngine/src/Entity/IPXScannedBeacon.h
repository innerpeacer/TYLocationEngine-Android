/*
 * IPXScannedBeacon.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef IPXSCANNEDBEACON_H_
#define IPXSCANNEDBEACON_H_

#include "IPXBeacon.h"

using namespace std;

namespace Innerpeacer {
namespace BLELocationEngine {

typedef enum {
	IPXProximityUnknwon = 0, IPXProximityImmediate, IPXProximityNear, IPXProximityFar
} IPXProximity;

class IPXScannedBeacon: public IPXBeacon {
public:
	IPXScannedBeacon(const char *uuid, uint16_t major, uint16_t minor, int rssi,
			double accuracy, IPXProximity proximity) :
			IPXBeacon(uuid, major, minor), rssi(rssi), accuracy(accuracy), proximity(
					proximity) {

	}
	virtual ~IPXScannedBeacon() {
        
    }

	int getRssi() const {
		return rssi;
	}

	double getAccuracy() const {
		return accuracy;
	}

	IPXProximity getProximity() const {
		return proximity;
	}

private:
	int rssi;
	double accuracy;
	IPXProximity proximity;
};
}
}

#endif /* IPXSCANNEDBEACON_H_ */
