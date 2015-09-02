package com.ty.locationengine.ibeacon;

import java.math.BigInteger;

final class IPUnsignedInteger extends Number {

	private static final long serialVersionUID = -3472660809221800562L;

	public static final IPUnsignedInteger ZERO = fromIntBits(0);
	public static final IPUnsignedInteger ONE = fromIntBits(1);
	public static final IPUnsignedInteger MAX_VALUE = fromIntBits(-1);
	private final int value;

	private IPUnsignedInteger(int value) {
		this.value = (value & 0xFFFFFFFF);
	}

	public static IPUnsignedInteger fromIntBits(int bits) {
		return new IPUnsignedInteger(bits);
	}

	public static IPUnsignedInteger valueOf(long value) {
		IPPreconditions
				.checkArgument(
						(value & 0xFFFFFFFF) == value,
						"value (%s) is outside the range for an unsigned integer value",
						new Object[] { Long.valueOf(value) });

		return fromIntBits((int) value);
	}

	public static IPUnsignedInteger valueOf(BigInteger value) {
		IPPreconditions.checkNotNull(value);
		IPPreconditions
				.checkArgument(
						(value.signum() >= 0) && (value.bitLength() <= 32),
						"value (%s) is outside the range for an unsigned integer value",
						new Object[] { value });

		return fromIntBits(value.intValue());
	}

	public static IPUnsignedInteger valueOf(String string) {
		return valueOf(string, 10);
	}

	public static IPUnsignedInteger valueOf(String string, int radix) {
		return fromIntBits(IPUnsignedInts.parseUnsignedInt(string, radix));
	}

	public IPUnsignedInteger plus(IPUnsignedInteger val) {
		return fromIntBits(this.value
				+ ((IPUnsignedInteger) IPPreconditions.checkNotNull(val)).value);
	}

	public IPUnsignedInteger minus(IPUnsignedInteger val) {
		return fromIntBits(this.value
				- ((IPUnsignedInteger) IPPreconditions.checkNotNull(val)).value);
	}

	public IPUnsignedInteger times(IPUnsignedInteger val) {
		return fromIntBits(this.value
				* ((IPUnsignedInteger) IPPreconditions.checkNotNull(val)).value);
	}

	public IPUnsignedInteger dividedBy(IPUnsignedInteger val) {
		return fromIntBits(IPUnsignedInts.divide(this.value,
				((IPUnsignedInteger) IPPreconditions.checkNotNull(val)).value));
	}

	public IPUnsignedInteger mod(IPUnsignedInteger val) {
		return fromIntBits(IPUnsignedInts.remainder(this.value,
				((IPUnsignedInteger) IPPreconditions.checkNotNull(val)).value));
	}

	public int intValue() {
		return this.value;
	}

	public long longValue() {
		return IPUnsignedInts.toLong(this.value);
	}

	public float floatValue() {
		return (float) longValue();
	}

	public double doubleValue() {
		return longValue();
	}

	public BigInteger bigIntegerValue() {
		return BigInteger.valueOf(longValue());
	}

	public int hashCode() {
		return this.value;
	}

	public boolean equals(Object obj) {
		if ((obj instanceof IPUnsignedInteger)) {
			IPUnsignedInteger other = (IPUnsignedInteger) obj;
			return this.value == other.value;
		}
		return false;
	}

	public String toString() {
		return toString(10);
	}

	public String toString(int radix) {
		return IPUnsignedInts.toString(this.value, radix);
	}

}