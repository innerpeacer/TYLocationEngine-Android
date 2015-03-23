//
//  NPXLocationAlgorithm.cpp
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "NPXLocationAlgorithm.h"

#include "NPXTriangulationAlgorithm.h"
#include "NPXWeightingAlgorithm.h"

using namespace Nephogram::BLELocationEngine;

NPXLocationAlgorithm::NPXLocationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type) {
    vector<NPXPublicBeacon>::const_iterator iter;
    for (iter = beacons.begin(); iter != beacons.end(); ++iter) {
        publicBeaconMap.insert(BeaconHashMap::value_type((*iter), (*iter)));
    }
    algorithmType = type;
}

void NPXLocationAlgorithm::setNearestBeacons(const vector<const Nephogram::BLELocationEngine::NPXScannedBeacon *> &beacons)
{
    nearestBeacons.clear();
    vector<const NPXScannedBeacon *>::const_iterator iter;
    for (iter = beacons.begin(); iter != beacons.end(); ++iter) {
        nearestBeacons.insert(nearestBeacons.end(), *iter);
    }
}

Nephogram::BLELocationEngine::NPXLocationAlgorithm *CreateLocationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type)
{
    switch (type) {
        case NPXSingle:
        case NPXTripple:
        case NPXHybridSingle:
        case NPXHybridTripple:
            return CreateTriangulationAlgorithm(beacons, type);
            break;
            
        case NPXLinearWeighting:
        case NPXQuadraticWeighting:
            return CreateWeighintAlgorithm(beacons, type);
            break;
            
        default:
            return nullptr;
            break;
    }
}
