package com.ty.locationengine.ble;

import com.ty.locationengine.ibeacon.BeaconUtils.Proximity;
import com.ty.locationengine.swig.IPXProximity;

class IPXProximityConverter {

	public static Proximity fromNPXProximity(IPXProximity pro) {
		Proximity result = Proximity.UNKNOWN;
		switch (pro) {
		case IPXProximityImmediate:
			result = Proximity.IMMEDIATE;
			break;

		case IPXProximityNear:
			result = Proximity.NEAR;
			break;

		case IPXProximityFar:
			result = Proximity.FAR;
			break;

		default:
			result = Proximity.UNKNOWN;
			break;
		}
		return result;
	}

	public static IPXProximity fromProximity(Proximity pro) {
		IPXProximity result = IPXProximity.IPXProximityUnknwon;
		switch (pro) {
		case IMMEDIATE:
			result = IPXProximity.IPXProximityImmediate;
			break;

		case NEAR:
			result = IPXProximity.IPXProximityNear;
			break;

		case FAR:
			result = IPXProximity.IPXProximityFar;
			break;

		default:
			result = IPXProximity.IPXProximityUnknwon;
			break;
		}
		return result;
	}
}
