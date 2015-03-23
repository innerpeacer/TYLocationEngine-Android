//
//  NPXTriangulationAlgorithm.cpp
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "NPXTriangulationAlgorithm.h"

#define DEFAULT_NUM_FOR_TRIANGULATION 4


NPXPoint NPXTriangulationAlgorithm::pointFor(const NPXPoint &p1, double a1,const NPXPoint &p2, double a2)
{
    double sum = a1 + a2;
    double x = (a1 * p2.getX() + a2 * p1.getX()) / sum;
    double y = (a1 * p2.getY() + a2 * p1.getY()) / sum;
    
    return NPXPoint(x, y, p1.getFloor());
}

NPXPoint NPXTriangulationAlgorithm::calculateBeaconLessThanThree()
{
    if (nearestBeacons.size() == 1) {
        return calculateOneBeacon(nearestBeacons.at(0));
    }
    
    if (nearestBeacons.size() == 2) {
        return calculateTwoBeacons(nearestBeacons.at(0), nearestBeacons.at(1));
    }
    return INVALID_POINT;
}

NPXPoint NPXTriangulationAlgorithm::calculateOneBeacon(const NPXScannedBeacon *beacon)
{
    if (beacon->getProximity() == NPXProximityImmediate
        || beacon->getProximity() == NPXProximityNear) {
        NPXPublicBeacon pb = GetPublicBeacon(*beacon);
        NPXPoint location = pb.getLocation();
        return NPXPoint(location);
    }
    return INVALID_POINT;
}

NPXPoint NPXTriangulationAlgorithm::calculateTwoBeacons(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2)
{
    const NPXPublicBeacon pb1 = GetPublicBeacon(*b1);
    const NPXPublicBeacon pb2 = GetPublicBeacon(*b2);
    
    NPXPoint p1 = pb1.getLocation();
    NPXPoint p2 = pb2.getLocation();
    
    return pointFor(p1, b1->getAccuracy(), p2, b2->getAccuracy());
}

NPXPoint NPXTriangulationAlgorithm::singleTriangulation(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2,const NPXScannedBeacon *b3)
{
    const NPXPublicBeacon pb1 = GetPublicBeacon(*b1);
    const NPXPublicBeacon pb2 = GetPublicBeacon(*b2);
    const NPXPublicBeacon pb3 = GetPublicBeacon(*b3);
    
    NPXPoint p1 = pb1.getLocation();
    NPXPoint p2 = pb2.getLocation();
    NPXPoint p3 = pb3.getLocation();
    
    NPXPoint p12 = pointFor(p1, b1->getAccuracy(), p2, b2->getAccuracy());
    NPXPoint p13 = pointFor(p1, b1->getAccuracy(), p3, b3->getAccuracy());
    
    NPXPoint result = pointFor(p12, b2->getAccuracy(), p13, b3->getAccuracy());
    result.setFloor(pb1.getLocation().getFloor());
    
    return result;
}

NPXPoint NPXTriangulationAlgorithm::tripleTriangulation(const NPXScannedBeacon *b1, const NPXScannedBeacon *b2, const NPXScannedBeacon *b3)
{
    NPXPoint t123 = singleTriangulation(b1, b2, b3);
    NPXPoint t231 = singleTriangulation(b2, b3, b1);
    NPXPoint t312 = singleTriangulation(b3, b1, b2);
    
    double x = (t123.getX() + t231.getX() + t312.getX()) / 3.0;
    double y = (t123.getY() + t231.getY() + t312.getY()) / 3.0;
    
    return NPXPoint(x, y, t123.getFloor());
}


namespace Nephogram {
    namespace BLELocationEngine {

        class NPXSingleTriangulationAlgorithm : public NPXTriangulationAlgorithm {
        public:
            NPXSingleTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXTriangulationAlgorithm(beacons, NPXSingle) {}
            const NPXPoint calculationLocation();
            
        protected:
            
        };
        
        const NPXPoint NPXSingleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                return singleTriangulation(nearestBeacons.at(0), nearestBeacons.at(1), nearestBeacons.at(2));
            }
            return INVALID_POINT;
        }
        
    }
}


namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXTrippleTriangulationAlgorithm : public NPXTriangulationAlgorithm {
        public:
            NPXTrippleTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXTriangulationAlgorithm(beacons, NPXTripple) {}
            const NPXPoint calculationLocation();
        };
        
        const NPXPoint NPXTrippleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                return tripleTriangulation((NPXScannedBeacon *)nearestBeacons.at(0), (NPXScannedBeacon *)nearestBeacons.at(1), (NPXScannedBeacon *)nearestBeacons.at(2));
            }
            return INVALID_POINT;
        }
    }
}

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXHybridSingleTriangulationAlgorithm : public NPXTriangulationAlgorithm {
        public:
            NPXHybridSingleTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXTriangulationAlgorithm(beacons, NPXHybridSingle) {}
            const NPXPoint calculationLocation();
            
        private:
            NPXPoint calculateLocationWithAverage3();
            NPXPoint calculateLocationWithAverage4();
        };
        
        const NPXPoint NPXHybridSingleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                const NPXScannedBeacon *b1 = nearestBeacons.at(0);
                const NPXScannedBeacon *b2 = nearestBeacons.at(1);
                
                if (b1->getAccuracy() / b2->getAccuracy() < 0.33) {
                    return calculateLocationWithAverage3();
                } else {
                    return calculateLocationWithAverage4();
                }
            }
            return INVALID_POINT;
        }
        
        NPXPoint NPXHybridSingleTriangulationAlgorithm::calculateLocationWithAverage3()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<NPXPoint> pointVector;
                for (int i = 0; i < 1; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            NPXPoint p = singleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<NPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                NPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                
                return NPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
            }
            
            return INVALID_POINT;
        }
        
        NPXPoint NPXHybridSingleTriangulationAlgorithm::calculateLocationWithAverage4()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<NPXPoint> pointVector;
                for (int i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            NPXPoint p = singleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<NPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                NPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                
                return NPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
            }
            
            return INVALID_POINT;
        }
    }
}

namespace Nephogram {
    namespace BLELocationEngine {
        
        class NPXHybridTrippleTriangulationAlgorithm : public NPXTriangulationAlgorithm {
        public:
            NPXHybridTrippleTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons) : NPXTriangulationAlgorithm(beacons, NPXHybridTripple) {}
            const NPXPoint calculationLocation();
            
        private:
            const NPXPoint calculateLocationUsingTripleWithAverage3();
            const NPXPoint calculateLocationUsingTripleWithAverage4();

        };
        
        const NPXPoint NPXHybridTrippleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                const NPXScannedBeacon *b1 = nearestBeacons.at(0);
                const NPXScannedBeacon *b2 = nearestBeacons.at(1);
                
                if (b1->getAccuracy() / b2->getAccuracy() < 0.33) {
                    return calculateLocationUsingTripleWithAverage3();
                } else {
                    return calculateLocationUsingTripleWithAverage4();
                }
            }
            return INVALID_POINT;
        }
        
        const NPXPoint NPXHybridTrippleTriangulationAlgorithm::calculateLocationUsingTripleWithAverage3()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<NPXPoint> pointVector;
                for (int i = 0; i < 1; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            NPXPoint p = tripleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<NPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                NPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                
                return NPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
            }
            return INVALID_POINT;
        }
        
        const NPXPoint NPXHybridTrippleTriangulationAlgorithm::calculateLocationUsingTripleWithAverage4()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                vector<NPXPoint> pointVector;
                for (int i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            NPXPoint p = tripleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<NPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                NPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                
                return NPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
            }
            return INVALID_POINT;
        }

    }
}

NPXTriangulationAlgorithm *CreateTriangulationAlgorithm(const vector<NPXPublicBeacon> &beacons, NPXAlgorithmType type)
{
    switch (type) {
        case NPXSingle:
            return new NPXSingleTriangulationAlgorithm(beacons);
            break;
            
        case NPXTripple:
            return new NPXTrippleTriangulationAlgorithm(beacons);
            break;
            
        case NPXHybridSingle:
            return new NPXHybridSingleTriangulationAlgorithm(beacons);
            break;
            
        case NPXHybridTripple:
            return new NPXHybridTrippleTriangulationAlgorithm(beacons);
            break;
            
        default:
            return nullptr;
            break;
    }
}