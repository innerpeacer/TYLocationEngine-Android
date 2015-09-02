//
//  IPXLocationAlgorithm.h
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __BLEProject__IPXLocationAlgorithm__
#define __BLEProject__IPXLocationAlgorithm__

#include <stdio.h>

#include <unordered_map>
#include <map>
#include <vector>
#include "../Entity/IPXPoint.h"
#include "../Entity/IPXScannedBeacon.h"
#include "../Entity/IPXPublicBeacon.h"
#include "IPXAlgorithmType.h"

using namespace std;
using namespace Innerpeacer::BLELocationEngine;

typedef unordered_map<IPXBeacon, IPXPublicBeacon, hash_beacon_key, equal_beacon_key> BeaconHashMap;

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        
        class IPXLocationAlgorithm {
            
        public:
            IPXLocationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type);
            void setNearestBeacons(const vector<const IPXScannedBeacon *> &beacons);
            virtual const IPXPoint calculationLocation() = 0;
            virtual ~IPXLocationAlgorithm() {};
            
        protected:
            IPXAlgorithmType algorithmType;
            vector<const IPXScannedBeacon *> nearestBeacons;
            BeaconHashMap publicBeaconMap;

            const bool HasPublicBeacon(const IPXBeacon &key) const {
                return publicBeaconMap.find(key) != publicBeaconMap.end();
            }
            
            const IPXPublicBeacon & GetPublicBeacon(const IPXBeacon &key) const {
                return publicBeaconMap.at(key);
            }
            
            IPXPublicBeacon & GetPublicBeacon(const IPXBeacon &key) {
                return publicBeaconMap.at(key);
            }
            
        };
    }
}

Innerpeacer::BLELocationEngine::IPXLocationAlgorithm *CreateLocationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type);



#endif /* defined(__BLEProject__IPXLocationAlgorithm__) */
