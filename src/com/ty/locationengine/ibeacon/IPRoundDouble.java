package com.ty.locationengine.ibeacon;

import java.math.BigDecimal;

class IPRoundDouble {

	public static double round(int length, double original) {
		BigDecimal decimal = new BigDecimal(original);
		return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
