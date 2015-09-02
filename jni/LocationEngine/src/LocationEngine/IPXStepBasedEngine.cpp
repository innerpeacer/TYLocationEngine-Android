//
//  IPXStepBasedEngine.cpp
//  BLEProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "IPXStepBasedEngine.h"
#include "../Algorithm/IPXGeometryCalculator.h"

using namespace Innerpeacer::BLELocationEngine;

ILocationEngine *CreateIPXStepBaseTriangulationEngine(IPXAlgorithmType type)
{
    return new IPXStepBasedEngine(type);
}

void IPXStepBasedEngine::Initilize(const vector<Innerpeacer::BLELocationEngine::IPXPublicBeacon> &beacons ) {
    if (algorithm) {
        delete algorithm;
    }
    
    algorithm = CreateLocationAlgorithm(beacons, algorithmType);
    
    xMovingAverage = IPXMovingAverage(DefaultMovingAverageWindow);
    yMovingAverage = IPXMovingAverage(DefaultMovingAverageWindow);
    
    stepCount = DefaultStep;
    
    printf("IPXStepBasedTEngine::Initilize OK!");
}


void IPXStepBasedEngine::processBeacons(vector<const Innerpeacer::BLELocationEngine::IPXScannedBeacon *> &beacons) {
    algorithm->setNearestBeacons(beacons);
    
//    printf("IPXStepBasedTEngine: Here OK!");
    
    IPXPoint newLocation = getIndependentLocation();
    
    if (newLocation == INVALID_POINT) {
        return;
    }
    
    if (currentAnchorLocation == INVALID_POINT) {
        currentAnchorLocation = newLocation;
        currentDisplayLocation = newLocation;
        xMovingAverage.clear();
        yMovingAverage.clear();
        xMovingAverage.push(newLocation.getX());
        yMovingAverage.push(newLocation.getY());
    } else {
        if (stepCount == DefaultStep) {
            xMovingAverage.push(newLocation.getX());
            yMovingAverage.push(newLocation.getY());
            currentDisplayLocation = IPXPoint(xMovingAverage.getAverage(),
                                              yMovingAverage.getAverage(), newLocation.getFloor());
        } else {
            double length = stepCount * DefaultStepLength;
            double distance = IPXPoint::DistanceBetween(newLocation,
                                                        currentAnchorLocation);
            
            if (distance < length) {
                currentAnchorLocation = newLocation;
                currentDisplayLocation = newLocation;
                stepCount = DefaultStep;
                xMovingAverage.clear();
                yMovingAverage.clear();
            } else {
                currentDisplayLocation = scalePointWithCenter(currentAnchorLocation, newLocation, length);
            }
        }
    }
}

void IPXStepBasedEngine::addStepEvent() {
    stepCount++;
}

void IPXStepBasedEngine::reset()
{
    stepCount = DefaultStep;
    xMovingAverage.clear();
    yMovingAverage.clear();
    currentAnchorLocation = INVALID_POINT;
    currentDisplayLocation = INVALID_POINT;
}

IPXPoint IPXStepBasedEngine::getLocation() const {
    return currentDisplayLocation;
}

IPXPoint IPXStepBasedEngine::getIndependentLocation() {
    IPXPoint currentLocation = algorithm->calculationLocation();
    return currentLocation;
}
