%module TYLocationEngine

%include "std_vector.i"
%include "std_string.i"

%{

#include "LocationEngine/src/Entity/IPXBeacon.h"
#include "LocationEngine/src/Entity/IPXPoint.h"
#include "LocationEngine/src/Entity/IPXPublicBeacon.h"
#include "LocationEngine/src/Entity/IPXScannedBeacon.h"

#include "LocationEngine/src/LocationEngine/ILocationEngine.h"

using namespace std;
using namespace Innerpeacer::BLELocationEngine;

%}

typedef int uint16_t;
%include "enums.swg"

%include "LocationEngine/swig/IPEntity.i"
%include "LocationEngine/swig/ILocationEngine.i"
%include "LocationEngine/swig/IPAlgorithm.i"
