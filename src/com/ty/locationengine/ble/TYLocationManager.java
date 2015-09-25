package com.ty.locationengine.ble;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.ty.locationengine.ble.IPXLocationEngine.IPXLocationEngineListener;
import com.ty.locationengine.ibeacon.BeaconRegion;
import com.ty.mapdata.TYBuilding;
import com.ty.mapdata.TYLocalPoint;
import com.ty.mapsdk.TYMapEnvironment;

/**
 * 定位引擎类
 * 
 * @author innerpeacer
 * 
 */
public class TYLocationManager implements IPXLocationEngineListener {
	static final String TAG = TYLocationManager.class.getSimpleName();

	private static final double DEFAULT_REQUEST_TIME_OUT = 4.0f;
	private static final long DEFAULT_CHECK_INERVAL = 1000;

	private double requestTimeOut = DEFAULT_REQUEST_TIME_OUT;

	private IPXLocationEngine locationEngine;
	private BeaconRegion beaconRegion;
	private String beaconPath;

	private TYLocalPoint lastLocation;

	private boolean isLocating;

	long lastTimeLocationUpdated;
	private Handler locationUpdatingCheckerHandler = new Handler();
	private Runnable locationUpdatingCheckerRunnable = new Runnable() {

		@Override
		public void run() {
			if (isLocating) {
				long now = System.currentTimeMillis();
				if (now - lastTimeLocationUpdated > (long) (requestTimeOut * 1000)) {
					notifyFailUpdateLocation();
				}
				locationUpdatingCheckerHandler.postDelayed(
						locationUpdatingCheckerRunnable, DEFAULT_CHECK_INERVAL);
			}
		}
	};

	// TYLocationManager(Context context, String beaconDBPath) {
	// this.beaconPath = beaconDBPath;
	// locationEngine = new IPXLocationEngine(context, beaconPath);
	// locationEngine.addLocationEngineListener(this);
	// }

	/**
	 * 定位引擎构造方法
	 * 
	 * @param context
	 *            上下文环境
	 * @param building
	 *            目标建筑
	 */
	public TYLocationManager(Context context, TYBuilding building) {
		// if (!building.getBuildingID().equalsIgnoreCase("00210100")) {
		// return;
		// }

		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date invalidDate = dateFormat.parse("2017-10-11");

			Date now = new Date();
			boolean isInvalid = now.after(invalidDate);
			if (isInvalid) {
				return;
			}
		} catch (ParseException e) {
			return;
		}

