/*
 * NPXPoint.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef NPXPOINT_H_
#define NPXPOINT_H_

namespace Nephogram {
namespace BLELocationEngine {
    
class NPXPoint {
private:
	double x;
	double y;
	int floor;

public:
    
    NPXPoint()
    {
        x = -1000000.0;
        y = -1000000.0;
        floor = -1000000;
    }
    
	NPXPoint(double px, double py) :
			x(px), y(py) {
		floor = 1;
	}

	NPXPoint(double px, double py, int pf) :
			x(px), y(py), floor(pf) {

	}

	NPXPoint(const NPXPoint &p) {
		x = p.x;
		y = p.y;
		floor = p.floor;
	}
    
    NPXPoint operator=(const NPXPoint &p)
    {
        x = p.x;
		y = p.y;
		floor = p.floor;
        return *this;
    }

	virtual ~NPXPoint() {
        
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

	static double DistanceBetween(const NPXPoint &p1, const NPXPoint &p2);
	double distanceBetween(const NPXPoint &p2);
    
    bool operator==(const NPXPoint &aPoint)
    {
        return (x == aPoint.x) && (y == aPoint.y) && (floor == aPoint.floor);
    }
    
    bool operator!=(const NPXPoint &aPoint)
    {
        return !((*this)==aPoint);
    }
};
    
static NPXPoint INVALID_POINT(-1000000.0, -1000000.0, -1000000);

}
}

#endif /* NPXPOINT_H_ */
