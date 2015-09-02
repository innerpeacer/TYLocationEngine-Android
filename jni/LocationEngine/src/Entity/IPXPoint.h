/*
 * IPXPoint.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef IPXPOINT_H_
#define IPXPOINT_H_

namespace Innerpeacer {
namespace BLELocationEngine {
    
class IPXPoint {
private:
	double x;
	double y;
	int floor;

public:
    
    IPXPoint()
    {
        x = -1000000.0;
        y = -1000000.0;
        floor = -1000000;
    }
    
	IPXPoint(double px, double py) :
			x(px), y(py) {
		floor = 1;
	}

	IPXPoint(double px, double py, int pf) :
			x(px), y(py), floor(pf) {

	}

	IPXPoint(const IPXPoint &p) {
		x = p.x;
		y = p.y;
		floor = p.floor;
	}
    
    IPXPoint operator=(const IPXPoint &p)
    {
        x = p.x;
		y = p.y;
		floor = p.floor;
        return *this;
    }

	virtual ~IPXPoint() {
        
    }

	int getFloor() const {
		return floor;
	}

	void setFloor(int floor) {
		this->floor = floor;
	}

	double getX() const {
		return x;
	}

	double getY() const {
		return y;
	}

	static double DistanceBetween(const IPXPoint &p1, const IPXPoint &p2);
	double distanceBetween(const IPXPoint &p2);
    
    bool operator==(const IPXPoint &aPoint)
    {
        return (x == aPoint.x) && (y == aPoint.y) && (floor == aPoint.floor);
    }
    
    bool operator!=(const IPXPoint &aPoint)
    {
        return !((*this)==aPoint);
    }
};
    
static IPXPoint INVALID_POINT(-1000000.0, -1000000.0, -1000000);

}
}

#endif /* IPXPOINT_H_ */
