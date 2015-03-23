//
//  NPXLocationAlgorithm.h
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __CloudAtlasTestProject__NPXLocationAlgorithm__
#define __CloudAtlasTestProject__NPXLocationAlgorithm__

#include <stdio.h>

#include <unordered_map>
#include <map>
#include <vector>
#include "../Entity/NPXPoint.h"
#include "../Entity/NPXScannedBeacon.h"
#include "../Entity/NPXPublicBeacon.h"
#include "NPXAlgorithmType.h"

using namespace std;
using namespace Nephogram::BLELocationEngine;

typedef unordered_map<NPXBeacon, NPXPublicBeacon, hash_beacon_key, equal_beacon_key> BeaconHashMap;

namespace Nephogram {
    namespace BLELocationEngine {
        
        
        class NPXLocationAlgorithm {
            
        public:
            NPXLocationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type);
            void setNearestBeacons(const vector<const NPXScannedBeacon *> &beacons);
            virtual const NPXPoint calculationLocation() = 0;
            virtual ~NPXLocationAlgorithm() {};
            
        protected:
            NPXAlgorithmType algorithmType;
            vector<const NPXScannedBeacon *> nearestBeacons;
            BeaconHashMap publicBeaconMap;

            const bool HasPublicBeacon(const NPXBeacon &key) const {
                return publicBeaconMap.find(key) != publicBeaconMap.end();
            }
            
            const NPXPublicBeacon & GetPublicBeacon(const NPXBeacon &key) const {
                return publicBeaconMap.at(key);
            }
            
            NPXPublicBeacon & GetPublicBeacon(const NPXBeacon &key) {
                return publicBeaconMap.at(key);
            }
            
        };
    }
}

Nephogram::BLELocationEngine::NPXLocationAlgorithm *CreateLocationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type);



#endif /* defined(__CloudAtlasTestProject__NPXLocationAlgorithm__) */
