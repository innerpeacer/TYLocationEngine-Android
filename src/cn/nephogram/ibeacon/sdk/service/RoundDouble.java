package cn.nephogram.ibeacon.sdk.service;

import java.math.BigDecimal;

public class RoundDouble {

	public static double round(int length, double original) {
		BigDecimal decimal = new BigDecimal(original);
		return decimal.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
