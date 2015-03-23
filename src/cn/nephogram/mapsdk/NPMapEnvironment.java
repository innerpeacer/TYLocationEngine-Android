package cn.nephogram.mapsdk;

import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

/**
 * 
 * @author innerpeacer 地图环境
 */
public class NPMapEnvironment {

	private static final SpatialReference SPATIALR_REFERENCE = SpatialReference
			.create(3395);
	private static UserCredentials userCredentials = null;

	/**
	 * 默认坐标系空间参考
	 * 
	 * @return WKID:3395
	 */
	public static SpatialReference defaultSpatialReference() {
		return SPATIALR_REFERENCE;
	}

	/**
	 * 初始化运行时环境，在调用任何地图SDK方法前调用此方法
	 */
	public static void initMapEnvironment() {
		ArcGISRuntime.setClientId("aBYcI5ED55pNyHQ8");
	}

	/**
	 * 访问地图服务的默认用户验证
	 * 
	 * @return [user:password] --> ["arcgis":"666666"]
	 */
	public static UserCredentials defaultUserCredentials() {
		if (userCredentials == null) {
			userCredentials = new UserCredentials();
			userCredentials.setUserAccount("arcgis", "666666");
		}
		return userCredentials;
	}

}
