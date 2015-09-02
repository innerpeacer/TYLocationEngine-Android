//
//  IPXTriangulationAlgorithm.h
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#ifndef __BLEProject__IPXTriangulationAlgorithm__
#define __BLEProject__IPXTriangulationAlgorithm__

#include <stdio.h>
#include "IPXLocationAlgorithm.h"

using namespace Innerpeacer::BLELocationEngine;

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXTriangulationAlgorithm: public IPXLocationAlgorithm {
            
        public:
            IPXTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type) : IPXLocationAlgorithm(beacons, type) {}
            virtual const IPXPoint calculationLocation() = 0;
            
        protected:
            IPXPoint pointFor(const IPXPoint &p1, double a1,const IPXPoint &p2, double a2);

            IPXPoint calculateBeaconLessThanThree();
            IPXPoint calculateOneBeacon(const IPXScannedBeacon *beacon);
            IPXPoint calculateTwoBeacons(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2);
            IPXPoint singleTriangulation(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2,const IPXScannedBeacon *b3);
            IPXPoint tripleTriangulation(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2, const IPXScannedBeacon *b3);
        };
    }
}

IPXTriangulationAlgorithm *CreateTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type);


#endif /* defined(__BLEProject__IPXTriangulationAlgorithm__) */
