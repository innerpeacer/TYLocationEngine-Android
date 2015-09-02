//
//  IPXWeightingAlgorithm.cpp
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "IPXWeightingAlgorithm.h"
#include <cmath>

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXLinearWeightingAlgorithm : public IPXWeightingAlgorithm {
        public:
            IPXLinearWeightingAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXWeightingAlgorithm(beacons, IPXLinearWeighting) {}
            const IPXPoint calculationLocation();

        };
        
        const IPXPoint IPXLinearWeightingAlgorithm::calculationLocation()
        {
            vector<double> weightingArray;
            vector<IPXPoint> pointArray;
            double totalWeighting = 0.0;
            double totalWeightingX = 0.0;
            double totalWeightingY = 0.0;
            
            for (int i = 0; i < nearestBeacons.size(); ++i) {
                const IPXScannedBeacon *sb = nearestBeacons.at(i);
                const IPXPublicBeacon pb = GetPublicBeacon(*sb);
                
                double weighting = 1.0/sb->getAccuracy();
                weightingArray.insert(weightingArray.end(), weighting);
                totalWeighting += weighting;
                
                pointArray.insert(pointArray.end(), pb.getLocation());
            }
            
            for (int i = 0; i < weightingArray.size(); ++i) {
                IPXPoint point = pointArray.at(i);
                
                totalWeightingX += point.getX() * weightingArray.at(i);
                totalWeightingY += point.getY() * weightingArray.at(i);
            }
            
            return IPXPoint(totalWeightingX/totalWeighting, totalWeightingY/totalWeighting);
        }

    }
}


namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXQuadraticWeightingAlgorithm : public IPXWeightingAlgorithm {
        public:
            IPXQuadraticWeightingAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXWeightingAlgorithm(beacons, IPXQuadraticWeighting) {}
            const IPXPoint calculationLocation();
        };
        
        const IPXPoint IPXQuadraticWeightingAlgorithm::calculationLocation()
        {
            vector<double> weightingArray;
            vector<IPXPoint> pointArray;
            double totalWeighting = 0.0;
            double totalWeightingX = 0.0;
            double totalWeightingY = 0.0;
            
            for (int i = 0; i < nearestBeacons.size(); ++i) {
                const IPXScannedBeacon *sb = nearestBeacons.at(i);
                const IPXPublicBeacon pb = GetPublicBeacon(*sb);
                
                double weighting = 1.0/pow(sb->getAccuracy(), 2);
                weightingArray.insert(weightingArray.end(), weighting);
                totalWeighting += weighting;
                
                pointArray.insert(pointArray.end(), pb.getLocation());
            }
            
            for (int i = 0; i < weightingArray.size(); ++i) {
                IPXPoint point = pointArray.at(i);
                
                totalWeightingX += point.getX() * weightingArray.at(i);
                totalWeightingY += point.getY() * weightingArray.at(i);
            }
            
            return IPXPoint(totalWeightingX/totalWeighting, totalWeightingY/totalWeighting);

        }
        
    }
}


IPXWeightingAlgorithm *CreateWeighintAlgorithm(const std::vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type)
{
    switch (type) {
        case IPXLinearWeighting:
            return new IPXLinearWeightingAlgorithm(beacons);
            break;
            
        case IPXQuadraticWeighting:
            return new IPXQuadraticWeightingAlgorithm(beacons);
            break;
            
        default:
            return nullptr;
            break;
    }
}