		String dbName = String.format("%s_Beacon.db", building.getBuildingID());
		this.beaconPath = new File(
				TYMapEnvironment.getDirectoryForBuilding(building), dbName)
				.toString();
		locationEngine = new IPXLocationEngine(context, beaconPath);
		locationEngine.addLocationEngineListener(this);
	}

	/**
	 * 开启定位引擎
	 */
	public void startUpdateLocation() {
		if (isLocating) {
			return;
		}

		Log.i(TAG, "startUpdateLocation");

		isLocating = true;
		locationEngine.start();

		locationUpdatingCheckerHandler.postDelayed(
				locationUpdatingCheckerRunnable, DEFAULT_CHECK_INERVAL);
		lastTimeLocationUpdated = System.currentTimeMillis();
	}

	/**
	 * 停止定位引擎
	 */
	public void stopUpdateLocation() {
		if (!isLocating) {
			return;
		}

		isLocating = false;
		locationEngine.stop();
	}

	/**
	 * 获取最近一次的位置信息
	 * 
	 * @return 位置点
	 */
	public TYLocalPoint getLastLocation() {
		return lastLocation;
	}

	// public NPLocalPoint getDirectioLocation() {
	//
	// }

	/**
	 * 设置是否限制定位使用的beacon个数
	 * 
	 * @param limitBeaconNumber
	 *            是否限制
	 */
	public void setLimitBeaconNumber(boolean limitBeaconNumber) {
		// this.limitBeaconNumber = limitBeaconNumber;
		locationEngine.setLimitBeaconNumber(limitBeaconNumber);
	}

	/**
	 * 设置用于定位的beacon最大个数
	 * 
	 * @param maxBeaconNumberForProcessing
	 *            用于定位的beacon最大个数，即选取不多于mbn个beacon进行定位
	 */
	public void setMaxBeaconNumberForProcessing(int maxBeaconNumberForProcessing) {
		// this.maxBeaconNumberForProcessing = maxBeaconNumberForProcessing;
		locationEngine
				.setMaxBeaconNumberForProcessing(maxBeaconNumberForProcessing);
	}

	/**
	 * 设置beacon信号阈值
	 * 
	 * @param threshold
	 *            低于此信号阈值将忽略beacon信号
	 */
	public void setRssiThreshold(int threshold) {
		locationEngine.setRssiThreshold(threshold);
	}

	@Override
	public void locationChanged(TYLocalPoint lp) {
		lastTimeLocationUpdated = System.currentTimeMillis();

		if (lp.getFloor() == 0) {
			if (lastLocation == null) {
				lp.setFloor(1);
			} else {
				lp.setFloor(lastLocation.getFloor());
			}
		}

		notifyLocationUpdated(lp);
		lastLocation = lp;
	}

	@Override
	public void headingChanged(double newHeading) {
		notifyHeadingUpdated(newHeading);
	}

	/**
	 * 设置用于定位的Beacon参数
	 * 
	 * @param region
	 *            Beacon Region参数
	 */
	public void setBeaconRegion(BeaconRegion region) {
		beaconRegion = region;
		locationEngine.setBeaconRegion(beaconRegion);
	}

	/**
	 * 设置定位请求超时时间，即超过此时间没有结果返回认为定位失败
	 * 
	 * @param rt
	 *            超时时间
	 */
	public void setRequestTimeOut(double rt) {
		this.requestTimeOut = rt;
	}

	/**
	 * 获取定位请求超时时间
	 * 
	 * @return 超时时间
	 */
	public double getRequestTimeOut() {
		return requestTimeOut;
	}

	/**
	 * 定位引擎监听回调接口
	 * 
	 * @author innerpeacer
	 * 
	 */
	public interface TYLocationManagerListener {

		/**
		 * 位置更新事件回调，位置更新并返回新的位置结果
		 * 
		 * @param locationManager
		 *            定位引擎实例
		 * @param lp
		 *            新的位置结果
		 */
		void didUpdateLocation(TYLocationManager locationManager,
				TYLocalPoint lp);

		/**
		 * 位置更新失败事件回调
		 * 
		 * @param locationManager
		 *            定位引擎实例
		 */
		void didFailUpdateLocation(TYLocationManager locationManager);

		/**
		 * 设备方向改变事件回调
		 * 
		 * @param locationManager
		 *            定位引擎实例
		 * @param newHeading
		 *            新的设备方向结果
		 */
		void didUpdateDeviceHeading(TYLocationManager locationManager,
				double newHeading);
	}

	private List<TYLocationManagerListener> locationListeners = new ArrayList<TYLocationManagerListener>();

	/**
	 * 添加定位引擎回调监听器
	 * 
	 * @param listener
	 *            回调监听
	 */
	public void addLocationEngineListener(TYLocationManagerListener listener) {
		if (!locationListeners.contains(listener)) {
			locationListeners.add(listener);
		}
	}

	/**
	 * 移除定位引擎回调监听器
	 * 
	 * @param listener
	 *            回调监听
	 */
	public void removeLocationEngineListener(TYLocationManagerListener listener) {
		if (locationListeners.contains(listener)) {
			locationListeners.remove(listener);
		}
	}

	private void notifyLocationUpdated(TYLocalPoint lp) {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didUpdateLocation(this, lp);
		}
	}

	private void notifyFailUpdateLocation() {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didFailUpdateLocation(this);
		}
	}

	private void notifyHeadingUpdated(double newHeading) {
		for (TYLocationManagerListener listener : locationListeners) {
			listener.didUpdateDeviceHeading(this, newHeading);
		}
	}
}
