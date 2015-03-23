package cn.nephogram.locationengine;

import cn.nephogram.ibeacon.sdk.Utils.Proximity;
import cn.nephogram.locationengine.swig.NPXProximity;

public class NPXProximityConverter {

	public static Proximity fromNPXProximity(NPXProximity pro) {
		Proximity result = Proximity.UNKNOWN;
		switch (pro) {
		case NPXProximityImmediate:
			result = Proximity.IMMEDIATE;
			break;

		case NPXProximityNear:
			result = Proximity.NEAR;
			break;

		case NPXProximityFar:
			result = Proximity.FAR;
			break;

		default:
			result = Proximity.UNKNOWN;
			break;
		}
		return result;
	}

	public static NPXProximity fromProximity(Proximity pro) {
		NPXProximity result = NPXProximity.NPXProximityUnknwon;
		switch (pro) {
		case IMMEDIATE:
			result = NPXProximity.NPXProximityImmediate;
			break;

		case NEAR:
			result = NPXProximity.NPXProximityNear;
			break;

		case FAR:
			result = NPXProximity.NPXProximityFar;
			break;

		default:
			result = NPXProximity.NPXProximityUnknwon;
			break;
		}
		return result;
	}
}
