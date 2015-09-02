//
//  IPXLocationAlgorithm.cpp
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "IPXLocationAlgorithm.h"

#include "IPXTriangulationAlgorithm.h"
#include "IPXWeightingAlgorithm.h"

using namespace Innerpeacer::BLELocationEngine;

IPXLocationAlgorithm::IPXLocationAlgorithm(const std::vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type) {
    vector<IPXPublicBeacon>::const_iterator iter;
    for (iter = beacons.begin(); iter != beacons.end(); ++iter) {
        publicBeaconMap.insert(BeaconHashMap::value_type((*iter), (*iter)));
    }
    algorithmType = type;
}

void IPXLocationAlgorithm::setNearestBeacons(const std::vector<const Innerpeacer::BLELocationEngine::IPXScannedBeacon *> &beacons)
{
    nearestBeacons.clear();
    vector<const IPXScannedBeacon *>::const_iterator iter;
    for (iter = beacons.begin(); iter != beacons.end(); ++iter) {
        nearestBeacons.insert(nearestBeacons.end(), *iter);
    }
}

Innerpeacer::BLELocationEngine::IPXLocationAlgorithm *CreateLocationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type)
{
    switch (type) {
        case IPXSingle:
        case IPXTripple:
        case IPXHybridSingle:
        case IPXHybridTripple:
            return CreateTriangulationAlgorithm(beacons, type);
            break;
            
        case IPXLinearWeighting:
        case IPXQuadraticWeighting:
            return CreateWeighintAlgorithm(beacons, type);
            break;
            
        default:
            return nullptr;
            break;
    }
}
