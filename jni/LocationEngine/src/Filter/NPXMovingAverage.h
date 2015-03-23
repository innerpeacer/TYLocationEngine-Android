/*
 * NPXMovingAverage.h
 *
 *  Created on: 2014-9-5
 *      Author: innerpeacer
 */

#ifndef NPXMOVINGAVERAGE_H_
#define NPXMOVINGAVERAGE_H_

#include <queue>

namespace Nephogram {
namespace BLELocationEngine {

const int DEFAULT_WINDOW = 10;
    
class NPXMovingAverage {
public:
    NPXMovingAverage()
    {
        window = DEFAULT_WINDOW;
    }
    
	NPXMovingAverage(int w)
    {
        window = w;
    }
    
    void push(double value);
    void clear();
    double getAverage() const;
    
	virtual ~NPXMovingAverage(){}
    
private:
    std::queue<double> doubleQueue;
    int window;
    double average;
};

} /* namespace BLELocationEngine */
} /* namespace Nephogram */
#endif /* NPXMOVINGAVERAGE_H_ */
