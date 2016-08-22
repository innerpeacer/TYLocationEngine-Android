//
//  IPXTriangulationAlgorithm.cpp
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "IPXTriangulationAlgorithm.h"

#define DEFAULT_NUM_FOR_TRIANGULATION 4


IPXPoint IPXTriangulationAlgorithm::pointFor(const IPXPoint &p1, double a1,const IPXPoint &p2, double a2)
{
    double sum = a1 + a2;
    double x = (a1 * p2.getX() + a2 * p1.getX()) / sum;
    double y = (a1 * p2.getY() + a2 * p1.getY()) / sum;
    
    return IPXPoint(x, y, p1.getFloor());
}

IPXPoint IPXTriangulationAlgorithm::calculateBeaconLessThanThree()
{
    if (nearestBeacons.size() == 1) {
        return calculateOneBeacon(nearestBeacons.at(0));
    }
    
    if (nearestBeacons.size() == 2) {
        return calculateTwoBeacons(nearestBeacons.at(0), nearestBeacons.at(1));
    }
    return INVALID_POINT;
}

IPXPoint IPXTriangulationAlgorithm::calculateOneBeacon(const IPXScannedBeacon *beacon)
{
    if (beacon->getProximity() == IPXProximityImmediate
        || beacon->getProximity() == IPXProximityNear) {
        if (HasPublicBeacon(*beacon)) {
            IPXPublicBeacon pb = GetPublicBeacon(*beacon);
            IPXPoint location = pb.getLocation();
            return IPXPoint(location);
        }
    }
    return INVALID_POINT;
}

IPXPoint IPXTriangulationAlgorithm::calculateTwoBeacons(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2)
{
    if (HasPublicBeacon(*b1) && HasPublicBeacon(*b2)) {
        const IPXPublicBeacon pb1 = GetPublicBeacon(*b1);
        const IPXPublicBeacon pb2 = GetPublicBeacon(*b2);

        IPXPoint p1 = pb1.getLocation();
        IPXPoint p2 = pb2.getLocation();

        return pointFor(p1, b1->getAccuracy(), p2, b2->getAccuracy());
    }
    return  INVALID_POINT;
}

IPXPoint IPXTriangulationAlgorithm::singleTriangulation(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2,const IPXScannedBeacon *b3)
{
    if (HasPublicBeacon(*b1) && HasPublicBeacon(*b2) && HasPublicBeacon(*b3)) {
        const IPXPublicBeacon pb1 = GetPublicBeacon(*b1);
        const IPXPublicBeacon pb2 = GetPublicBeacon(*b2);
        const IPXPublicBeacon pb3 = GetPublicBeacon(*b3);

        IPXPoint p1 = pb1.getLocation();
        IPXPoint p2 = pb2.getLocation();
        IPXPoint p3 = pb3.getLocation();

        IPXPoint p12 = pointFor(p1, b1->getAccuracy(), p2, b2->getAccuracy());
        IPXPoint p13 = pointFor(p1, b1->getAccuracy(), p3, b3->getAccuracy());

        IPXPoint result = pointFor(p12, b2->getAccuracy(), p13, b3->getAccuracy());
        result.setFloor(pb1.getLocation().getFloor());

        return result;
    }
    return INVALID_POINT;
}

IPXPoint IPXTriangulationAlgorithm::tripleTriangulation(const IPXScannedBeacon *b1, const IPXScannedBeacon *b2, const IPXScannedBeacon *b3)
{
    IPXPoint t123 = singleTriangulation(b1, b2, b3);
    IPXPoint t231 = singleTriangulation(b2, b3, b1);
    IPXPoint t312 = singleTriangulation(b3, b1, b2);
    
    double x = (t123.getX() + t231.getX() + t312.getX()) / 3.0;
    double y = (t123.getY() + t231.getY() + t312.getY()) / 3.0;
    
    return IPXPoint(x, y, t123.getFloor());
}


namespace Innerpeacer {
    namespace BLELocationEngine {

        class IPXSingleTriangulationAlgorithm : public IPXTriangulationAlgorithm {
        public:
            IPXSingleTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXTriangulationAlgorithm(beacons, IPXSingle) {}
            const IPXPoint calculationLocation();
            
        protected:
            
        };
        
        const IPXPoint IPXSingleTriangulationAlgorithm::calculationLocation()
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


namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXTrippleTriangulationAlgorithm : public IPXTriangulationAlgorithm {
        public:
            IPXTrippleTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXTriangulationAlgorithm(beacons, IPXTripple) {}
            const IPXPoint calculationLocation();
        };
        
