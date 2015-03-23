//
//  NPXTriangulationAlgorithm.h
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __CloudAtlasTestProject__NPXTriangulationAlgorithm__
#define __CloudAtlasTestProject__NPXTriangulationAlgorithm__

#include <stdio.h>
#include "NPXLocationAlgorithm.h"

using namespace Nephogram::BLELocationEngine;

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXTriangulationAlgorithm: public NPXLocationAlgorithm {
            
        public:
            NPXTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type) : NPXLocationAlgorithm(beacons, type) {}
            virtual const NPXPoint calculationLocation() = 0;
            
        protected:
            NPXPoint pointFor(const NPXPoint &p1, double a1,const NPXPoint &p2, double a2);

            NPXPoint calculateBeaconLessThanThree();
            NPXPoint calculateOneBeacon(const NPXScannedBeacon *beacon);
            NPXPoint calculateTwoBeacons(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2);
            NPXPoint singleTriangulation(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2,const NPXScannedBeacon *b3);
            NPXPoint tripleTriangulation(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2, const NPXScannedBeacon *b3);
        };
    }
}

NPXTriangulationAlgorithm *CreateTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type);


#endif /* defined(__CloudAtlasTestProject__NPXTriangulationAlgorithm__) */
