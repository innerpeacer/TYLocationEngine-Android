%module BLELocationEngine

%include "std_vector.i"
%include "std_string.i"

%{

#include "LocationEngine/src/Entity/NPXBeacon.h"
#include "LocationEngine/src/Entity/NPXPoint.h"
#include "LocationEngine/src/Entity/NPXPublicBeacon.h"
#include "LocationEngine/src/Entity/NPXScannedBeacon.h"

#include "LocationEngine/src/LocationEngine/ILocationEngine.h"

using namespace std;
using namespace Nephogram::BLELocationEngine;

%}

typedef int uint16_t;
%include "enums.swg"

%include "LocationEngine/swig/Entity.i"
%include "LocationEngine/swig/LocationEngine.i"
%include "LocationEngine/swig/Algorithm.i"
