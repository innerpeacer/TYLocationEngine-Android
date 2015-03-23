/*
 * NPXMovingAverage.cpp
 *
 *  Created on: 2014-9-5
 *      Author: innerpeacer
 */

#include "NPXMovingAverage.h"

namespace Nephogram {
namespace BLELocationEngine {

void NPXMovingAverage::push(double value) {
    int size = (int)doubleQueue.size();
    
    double sum = average * size;
    
    if (size == window) {
        double tmp = doubleQueue.front();
        doubleQueue.pop();
        
        sum -= tmp;
        size--;
    }
    
    average = (sum + value) / (size + 1);
    doubleQueue.push(value);
}

double NPXMovingAverage::getAverage() const {
	return average;
}

void NPXMovingAverage::clear() {
    while (!doubleQueue.empty()) {
        doubleQueue.pop();
    }
    average = 0.0;
}

} /* namespace BLELocationEngine */
} /* namespace Nephogram */
