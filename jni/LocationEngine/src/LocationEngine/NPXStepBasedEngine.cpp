//
//  NPXStepBasedEngine.cpp
//  CloudAtlasTestProject
//
//  Created by innerpeacer on 15/2/11.
//  Copyright (c) 2015年 innerpeacer. All rights reserved.
//

#include "NPXStepBasedEngine.h"
#include "../Algorithm/NPXGeometryCalculator.h"

using namespace Nephogram::BLELocationEngine;

ILocationEngine *CreateNPXStepBaseTriangulationEngine(NPXAlgorithmType type)
{
    return new NPXStepBasedEngine(type);
}

void NPXStepBasedEngine::Initilize(const vector<Nephogram::BLELocationEngine::NPXPublicBeacon> &beacons ) {
    if (algorithm) {
        delete algorithm;
    }
    
    algorithm = CreateLocationAlgorithm(beacons, algorithmType);
    
    xMovingAverage = NPXMovingAverage(DefaultMovingAverageWindow);
    yMovingAverage = NPXMovingAverage(DefaultMovingAverageWindow);
    
    stepCount = DefaultStep;
    
    printf("NPXStepBasedTEngine::Initilize OK!");
}


void NPXStepBasedEngine::processBeacons(vector<const Nephogram::BLELocationEngine::NPXScannedBeacon *> &beacons) {
    algorithm->setNearestBeacons(beacons);
    
    printf("NPXStepBasedTEngine: Here OK!");
    
    NPXPoint newLocation = getIndependentLocation();
    
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
            currentDisplayLocation = NPXPoint(xMovingAverage.getAverage(),
                                              yMovingAverage.getAverage(), newLocation.getFloor());
        } else {
            double length = stepCount * DefaultStepLength;
            double distance = NPXPoint::DistanceBetween(newLocation,
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

void NPXStepBasedEngine::addStepEvent() {
    stepCount++;
}

NPXPoint NPXStepBasedEngine::getLocation() const {
    return currentDisplayLocation;
}

NPXPoint NPXStepBasedEngine::getIndependentLocation() {
    NPXPoint currentLocation = algorithm->calculationLocation();
    return currentLocation;
}
