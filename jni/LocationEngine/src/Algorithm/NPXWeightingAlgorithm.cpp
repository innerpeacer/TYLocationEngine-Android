//
//  NPXWeightingAlgorithm.cpp
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "NPXWeightingAlgorithm.h"
#include <cmath>

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXLinearWeightingAlgorithm : public NPXWeightingAlgorithm {
        public:
            NPXLinearWeightingAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXWeightingAlgorithm(beacons, NPXLinearWeighting) {}
            const NPXPoint calculationLocation();

        };
        
        const NPXPoint NPXLinearWeightingAlgorithm::calculationLocation()
        {
            vector<double> weightingArray;
            vector<NPXPoint> pointArray;
            double totalWeighting = 0.0;
            double totalWeightingX = 0.0;
            double totalWeightingY = 0.0;
            
            for (int i = 0; i < nearestBeacons.size(); ++i) {
                const NPXScannedBeacon *sb = nearestBeacons.at(i);
                const NPXPublicBeacon pb = GetPublicBeacon(*sb);
                
                double weighting = 1.0/sb->getAccuracy();
                weightingArray.insert(weightingArray.end(), weighting);
                totalWeighting += weighting;
                
                pointArray.insert(pointArray.end(), pb.getLocation());
            }
            
            for (int i = 0; i < weightingArray.size(); ++i) {
                NPXPoint point = pointArray.at(i);
                
                totalWeightingX += point.getX() * weightingArray.at(i);
                totalWeightingY += point.getY() * weightingArray.at(i);
            }
            
            return NPXPoint(totalWeightingX/totalWeighting, totalWeightingY/totalWeighting);
        }

    }
}


namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXQuadraticWeightingAlgorithm : public NPXWeightingAlgorithm {
        public:
            NPXQuadraticWeightingAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXWeightingAlgorithm(beacons, NPXQuadraticWeighting) {}
            const NPXPoint calculationLocation();
        };
        
        const NPXPoint NPXQuadraticWeightingAlgorithm::calculationLocation()
        {
            vector<double> weightingArray;
            vector<NPXPoint> pointArray;
            double totalWeighting = 0.0;
            double totalWeightingX = 0.0;
            double totalWeightingY = 0.0;
            
            for (int i = 0; i < nearestBeacons.size(); ++i) {
                const NPXScannedBeacon *sb = nearestBeacons.at(i);
                const NPXPublicBeacon pb = GetPublicBeacon(*sb);
                
                double weighting = 1.0/pow(sb->getAccuracy(), 2);
                weightingArray.insert(weightingArray.end(), weighting);
                totalWeighting += weighting;
                
                pointArray.insert(pointArray.end(), pb.getLocation());
            }
            
            for (int i = 0; i < weightingArray.size(); ++i) {
                NPXPoint point = pointArray.at(i);
                
                totalWeightingX += point.getX() * weightingArray.at(i);
                totalWeightingY += point.getY() * weightingArray.at(i);
            }
            
            return NPXPoint(totalWeightingX/totalWeighting, totalWeightingY/totalWeighting);

        }
        
    }
}


NPXWeightingAlgorithm *CreateWeighintAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type)
{
    switch (type) {
        case NPXLinearWeighting:
            return new NPXLinearWeightingAlgorithm(beacons);
            break;
            
        case NPXQuadraticWeighting:
            return new NPXQuadraticWeightingAlgorithm(beacons);
            break;
            
        default:
            return nullptr;
            break;
    }
}
