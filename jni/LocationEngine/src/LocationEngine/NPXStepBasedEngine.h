//
//  NPXStepBasedEngine.h
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __CloudAtlasTestProject__NPXStepBasedEngine__
#define __CloudAtlasTestProject__NPXStepBasedEngine__

#include <stdio.h>

#include "ILocationEngine.h"
#include "../Filter/NPXMovingAverage.h"
#include "../Algorithm/NPXLocationAlgorithm.h"

const int DefaultMovingAverageWindow = 10;
const int DefaultStep = 1;
const double DefaultStepLength = 0.5;

using namespace Nephogram::BLELocationEngine;

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXStepBasedEngine : public ILocationEngine {
        public:
            NPXStepBasedEngine(NPXAlgorithmType type):algorithmType(type) {
                algorithm = NULL;
            }
            
            void Initilize(const vector<NPXPublicBeacon> &beacons);
            void processBeacons(vector<const NPXScannedBeacon *> &beacons);
            void addStepEvent();
            void reset();
            NPXPoint getLocation() const;
            
            ~NPXStepBasedEngine() {
                if (algorithm)
                    delete algorithm;
            }
            
        private:
            NPXLocationAlgorithm *algorithm;
            NPXAlgorithmType algorithmType;
            
            NPXPoint currentDisplayLocation;
            NPXPoint currentAnchorLocation;
            
            NPXMovingAverage xMovingAverage;
            NPXMovingAverage yMovingAverage;
            
            int stepCount;
            
            NPXPoint getIndependentLocation();

        };
        
    }
}


#endif /* defined(__CloudAtlasTestProject__NPXStepBasedEngine__) */
