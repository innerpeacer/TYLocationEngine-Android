package cn.nephogram.locationengine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.nephogram.data.NPLocalPoint;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NPBeaconDBAdapter {
	static final String TAG = NPBeaconDBAdapter.class.getSimpleName();

	private static final String TABLE_BEACON = "beacon";
	private static final String FIELD_BEACON_GEOM = "geom";
	private static final String FIELD_BEACON_UUID = "uuid";
	private static final String FIELD_BEACON_MAJOR = "major";
	private static final String FIELD_BEACON_MINOR = "minor";
	private static final String FIELD_BEACON_FLOOR = "floor";
	// private static final String FIELD_BEACON_TAG = "tag";

	final Context context;
	private SQLiteDatabase db;
	private File dbFile;

	public NPBeaconDBAdapter(Context ctx, String path) {
		this.context = ctx;
		dbFile = new File(path);
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

	public List<NPPublicBeacon> getAllNephogramBeacons() {
		List<NPPublicBeacon> allBeacons = new ArrayList<NPPublicBeacon>();

		String[] columns = new String[] { FIELD_BEACON_GEOM, FIELD_BEACON_UUID,
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR, FIELD_BEACON_FLOOR };
		Cursor c = db.query(true, TABLE_BEACON, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				byte[] coordByte = c.getBlob(0);
				NPPoint3D point = NPPointConverter.pointFromData(coordByte);
				String uuid = c.getString(1);
				int major = c.getInt(2);
				int minor = c.getInt(3);
				int floor = c.getInt(4);

				NPPublicBeacon pb = new NPPublicBeacon(uuid, major, minor,
						null, null);
				pb.setLocation(new NPLocalPoint(point.getX(), point.getY(),
						floor));
				allBeacons.add(pb);
			} while (c.moveToNext());
		}

		return allBeacons;
	}

	public NPPublicBeacon getNephogramBeacon(int major, int minor) {
		NPPublicBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_GEOM, FIELD_BEACON_UUID,
				FIELD_BEACON_FLOOR };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };
		Cursor c = db.query(true, TABLE_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			byte[] coordByte = c.getBlob(0);
			NPPoint3D point = NPPointConverter.pointFromData(coordByte);
			String uuid = c.getString(1);
			int floor = c.getInt(2);

			beacon = new NPPublicBeacon(uuid, major, minor, null, null);
			beacon.setLocation(new NPLocalPoint(point.getX(), point.getY(),
					floor));
		}
		return beacon;
	}

}
