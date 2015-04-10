package cn.nephogram.mapsdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import cn.nephogram.data.NPLocalPoint;
import cn.nephogram.datamanager.NPAssetsManager;
import cn.nephogram.datamanager.NPFileManager;
import cn.nephogram.mapsdk.data.NPMapInfo;
import cn.nephogram.mapsdk.layer.NPAssetLayer;
import cn.nephogram.mapsdk.layer.NPFacilityLayer;
import cn.nephogram.mapsdk.layer.NPFloorLayer;
import cn.nephogram.mapsdk.layer.NPLabelLayer;
import cn.nephogram.mapsdk.layer.NPLocationLayer;
import cn.nephogram.mapsdk.layer.NPRoomLayer;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.MarkerSymbol;

public class NPMapView extends MapView implements OnSingleTapListener,
		OnPanListener, OnZoomListener {

	private static final long serialVersionUID = 7475014088599931549L;

	static final String TAG = NPMapView.class.getSimpleName();

	private static final int DEFAULT_TOLERANCE = 5;

	private Context context;

	private String buildingID;
	private NPMapInfo currentMapInfo;

	private NPFloorLayer floorLayer;
	private NPRoomLayer roomLayer;
	private GraphicsLayer roomHighlightLayer;
	private NPAssetLayer assetLayer;

	private NPFacilityLayer facilityLayer;
	private NPLabelLayer labelLayer;

	private NPLocationLayer locationLayer;

	private Envelope initialEnvelope;

	private NPMapViewMode mapViewMode = NPMapViewMode.NPMapViewModeDefault;
	private double currentDeviceHeading = 0;
	/**
	 * 是否从assets目录读取地图文件，默认为true
	 */
	public static boolean useAsset = true;

	private List<NPMapViewListenser> listeners = new ArrayList<NPMapView.NPMapViewListenser>();

	private boolean highlightPoiOnSelection = false;

	// =====================================
	public NPMapView(Context context) {
		super(context);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(0x88888888, Color.TRANSPARENT, 0, 0);
	}

	public NPMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setAllowRotationByPinch(true);
		setMapBackground(0x88888888, Color.TRANSPARENT, 0, 0);
	}

	/**
	 * 地图初始化方法
	 * 
	 * @param renderingScheme
	 *            地图渲染方案
	 */
	public void init(NPRenderingScheme renderingScheme) {
		// Log.i(TAG, "init");
		SpatialReference sr = NPMapEnvironment.defaultSpatialReference();

		floorLayer = new NPFloorLayer(context, renderingScheme, sr, null);
		addLayer(floorLayer);

		roomLayer = new NPRoomLayer(context, renderingScheme, sr, null);
		addLayer(roomLayer);

		roomHighlightLayer = new GraphicsLayer();
		roomHighlightLayer.setRenderer(new SimpleRenderer(renderingScheme
				.getDefaultHighlightFillSymbol()));
		addLayer(roomHighlightLayer);
		// roomLayer.setSelectionColor(renderingScheme
		// .getDefaultHighlightFillSymbol().getColor());
		// roomLayer.setSelectionColorWidth(2);

		assetLayer = new NPAssetLayer(context, renderingScheme, sr, null);
		addLayer(assetLayer);

		facilityLayer = new NPFacilityLayer(context, renderingScheme, sr, null);
		addLayer(facilityLayer);

		labelLayer = new NPLabelLayer(context, sr, null);
		addLayer(labelLayer);

		locationLayer = new NPLocationLayer();
		addLayer(locationLayer);

		setOnSingleTapListener(this);
		setOnPanListener(this);
		setOnZoomListener(this);
	}

	/**
	 * 切换楼层方法
	 * 
	 * @param info
	 *            目标楼层的地图信息
	 */
	public void setFloor(final NPMapInfo info) {
		if (currentMapInfo != null
				&& info.getMapID().equalsIgnoreCase(currentMapInfo.getMapID())) {
			return;
		}

		currentMapInfo = info;

		floorLayer.removeAll();
		roomLayer.removeAll();
		roomHighlightLayer.removeAll();
		assetLayer.removeAll();
		facilityLayer.removeAll();
		labelLayer.removeAll();
		locationLayer.removeAll();

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (useAsset) {
					floorLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
							.getFloorFilePath(info.getMapID()));
					roomLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
							.getRoomFilePath(info.getMapID()));
					assetLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
							.getAssetFilePath(info.getMapID()));
					facilityLayer
							.loadContentsFromAssetsWithInfo(NPAssetsManager
									.getFacilityFilePath(info.getMapID()));
					labelLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
							.getLabelFilePath(info.getMapID()));
				} else {
					floorLayer.loadContentsFromFileWithInfo(NPFileManager
							.getFloorFilePath(info.getMapID()));
					roomLayer.loadContentsFromFileWithInfo(NPFileManager
							.getRoomFilePath(info.getMapID()));
					assetLayer.loadContentsFromFileWithInfo(NPFileManager
							.getAssetFilePath(info.getMapID()));
					facilityLayer.loadContentsFromFileWithInfo(NPFileManager
							.getFacilityFilePath(info.getMapID()));
					labelLayer.loadContentsFromFileWithInfo(NPFileManager
							.getLabelFilePath(info.getMapID()));
				}

				if (initialEnvelope == null) {
					initialEnvelope = new Envelope(info.getMapExtent()
							.getXmin(), info.getMapExtent().getYmin(), info
							.getMapExtent().getXmax(), info.getMapExtent()
							.getYmax());
					setExtent(initialEnvelope);

					// setMaxResolution(info.getMapSize().getX() * 1.5
					// / (720 / 2.0));
					// setMaxResolution(info.getMapSize().getX() * 1.5 / (720));
					double width = 0.06; // 6cm
					setMinScale(info.getMapSize().getX() / width);
					setMaxScale(6 / width);
				}

				boolean labelVisible = getScale() < DEFAULT_SCALE_THRESHOLD;
				labelLayer.setVisible(labelVisible);

				notifyFinishLoadingFloor(NPMapView.this, currentMapInfo);
			}
		}).start();

		// if (useAsset) {
		// floorLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
		// .getFloorFilePath(info.getMapID()));
		// roomLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
		// .getRoomFilePath(info.getMapID()));
		// assetLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
		// .getAssetFilePath(info.getMapID()));
		// facilityLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
		// .getFacilityFilePath(info.getMapID()));
		// labelLayer.loadContentsFromAssetsWithInfo(NPAssetsManager
		// .getLabelFilePath(info.getMapID()));
		// } else {
		// floorLayer.loadContentsFromFileWithInfo(NPFileManager
		// .getFloorFilePath(info.getMapID()));
		// roomLayer.loadContentsFromFileWithInfo(NPFileManager
		// .getRoomFilePath(info.getMapID()));
		// assetLayer.loadContentsFromFileWithInfo(NPFileManager
		// .getAssetFilePath(info.getMapID()));
		// facilityLayer.loadContentsFromFileWithInfo(NPFileManager
		// .getFacilityFilePath(info.getMapID()));
		// labelLayer.loadContentsFromFileWithInfo(NPFileManager
		// .getLabelFilePath(info.getMapID()));
		// }
		//
		// if (initialEnvelope == null) {
		// initialEnvelope = new Envelope(info.getMapExtent().getXmin(), info
		// .getMapExtent().getYmin(), info.getMapExtent().getXmax(),
		// info.getMapExtent().getYmax());
		// setExtent(initialEnvelope);
		//
		// }
		//
		// notifyFinishLoadingFloor(this, currentMapInfo);

		// Envelope envelope = new Envelope(info.getMapExtent().getXmin(), info
		// .getMapExtent().getYmin(), info.getMapExtent().getXmax(), info
		// .getMapExtent().getYmax());
		// setExtent(envelope);
	}

	/**
	 * 当前显示的建筑ID
	 */
	public String getBuildingID() {
		return buildingID;
	}

	/**
	 * 当前显示的建筑ID
	 */
	public void setBuildingID(String buildingID) {
		this.buildingID = buildingID;
	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public NPMapInfo getCurrentMapInfo() {
		return currentMapInfo;
	}

	/**
	 * 当前建筑的当前楼层信息
	 */
	public void setCurrentMapInfo(NPMapInfo currentMapInfo) {
		this.currentMapInfo = currentMapInfo;
	}

	public void setLocationSymbol(MarkerSymbol markerSymbol) {
		locationLayer.setLcoationSymbol(markerSymbol);
	}

	public void showLocation(NPLocalPoint location) {
		locationLayer.removeAll();
		if (currentMapInfo.getFloorNumber() == location.getFloor()) {
			Point pos = new Point(location.getX(), location.getY());
			locationLayer.showLocation(pos, currentDeviceHeading,
					currentMapInfo.getInitAngle(), mapViewMode);
		}
	}

	public void removeLocation() {
		locationLayer.removeLocation();
	}

	public void processDeviceRotation(double newHeading) {
		currentDeviceHeading = newHeading;
		locationLayer.updateDeviceHeading(newHeading,
				currentMapInfo.getInitAngle(), mapViewMode);

		switch (mapViewMode) {
		case NPMapViewModeDefault:

			break;

		case NPMapViewModeFollowing:
			setRotationAngle(currentMapInfo.getInitAngle()
					+ currentDeviceHeading, false);
			break;

		default:
			break;
		}
	}

	/**
	 * 在POI被点选时是否高亮显示，默认为NO
	 */
	public boolean isHighlightPoiOnSelection() {
		return highlightPoiOnSelection;
	}

	/**
	 * 在POI被点选时是否高亮显示，默认为NO
	 */
	public void setHighlightPoiOnSelection(boolean highlightPoiOnSelection) {
		this.highlightPoiOnSelection = highlightPoiOnSelection;
	}

	/**
	 * 清除高亮显示的POI
	 */
	public void clearSelection() {
		roomLayer.clearSelection();
		roomHighlightLayer.removeAll();
		// assetLayer.clearSelection();
		facilityLayer.clearSelection();
	}

	public void translateInScreenUnit(double x, double y, boolean animated) {
		Point centerScreen = toScreenPoint(getCenter());
		Point newCenterScreen = new Point(centerScreen.getX() - x,
				centerScreen.getY() - y);
		Point newCenter = toMapPoint(newCenterScreen);
		centerAt(newCenter, animated);
	}

	public void translateInMapUnit(double x, double y, boolean animated) {
		Point center = getCenter();
		Point newCenter = new Point(center.getX() - x, center.getY() - y);
		centerAt(newCenter, animated);
	}

	public void restrictLocation(Point location, Rect range, boolean animated) {
		Point locationOnScreen = toScreenPoint(location);

		if (range.contains((int) locationOnScreen.getX(),
				(int) locationOnScreen.getY())) {
			return;
		}

		double xOffset = 0;
		double yOffset = 0;

		if (locationOnScreen.getX() < range.left) {
			xOffset = range.left - locationOnScreen.getX();
		}

		if (locationOnScreen.getX() > range.right) {
			xOffset = range.right - locationOnScreen.getX();
		}

		if (locationOnScreen.getY() < range.bottom) {
			yOffset = range.bottom - locationOnScreen.getY();
		}

		if (locationOnScreen.getY() > range.top) {
			yOffset = range.top - locationOnScreen.getY();
		}

		translateInScreenUnit(xOffset, yOffset, animated);
	}

	public void setMapMode(NPMapViewMode mode) {
		mapViewMode = mode;
		switch (mapViewMode) {
		case NPMapViewModeDefault:
			setAllowRotationByPinch(true);
			setRotationAngle(0);
			break;

		case NPMapViewModeFollowing:
			setAllowRotationByPinch(false);
			break;

		default:
			break;
		}
	}

	/**
	 * 返回当前楼层的所有公共设施类型
	 * 
	 * @return 公共设施类型数组:[Integer]
	 */
	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return facilityLayer.getAllFacilityCategoryIDOnCurrentFloor();
	}

	/**
	 * 显示当前楼层的所有公共设施
	 */
	public void showAllFacilitiesOnCurrentFloor() {
		facilityLayer.showAllFacilities();
	}

	/**
	 * 显示当前楼层的特定类型公共设施
	 * 
	 * @param categoryID
	 *            公共设施类型ID
	 */
	public void showFacilityOnCurrentWithCategory(int categoryID) {
		facilityLayer.showFacilityWithCategory(categoryID);
	}

	/**
	 * 获取当前楼层下特定子层特定poiID的信息
	 * 
	 * @param pid
	 *            poiID
	 * @param layer
	 *            目标子层
	 * 
	 * @return poi信息
	 */
	public NPPoi getPoiOnCurrentFloorWithPoiID(String pid, POI_LAYER layer) {
		NPPoi result = null;

		switch (layer) {
		case POI_ROOM:
			result = roomLayer.getPoiWithPoiID(pid);
			break;

		case POI_FACILITY:
			result = facilityLayer.getPoiWithPoiID(pid);
			break;

		default:
			break;
		}

		return result;
	}

	/**
	 * 高亮显示POI
	 * 
	 * @param poi
	 *            目标poi 目标poi至少包含poiID和layer信息，当前支持ROOM和FACILITY高亮
	 */
	public void highlightPoi(NPPoi poi) {
		switch (poi.getLayer()) {
		case POI_ROOM:
			// roomLayer.highlightPoi(poi.getPoiID());
			NPPoi roomPoi = roomLayer.getPoiWithPoiID(poi.getPoiID());
			if (poi != null) {
				roomHighlightLayer.addGraphic(new Graphic(
						roomPoi.getGeometry(), null));
			}
			break;

		case POI_FACILITY:
			facilityLayer.highlightPoi(poi.getPoiID());
			break;

		default:
			break;
		}
	}

	/**
	 * 高亮显示一组POI
	 * 
	 * @param poiList
	 *            目标poi数组
	 */
	public void highlightPois(List<NPPoi> poiList) {
		for (NPPoi poi : poiList) {
			highlightPoi(poi);
		}
	}

	/**
	 * 根据坐标x和y提取当前楼层的ROOM POI
	 * 
	 * @param x
	 *            坐标x
	 * @param y
	 *            坐标y
	 * 
	 * @return ROOM POI
	 */
	public NPPoi extractRoomPoiOnCurrentFloor(double x, double y) {
		return roomLayer.extractPoiOnCurrentFloor(x, y);
	}

	@Override
	public void onSingleTap(float x, float y) {
		clearSelection();

		Point p = toMapPoint(x, y);
		notifyClickAtPoint(this, p);

		if (listeners.size() > 0) {
			List<NPPoi> poiList = extractSelectedPoi(x, y);
			// Log.i(TAG, "PoiList: " + poiList.size());
			if (poiList.size() > 0) {
				notifyPoiSelected(this, poiList);
			}
		}

		if (highlightPoiOnSelection) {
			highlightPoiFeature(x, y);
		}
	}

	@Override
	public void postPointerMove(float fromx, float fromy, float tox, float toy) {
		// Log.i(TAG, "postPointerMove: " + fromx + ", " + fromy + ", " + tox
		// + ", " + toy);
		// checkMapCenter();
	}

	@Override
	public void postPointerUp(float fromx, float fromy, float tox, float toy) {
		// Log.i(TAG, "postPointerUp");
		// checkMapCenter();
	}

	private long lastTimeCheckCenter = 0;

	private void checkMapCenter() {

		long now = System.currentTimeMillis();
		if (now - lastTimeCheckCenter < 200) {
			return;
		}

		lastTimeCheckCenter = now;

		Point center = getCenter();

		double x = center.getX();
		double y = center.getY();

		double xmax = currentMapInfo.getMapExtent().getXmax();
		double xmin = currentMapInfo.getMapExtent().getXmin();
		double ymax = currentMapInfo.getMapExtent().getYmax();
		double ymin = currentMapInfo.getMapExtent().getYmin();
		if (x <= xmax && x >= xmin && y <= ymax && y >= ymin) {
			return;
		}

		x = (x > xmax ? xmax : (x < xmin ? xmin : x));
		y = (y > ymax ? ymax : (y < ymin ? ymin : y));

		centerAt(new Point(x, y), true);
	}

	@Override
	public void prePointerMove(float fromx, float fromy, float tox, float toy) {
		// Log.i(TAG, "prePointerMove");
	}

	@Override
	public void prePointerUp(float fromx, float fromy, float tox, float toy) {
		// Log.i(TAG, "prePointerUp");
		checkMapCenter();

	}

	@Override
	public void postAction(float pivotX, float pivotY, double factor) {
		boolean labelVisible = getScale() < DEFAULT_SCALE_THRESHOLD;
		labelLayer.setVisible(labelVisible);
	}

	// 一般按5个字算，屏幕占距1cm，6m的房间内可以显示
	private static final double DEFAULT_SCALE_THRESHOLD = 600;

	@Override
	public void preAction(float pivotX, float pivotY, double factor) {

	}

	private void highlightPoiFeature(float x, float y) {
		int[] facilityIDs = facilityLayer
				.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		if (facilityIDs != null && facilityIDs.length > 0) {
			facilityLayer.setSelectedGraphics(facilityIDs, true);
			return;
		}

		// int[] assetIDs = assetLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		// if (assetIDs != null && assetIDs.length > 0) {
		// assetLayer.setSelectedGraphics(assetIDs, true);
		// return;
		// }

		int[] roomIDs = roomLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
		if (roomIDs != null && roomIDs.length > 0) {
			// roomLayer.setSelectedGraphics(roomIDs, true);
			for (int gid : roomIDs) {
				Graphic g = roomLayer.getGraphic(gid);
				roomHighlightLayer
						.addGraphic(new Graphic(g.getGeometry(), null));
			}
			return;
		}
	}

	private List<NPPoi> extractSelectedPoi(float x, float y) {
		List<NPPoi> poiList = new ArrayList<NPPoi>();
		{
			int[] facilityIDs = facilityLayer.getGraphicIDs(x, y,
					DEFAULT_TOLERANCE);
			if (facilityIDs != null && facilityIDs.length > 0) {
				for (int gid : facilityIDs) {
					Graphic g = facilityLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_LAYER.POI_FACILITY);
					poiList.add(poi);
				}
			}
		}

		{
			int[] roomIDs = roomLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
			if (roomIDs != null && roomIDs.length > 0) {
				for (int gid : roomIDs) {
					Graphic g = roomLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_LAYER.POI_ROOM);
					poiList.add(poi);
				}
			}
		}

		{
			int[] assetIDs = assetLayer.getGraphicIDs(x, y, DEFAULT_TOLERANCE);
			if (assetIDs != null && assetIDs.length > 0) {
				for (int gid : assetIDs) {
					Graphic g = assetLayer.getGraphic(gid);
					NPPoi poi = new NPPoi(
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
							(String) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
							g.getGeometry(),
							(Integer) g
									.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
							POI_LAYER.POI_ASSET);
					poiList.add(poi);
				}
			}
		}

		return poiList;
	}

	/**
	 * 地图事件接口
	 */
	public interface NPMapViewListenser {
		/**
		 * 地图点选事件回调方法
		 * 
		 * @param mapView
		 *            地图视图
		 * @param mappoint
		 *            点击事件的地图坐标
		 */
		void onClickAtPoint(NPMapView mapView, Point mappoint);

		/**
		 * 地图POI选中事件回调
		 * 
		 * @param mapView
		 *            地图视图
		 * @param array
		 *            选中的POI数组:[NPPoi]
		 */
		void onPoiSelected(NPMapView mapView, List<NPPoi> poiList);

		/**
		 * 地图楼层加载完成回调
		 * 
		 * @param mapView
		 *            地图视图
		 * @param mapInfo
		 *            加载楼层信息
		 */
		void onFinishLoadingFloor(NPMapView mapView, NPMapInfo mapInfo);

	}

	/**
	 * 添加地图事件监听
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void addMapListener(NPMapViewListenser listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * 移除地图事件监听
	 * 
	 * @param listener
	 *            地图事件监听接口
	 */
	public void removeMapListener(NPMapViewListenser listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void notifyPoiSelected(NPMapView mapView, List<NPPoi> poiList) {
		for (NPMapViewListenser listener : listeners) {
			listener.onPoiSelected(mapView, poiList);
		}
	}

	private void notifyClickAtPoint(NPMapView mapView, Point mappoint) {
		for (NPMapViewListenser listener : listeners) {
			listener.onClickAtPoint(mapView, mappoint);
		}
	}

	private void notifyFinishLoadingFloor(NPMapView mapView, NPMapInfo mapInfo) {
		for (NPMapViewListenser listener : listeners) {
			listener.onFinishLoadingFloor(mapView, mapInfo);
		}
	}

	public enum NPMapViewMode {
		NPMapViewModeDefault, NPMapViewModeFollowing
	}

}
