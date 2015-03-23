//
//  NPXWeightingAlgorithm.h
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __CloudAtlasTestProject__NPXWeightingAlgorithm__
#define __CloudAtlasTestProject__NPXWeightingAlgorithm__

#include <stdio.h>
#include "NPXLocationAlgorithm.h"

using namespace Nephogram::BLELocationEngine;

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXWeightingAlgorithm : public NPXLocationAlgorithm {
            
        public:
            NPXWeightingAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type) : NPXLocationAlgorithm(beacons, type) {}
            virtual const NPXPoint calculationLocation() = 0;

        protected:
            
        };
        
        
    }
}

NPXWeightingAlgorithm *CreateWeighintAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type);

#endif /* defined(__CloudAtlasTestProject__NPXWeightingAlgorithm__) */
