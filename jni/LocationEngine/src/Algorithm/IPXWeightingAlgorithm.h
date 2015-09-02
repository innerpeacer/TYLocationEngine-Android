//
//  IPXWeightingAlgorithm.h
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __BLEProject__IPXWeightingAlgorithm__
#define __BLEProject__IPXWeightingAlgorithm__

#include <stdio.h>
#include "IPXLocationAlgorithm.h"

using namespace Innerpeacer::BLELocationEngine;

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXWeightingAlgorithm : public IPXLocationAlgorithm {
            
        public:
            IPXWeightingAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type) : IPXLocationAlgorithm(beacons, type) {}
            virtual const IPXPoint calculationLocation() = 0;

        protected:
            
        };
        
        
    }
}

IPXWeightingAlgorithm *CreateWeighintAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type);

#endif /* defined(__BLEProject__IPXWeightingAlgorithm__) */
