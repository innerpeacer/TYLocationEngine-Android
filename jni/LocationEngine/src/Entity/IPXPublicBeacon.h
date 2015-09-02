/*
 * IPXPublicBeacon.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef IPXPUBLICBEACON_H_
#define IPXPUBLICBEACON_H_

#include <string>

#include "IPXBeacon.h"
#include "IPXPoint.h"

using namespace std;

namespace Innerpeacer {
namespace BLELocationEngine {

class IPXPublicBeacon: public IPXBeacon {
public:
    IPXPublicBeacon()
    {
        
    }
    
	IPXPublicBeacon(const char *uuid, uint16_t major, uint16_t minor,
			IPXPoint &location) :
			IPXBeacon(uuid, major, minor), location(location) {
	}

	virtual ~IPXPublicBeacon() {

	}

	IPXPoint getLocation() const {
		return location;
	}

	void setLocation(IPXPoint lp) {
		location = lp;
	}

private:
	IPXPoint location;
};
}
}

#endif /* IPXPUBLICBEACON_H_ */
