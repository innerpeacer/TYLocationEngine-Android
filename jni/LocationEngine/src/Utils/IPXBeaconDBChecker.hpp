//
//  IPXBeaconDBChecker.hpp
//  BLEProject
//
//  Created by innerpeacer on 15/9/24.
//  Copyright © 2015年 innerpeacer. All rights reserved.
//

#ifndef IPXBeaconDBChecker_hpp
#define IPXBeaconDBChecker_hpp

#include <stdio.h>
#include "../Entity/IPXPublicBeacon.h"
#include <vector>

bool checkBeaconDB(std::vector<Innerpeacer::BLELocationEngine::IPXPublicBeacon> beaconArray, std::string code);

#endif /* IPXBeaconDBChecker_hpp */
