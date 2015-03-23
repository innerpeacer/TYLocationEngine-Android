/*
 * CAPublicBeacon.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef NPXPUBLICBEACON_H_
#define NPXPUBLICBEACON_H_

#include <string>

#include "NPXBeacon.h"
#include "NPXPoint.h"

using namespace std;

namespace Nephogram {
namespace BLELocationEngine {

class NPXPublicBeacon: public NPXBeacon {
public:
    NPXPublicBeacon()
    {

    }

	NPXPublicBeacon(const char *uuid, uint16_t major, uint16_t minor,
			Nephogram::BLELocationEngine::NPXPoint &location) :
			NPXBeacon(uuid, major, minor), location(location) {
	}

	virtual ~NPXPublicBeacon() {

	}

	NPXPoint getLocation() const {
		return location;
	}

	void setLocation(NPXPoint lp) {
		location = lp;
	}

private:
	NPXPoint location;
};
}
}

#endif /* NPXPUBLICBEACON_H_ */
