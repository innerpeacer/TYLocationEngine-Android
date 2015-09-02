/* ============= IPXBeacon ================ */
/*
 * Swig File for IPXBeacon Class
 *
 *
*/		

%rename(HashBeaconKey) operator()(const IPXBeacon &key) const;
%rename(EqualBeaconKey) operator()(const IPXBeacon &key1, const IPXBeacon &key2) const;
namespace Innerpeacer {
namespace BLELocationEngine {
class IPXBeacon {
public:
	IPXBeacon();
	IPXBeacon(const char *uuid, uint16_t major, uint16_t minor) :
			uuid(uuid), major(major), minor(minor);
	virtual ~IPXBeacon();
	uint16_t getMajor() const;
	uint16_t getMinor() const;
	const std::string getUuid() const;
	friend class hash_beacon_key;
	friend class equal_beacon_key;
}; 

class hash_beacon_key {
public:
	size_t operator()(const IPXBeacon &key) const;
};

class equal_beacon_key {
public:
	bool operator()(const IPXBeacon &key1, const IPXBeacon &key2) const;
};

}
}




/* ============= IPXPublicBeacon ================ */
/*
 * Swig File for IPXPublicBeacon Class
 *
 *
*/	
namespace Innerpeacer {
namespace BLELocationEngine {

class IPXPublicBeacon: public IPXBeacon {
public:
    IPXPublicBeacon();
	IPXPublicBeacon(const char *uuid, uint16_t major, uint16_t minor,
			Innerpeacer::BLELocationEngine::IPXPoint &location);
	virtual ~IPXPublicBeacon();
	Innerpeacer::BLELocationEngine::IPXPoint getLocation() const;
	void setLocation(Innerpeacer::BLELocationEngine::IPXPoint lp);
};
}
}



/* ============= IPXPoint ================ */
/*
 * Swig File for IPXPoint Class
 *
 *
*/	

%rename(assignment_point) operator=(const IPXPoint &p);
%rename(equal_point) operator==(const IPXPoint &aPoint);
%rename(not_equal_point) operator!=(const IPXPoint &aPoint);
namespace Innerpeacer {
namespace BLELocationEngine {
    
class IPXPoint {
public:
    IPXPoint();
	IPXPoint(double px, double py);
	IPXPoint(double px, double py, int pf);
	IPXPoint(const IPXPoint &p);
    IPXPoint operator=(const IPXPoint &p);
	virtual ~IPXPoint();
	int getFloor() const;
	void setFloor(int floor);
	double getX() const;
	double getY() const;

	static double DistanceBetween(const IPXPoint &p1, const IPXPoint &p2);
	double distanceBetween(const IPXPoint &p2);
    bool operator==(const IPXPoint &aPoint);
    bool operator!=(const IPXPoint &aPoint);
};
    
static IPXPoint INVALID_POINT;

}
}




/* ============= IPXScannedBeacon ================ */
/*
 * Swig File for IPXScannedBeacon Class
 *
 *
*/	

namespace Innerpeacer {
namespace BLELocationEngine {

typedef enum {
	IPXProximityUnknwon = 0, IPXProximityImmediate, IPXProximityNear, IPXProximityFar
} IPXProximity;

class IPXScannedBeacon: public IPXBeacon {
public:
	IPXScannedBeacon(const char *uuid, uint16_t major, uint16_t minor, int rssi,
			double accuracy, IPXProximity proximity);
	virtual ~IPXScannedBeacon();
	int getRssi() const;
	double getAccuracy() const;
	IPXProximity getProximity() const;
};
}
}

