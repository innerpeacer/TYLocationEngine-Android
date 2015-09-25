//
//  IPXBeaconDBChecker.cpp
//  BLEProject
//
//  Created by innerpeacer on 15/9/24.
//  Copyright © 2015年 innerpeacer. All rights reserved.
//

#include "IPXBeaconDBChecker.hpp"
#include "BLEMD5.hpp"
#include <sstream>

using namespace Innerpeacer::BLELocationEngine;
using namespace std;

bool checkBeaconDB(std::vector<Innerpeacer::BLELocationEngine::IPXPublicBeacon> beaconArray, std::string code)
{
    if(beaconArray.size() == 0) {
        return false;
    }
    
    long sum = 0;
    int count = (int)beaconArray.size();
    
    vector<IPXPublicBeacon>::iterator iter;
    for (iter = beaconArray.begin(); iter != beaconArray.end(); ++iter) {
        sum += iter->getMajor();
        sum += iter->getMinor();
    }
    sum = sum * count;
    
    stringstream stream;
    stream << sum;
    
    std::string str = stream.str();
//    printf("C String: %s\n", str.c_str());
    
    BLEMD5 md5;
    md5.update(str);
    std::string md5String = md5.toString();
//    printf("md5 String: %s\n", md5String.c_str());

    return (md5String == code);
}