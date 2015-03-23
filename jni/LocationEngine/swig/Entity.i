/* ============= NPXBeacon ================ */
/*
 * Swig File for NPXBeacon Class
 *
 *
*/		

%rename(hash_beacon_key) operator()(const NPXBeacon &key) const;
%rename(equal_beacon_key) operator()(const NPXBeacon &key1, const NPXBeacon &key2) const;
namespace Nephogram {
namespace BLELocationEngine {
class NPXBeacon {
public:
	NPXBeacon();
	NPXBeacon(const char *uuid, uint16_t major, uint16_t minor) :
			uuid(uuid), major(major), minor(minor);
	virtual ~NPXBeacon();
	uint16_t getMajor() const;
	uint16_t getMinor() const;
	const std::string getUuid() const;
	friend class hash_beacon_key;
	friend class equal_beacon_key;
}; 

class hash_beacon_key {
public:
	size_t operator()(const NPXBeacon &key) const;
};

class equal_beacon_key {
public:
	bool operator()(const NPXBeacon &key1, const NPXBeacon &key2) const;
};

}
}




/* ============= NPXPublicBeacon ================ */
/*
 * Swig File for NPXPublicBeacon Class
 *
 *
*/	
namespace Nephogram {
namespace BLELocationEngine {

class NPXPublicBeacon: public NPXBeacon {
public:
    NPXPublicBeacon();
	NPXPublicBeacon(const char *uuid, uint16_t major, uint16_t minor,
			Nephogram::BLELocationEngine::NPXPoint &location);
	virtual ~NPXPublicBeacon();
	Nephogram::BLELocationEngine::NPXPoint getLocation() const;
	void setLocation(Nephogram::BLELocationEngine::NPXPoint lp);
};
}
}



/* ============= NPXPoint ================ */
/*
 * Swig File for NPXPoint Class
 *
 *
*/	

%rename(assignment_point) operator=(const NPXPoint &p);
%rename(equal_point) operator==(const NPXPoint &aPoint);
%rename(not_equal_point) operator!=(const NPXPoint &aPoint);
namespace Nephogram {
namespace BLELocationEngine {
    
class NPXPoint {
public:
    NPXPoint();
	NPXPoint(double px, double py);
	NPXPoint(double px, double py, int pf);
	NPXPoint(const NPXPoint &p);
    NPXPoint operator=(const NPXPoint &p);
	virtual ~NPXPoint();
	int getFloor() const;
	void setFloor(int floor);
	double getX() const;
	double getY() const;

	static double DistanceBetween(const NPXPoint &p1, const NPXPoint &p2);
	double distanceBetween(const NPXPoint &p2);
    bool operator==(const NPXPoint &aPoint);
    bool operator!=(const NPXPoint &aPoint);
};
    
static NPXPoint INVALID_POINT;

}
}




/* ============= NPXScannedBeacon ================ */
/*
 * Swig File for NPXScannedBeacon Class
 *
 *
*/	

namespace Nephogram {
namespace BLELocationEngine {

typedef enum {
	NPXProximityUnknwon = 0, NPXProximityImmediate, NPXProximityNear, NPXProximityFar
} NPXProximity;

class NPXScannedBeacon: public NPXBeacon {
public:
	NPXScannedBeacon(const char *uuid, uint16_t major, uint16_t minor, int rssi,
			double accuracy, NPXProximity proximity);
	virtual ~NPXScannedBeacon();
	int getRssi() const;
	double getAccuracy() const;
	NPXProximity getProximity() const;
};
}
}

