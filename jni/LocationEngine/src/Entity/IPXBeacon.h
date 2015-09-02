/*
 * IPXBeacon.h
 *
 *  Created on: 2014-9-2
 *      Author: innerpeacer
 */

#ifndef IPXBEACON_H_
#define IPXBEACON_H_

#include <string>
//#include <cctype>

using namespace std;

namespace Innerpeacer {
namespace BLELocationEngine {

class IPXBeacon {
protected:
	string uuid;
	uint16_t major;
	uint16_t minor;

public:
    IPXBeacon()
    {
        
    }
    
	IPXBeacon(const char *uuid, uint16_t major, uint16_t minor) :
			uuid(uuid), major(major), minor(minor) {
		for (int i = 0; i < this->uuid.size(); ++i) {
			this->uuid[i] = toupper((int) this->uuid[i]);
		}
	}

	virtual ~IPXBeacon() {
	}

	uint16_t getMajor() const {
		return major;
	}

	uint16_t getMinor() const {
		return minor;
	}

	const string getUuid() const {
		return uuid;
	}

	friend class hash_beacon_key;
	friend class equal_beacon_key;
};

class hash_beacon_key {
public:
	size_t operator()(const IPXBeacon &key) const {
		hash<string> hash;
		size_t h = hash(key.uuid) * 31;
		h += key.major * 31;
		h += key.minor;
		return h;
	}
};

class equal_beacon_key {
public:
	bool operator()(const IPXBeacon &key1, const IPXBeacon &key2) const {
		return strcasecmp(key1.uuid.c_str(), key2.uuid.c_str()) == 0
				&& key1.major == key2.major && key1.minor == key2.minor;
	}

};
}
}

#endif /* IPXBEACON_H_ */