        const IPXPoint IPXTrippleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                return tripleTriangulation((IPXScannedBeacon *)nearestBeacons.at(0), (IPXScannedBeacon *)nearestBeacons.at(1), (IPXScannedBeacon *)nearestBeacons.at(2));
            }
            return INVALID_POINT;
        }
    }
}

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXHybridSingleTriangulationAlgorithm : public IPXTriangulationAlgorithm {
        public:
            IPXHybridSingleTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXTriangulationAlgorithm(beacons, IPXHybridSingle) {}
            const IPXPoint calculationLocation();
            
        private:
            IPXPoint calculateLocationWithAverage3();
            IPXPoint calculateLocationWithAverage4();
        };
        
        const IPXPoint IPXHybridSingleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                const IPXScannedBeacon *b1 = nearestBeacons.at(0);
                const IPXScannedBeacon *b2 = nearestBeacons.at(1);
                
                if (b1->getAccuracy() / b2->getAccuracy() < 0.33) {
                    return calculateLocationWithAverage3();
                } else {
                    return calculateLocationWithAverage4();
                }
            }
            return INVALID_POINT;
        }
        
        IPXPoint IPXHybridSingleTriangulationAlgorithm::calculateLocationWithAverage3()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<IPXPoint> pointVector;
                for (int i = 0; i < 1; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            IPXPoint p = singleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<IPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                if (HasPublicBeacon(*(nearestBeacons.at(0)))) {
                    IPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                    return IPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
                }
            }
            
            return INVALID_POINT;
        }
        
        IPXPoint IPXHybridSingleTriangulationAlgorithm::calculateLocationWithAverage4()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<IPXPoint> pointVector;
                for (int i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            IPXPoint p = singleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<IPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                if (HasPublicBeacon(*(nearestBeacons.at(0)))) {
                    IPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                    return IPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
                }
            }
            
            return INVALID_POINT;
        }
    }
}

namespace Innerpeacer {
    namespace BLELocationEngine {
        
        class IPXHybridTrippleTriangulationAlgorithm : public IPXTriangulationAlgorithm {
        public:
            IPXHybridTrippleTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons) : IPXTriangulationAlgorithm(beacons, IPXHybridTripple) {}
            const IPXPoint calculationLocation();
            
        private:
            const IPXPoint calculateLocationUsingTripleWithAverage3();
            const IPXPoint calculateLocationUsingTripleWithAverage4();

        };
        
        const IPXPoint IPXHybridTrippleTriangulationAlgorithm::calculationLocation()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            } else {
                const IPXScannedBeacon *b1 = nearestBeacons.at(0);
                const IPXScannedBeacon *b2 = nearestBeacons.at(1);
                
                if (b1->getAccuracy() / b2->getAccuracy() < 0.33) {
                    return calculateLocationUsingTripleWithAverage3();
                } else {
                    return calculateLocationUsingTripleWithAverage4();
                }
            }
            return INVALID_POINT;
        }
        
        const IPXPoint IPXHybridTrippleTriangulationAlgorithm::calculateLocationUsingTripleWithAverage3()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                
                vector<IPXPoint> pointVector;
                for (int i = 0; i < 1; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            IPXPoint p = tripleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<IPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                if (HasPublicBeacon(*(nearestBeacons.at(0)))) {
                    IPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                    return IPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
                }
            }
            return INVALID_POINT;
        }
        
        const IPXPoint IPXHybridTrippleTriangulationAlgorithm::calculateLocationUsingTripleWithAverage4()
        {
            if (nearestBeacons.size() < 3) {
                return calculateBeaconLessThanThree();
            }
            
            if (nearestBeacons.size() >= 3) {
                int count = (int)std::min((int)DEFAULT_NUM_FOR_TRIANGULATION,(int)nearestBeacons.size());
                vector<IPXPoint> pointVector;
                for (int i = 0; i < count; ++i) {
                    for (int j = i + 1; j < count; ++j) {
                        for (int k = j + 1; k < count; ++k) {
                            IPXPoint p = tripleTriangulation(nearestBeacons.at(i), nearestBeacons.at(j), nearestBeacons.at(k));
                            pointVector.insert(pointVector.end(), p);
                        }
                    }
                }
                
                double xSum = 0.0;
                double ySum = 0.0;
                int pointCount = (int) pointVector.size();
                
                vector<IPXPoint>::iterator iter;
                for (iter = pointVector.begin(); iter != pointVector.end(); ++iter) {
                    xSum += iter->getX();
                    ySum += iter->getY();
                }
                
                if (HasPublicBeacon(*(nearestBeacons.at(0)))) {
                    IPXPublicBeacon npb = GetPublicBeacon(*(nearestBeacons.at(0)));
                    return IPXPoint(xSum/pointCount, ySum/pointCount, npb.getLocation().getFloor());
                }
            }
            return INVALID_POINT;
        }

    }
}

IPXTriangulationAlgorithm *CreateTriangulationAlgorithm(const vector<IPXPublicBeacon> &beacons, IPXAlgorithmType type)
{
    switch (type) {
        case IPXSingle:
            return new IPXSingleTriangulationAlgorithm(beacons);
            break;
            
        case IPXTripple:
            return new IPXTrippleTriangulationAlgorithm(beacons);
            break;
            
        case IPXHybridSingle:
            return new IPXHybridSingleTriangulationAlgorithm(beacons);
            break;
            
        case IPXHybridTripple:
            return new IPXHybridTrippleTriangulationAlgorithm(beacons);
            break;
            
        default:
            return NULL;
            break;
    }
    return NULL;
}
