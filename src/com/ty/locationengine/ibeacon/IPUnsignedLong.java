package com.ty.locationengine.ibeacon;

import java.math.BigInteger;

public final class IPUnsignedLong extends Number {

	private static final long serialVersionUID = 4976610486759007623L;

	public static final long UNSIGNED_MASK = 9223372036854775807L;
	public static final IPUnsignedLong ZERO = new IPUnsignedLong(0L);
	public static final IPUnsignedLong ONE = new IPUnsignedLong(1L);
	public static final IPUnsignedLong MAX_VALUE = new IPUnsignedLong(-1L);
	private final long value;

	private IPUnsignedLong(long value) {
		this.value = value;
	}

	public static IPUnsignedLong fromLongBits(long bits) {
		return new IPUnsignedLong(bits);
	}

	public static IPUnsignedLong valueOf(long value) {
		IPPreconditions.checkArgument(value >= 0L,
				"value (%s) is outside the range for an unsigned long value",
				new Object[] { Long.valueOf(value) });

		return fromLongBits(value);
	}

	public static IPUnsignedLong valueOf(BigInteger value) {
		IPPreconditions.checkNotNull(value);
		IPPreconditions.checkArgument(
				(value.signum() >= 0) && (value.bitLength() <= 64),
				"value (%s) is outside the range for an unsigned long value",
				new Object[] { value });

		return fromLongBits(value.longValue());
	}

	public IPUnsignedLong plus(IPUnsignedLong val) {
		return fromLongBits(this.value
				+ ((IPUnsignedLong) IPPreconditions.checkNotNull(val)).value);
	}

	public IPUnsignedLong minus(IPUnsignedLong val) {
		return fromLongBits(this.value
				- ((IPUnsignedLong) IPPreconditions.checkNotNull(val)).value);
	}

	public IPUnsignedLong times(IPUnsignedLong val) {
		return fromLongBits(this.value
				* ((IPUnsignedLong) IPPreconditions.checkNotNull(val)).value);
	}

	public int intValue() {
		return (int) this.value;
	}

	public long longValue() {
		return this.value;
	}

	public float floatValue() {
		float fValue = (float) (this.value & 0xFFFFFFFF);
		if (this.value < 0L) {
			fValue += 9.223372E+018F;
		}
		return fValue;
	}

	public double doubleValue() {
		double dValue = this.value & 0xFFFFFFFF;
		if (this.value < 0L) {
			dValue += 9.223372036854776E+018D;
		}
		return dValue;
	}

	public BigInteger bigIntegerValue() {
		BigInteger bigInt = BigInteger.valueOf(this.value & 0xFFFFFFFF);
		if (this.value < 0L) {
			bigInt = bigInt.setBit(63);
		}
		return bigInt;
	}

	public int hashCode() {
		return (int) (this.value ^ this.value >>> 32);
	}

	public boolean equals(Object obj) {
		if ((obj instanceof IPUnsignedLong)) {
			IPUnsignedLong other = (IPUnsignedLong) obj;
			return this.value == other.value;
		}
		return false;
	}

	public String toString() {
		return "Not correct: " + this.value;
	}

	public String toString(int radix) {
		return "not correct" + this.value + "  radix:" + this.value;
	}

}
