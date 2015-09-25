package com.ty.locationengine.ble;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ty.mapdata.TYLocalPoint;

class TYBeaconDBAdapter {
	static final String TAG = TYBeaconDBAdapter.class.getSimpleName();

	private static final String TABLE_BEACON = "beacon";
	private static final String TABLE_CODE = "Code";

	private static final String FIELD_BEACON_GEOM = "geom";
	private static final String FIELD_BEACON_UUID = "uuid";
	private static final String FIELD_BEACON_MAJOR = "major";
	private static final String FIELD_BEACON_MINOR = "minor";
	private static final String FIELD_BEACON_FLOOR = "floor";
	// private static final String FIELD_BEACON_TAG = "tag";

	private static final String FIELD_CODE = "Code";

	final Context context;
	private SQLiteDatabase db;
	private File dbFile;

	public TYBeaconDBAdapter(Context ctx, String path) {
		this.context = ctx;
		dbFile = new File(path);
		Log.i(TAG, path);
		db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
	}

	public void open() {
		if (!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		}
	}

	public void close() {
		if (db.isOpen()) {
			db.close();
		}
	}

	public List<TYPublicBeacon> getAllLocationingBeacons() {
		List<TYPublicBeacon> allBeacons = new ArrayList<TYPublicBeacon>();

		String[] columns = new String[] { FIELD_BEACON_GEOM, FIELD_BEACON_UUID,
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR, FIELD_BEACON_FLOOR };
		Cursor c = db.query(true, TABLE_BEACON, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				byte[] coordByte = c.getBlob(0);
				IPXPoint3D point = IPXPointConverter.pointFromData(coordByte);
				String uuid = c.getString(1);
				int major = c.getInt(2);
				int minor = c.getInt(3);
				int floor = c.getInt(4);

				TYPublicBeacon pb = new TYPublicBeacon(uuid, major, minor,
						null, null);
				pb.setLocation(new TYLocalPoint(point.getX(), point.getY(),
						floor));
				allBeacons.add(pb);
			} while (c.moveToNext());
		}
		c.close();

		return allBeacons;
	}

	public TYPublicBeacon getAllLocationingBeacon(int major, int minor) {
		TYPublicBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_GEOM, FIELD_BEACON_UUID,
				FIELD_BEACON_FLOOR };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };
		Cursor c = db.query(true, TABLE_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			byte[] coordByte = c.getBlob(0);
			IPXPoint3D point = IPXPointConverter.pointFromData(coordByte);
			String uuid = c.getString(1);
			int floor = c.getInt(2);

			beacon = new TYPublicBeacon(uuid, major, minor, null, null);
			beacon.setLocation(new TYLocalPoint(point.getX(), point.getY(),
					floor));
		}
		return beacon;
	}

	public String getCode() {
		String code = null;
		String[] columns = new String[] { FIELD_CODE };
		Cursor c = db.query(true, TABLE_CODE, columns, null, null, null, null,
				null, null, null);
		if (c != null && c.moveToFirst()) {
			code = c.getString(0);
		}
		return code;
	}
}
