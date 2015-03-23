package cn.nephogram.ibeacon.sdk.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Messenger;
import android.util.Log;

import cn.nephogram.ibeacon.sdk.ALMovingAverageTD;
import cn.nephogram.ibeacon.sdk.Beacon;
import cn.nephogram.ibeacon.sdk.Region;
import cn.nephogram.ibeacon.sdk.Utils;

class RangingRegion {
	private static final String TAG = (RangingRegion.class.getSimpleName());

	private static final Comparator<Beacon> BEACON_ACCURACY_COMPARATOR = new Comparator<Beacon>() {
		public int compare(Beacon lhs, Beacon rhs) {
			return Double.compare(Utils.computeAccuracy(lhs),
					Utils.computeAccuracy(rhs));
		}

	};
	private final ConcurrentHashMap<Beacon, Long> beacons;
	private ConcurrentHashMap<Beacon, ALMovingAverageTD> beaconsRssi;
	final Region region;
	final Messenger replyTo;

	RangingRegion(Region region, Messenger replyTo) {
		this.region = region;
		this.replyTo = replyTo;
		this.beacons = new ConcurrentHashMap<Beacon, Long>();
		this.beaconsRssi = new ConcurrentHashMap<Beacon, ALMovingAverageTD>();
	}

	public final Collection<Beacon> getSortedBeacons() {
		ArrayList<Beacon> sortedBeacons = new ArrayList<Beacon>(
				this.beacons.keySet());
		Collections.sort(sortedBeacons, BEACON_ACCURACY_COMPARATOR);

		// Log.i(TAG, "getSortedBeacons");

		List<Beacon> averageBeacons = new ArrayList<Beacon>();
		for (Beacon beacon : sortedBeacons) {
			if (beaconsRssi.containsKey(beacon)) {
				Beacon b = new Beacon(beacon.getProximityUUID(),
						beacon.getName(), beacon.getMacAddress(),
						beacon.getMajor(), beacon.getMinor(),
						beacon.getMeasuredPower(), (int) RoundDouble.round(0,
								beaconsRssi.get(beacon).getAverage()));
				// Log.i(TAG, "Averaged Beacon: "
				// + beaconsRssi.get(beacon).getAverage());
				averageBeacons.add(b);

			} else {
				averageBeacons.add(beacon);
			}
		}
		return averageBeacons;
		// return sortedBeacons;
	}

	public final void processFoundBeacons(
			Map<Beacon, Long> beaconsFoundInScanCycle,
			ConcurrentHashMap<Beacon, ALMovingAverageTD> averageRssi) {
		// Log.i(TAG, "processFoundBeacons");
		beaconsRssi = averageRssi;
		for (Entry<Beacon, Long> entry : beaconsFoundInScanCycle.entrySet()) {
			if (Utils.isBeaconInRegion((Beacon) entry.getKey(), this.region)) {
				this.beacons.remove(entry.getKey());
				this.beacons.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public final void removeNotSeenBeacons(long currentTimeMillis) {
		Iterator<Entry<Beacon, Long>> iterator = this.beacons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Beacon, Long> entry = (Entry<Beacon, Long>) iterator.next();
			if (currentTimeMillis - ((Long) entry.getValue()).longValue() > BeaconService.EXPIRATION_MILLIS) {
				Log.v(TAG, "Not seen lately: " + entry.getKey());
				iterator.remove();
			}
		}
	}

}
