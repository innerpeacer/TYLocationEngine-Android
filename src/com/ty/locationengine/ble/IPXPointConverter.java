package com.ty.locationengine.ble;

class IPXPointConverter {
	static String TAG = "NPPointConverter";

	public static IPXPoint3D pointFromData(byte[] data) {

		double x = arr2double(data, 5);
		double y = arr2double(data, 13);
		double z = arr2double(data, 21);

		return new IPXPoint3D(x, y, z);
	}

	public static byte[] dataFromPoint3D(IPXPoint3D point) {
		byte[] data = new byte[29];
		data[1] = data[0] = 1;
		data[3] = data[2] = 0;
		data[4] = -128;

		byte[] dx = getBytes(point.getX());
		byte[] dy = getBytes(point.getY());
		byte[] dz = getBytes(point.getZ());

		System.arraycopy(dx, 0, data, 5, 8);
		System.arraycopy(dy, 0, data, 13, 8);
		System.arraycopy(dz, 0, data, 21, 8);

		return data;
	}

	public static int bytesToInt2(byte[] src, int offset) {
		int value;
		value = (int) (((src[offset] & 0xFF) << 24)
				| ((src[offset + 1] & 0xFF) << 16)
				| ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
		return value;
	}

	public static double arr2double(byte[] arr, int start) {
		int i = 0;
		int len = 8;
		int cnt = 0;
		byte[] tmp = new byte[len];
		for (i = start; i < (start + len); i++) {
			tmp[cnt] = arr[i];
			cnt++;
		}
		long accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
			accum |= ((long) (tmp[i] & 0xff)) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}

	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}

	public static byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data >> 8) & 0xff);
		bytes[2] = (byte) ((data >> 16) & 0xff);
		bytes[3] = (byte) ((data >> 24) & 0xff);
		bytes[4] = (byte) ((data >> 32) & 0xff);
		bytes[5] = (byte) ((data >> 40) & 0xff);
		bytes[6] = (byte) ((data >> 48) & 0xff);
		bytes[7] = (byte) ((data >> 56) & 0xff);
		return bytes;
	}
}
