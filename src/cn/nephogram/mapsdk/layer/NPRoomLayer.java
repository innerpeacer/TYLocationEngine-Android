package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;

public class NPRoomLayer extends GraphicsLayer {
	static final String TAG = NPRoomLayer.class.getSimpleName();
	private Context context;
	private NPRenderingScheme renderingScheme;

	private Map<String, Graphic> roomDict = new HashMap<String, Graphic>();
	private Map<String, Integer> roomGidDict = new HashMap<String, Integer>();

	public NPRoomLayer(Context context, NPRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;
		setRenderer(createRoomRenderer());
	}

	private Renderer createRoomRenderer() {
		UniqueValueRenderer roomRenderer = new UniqueValueRenderer();
		List<UniqueValue> uvInfo = new ArrayList<UniqueValue>();

		for (Integer colorID : renderingScheme.getFillSymbolDictionary()
				.keySet()) {
			SimpleFillSymbol sfs = renderingScheme.getFillSymbolDictionary()
					.get(colorID);
			UniqueValue uv = new UniqueValue();
			uv.setSymbol(sfs);
			uv.setValue(new Integer[] { colorID });
			uvInfo.add(uv);
		}
		roomRenderer.setDefaultSymbol(renderingScheme.getDefaultFillSymbol());

		roomRenderer.setField1("COLOR");
		roomRenderer.setUniqueValueInfos(uvInfo);
		return roomRenderer;
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();
		roomDict.clear();
		roomGidDict.clear();
		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			for (Graphic graphic : graphics) {
				String poiID = (String) graphic
						.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				roomDict.put(poiID, graphic);

				int gid = addGraphic(graphic);
				roomGidDict.put(poiID, gid);
			}

			// addGraphics(graphics);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadContentsFromAssetsWithInfo(String path) {
		removeAll();
		roomDict.clear();
		roomGidDict.clear();
		JsonFactory factory = new JsonFactory();

		try {
			InputStream inStream = context.getAssets().open(path);
			JsonParser parser = factory.createJsonParser(inStream);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			for (Graphic graphic : graphics) {
				String poiID = (String) graphic
						.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				roomDict.put(poiID, graphic);

				int gid = addGraphic(graphic);
				roomGidDict.put(poiID, gid);
			}

			addGraphics(graphics);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NPPoi getPoiWithPoiID(String pid) {
		NPPoi result = null;
		Graphic graphic = roomDict.get(pid);

		if (graphic != null) {
			result = new NPPoi(
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
					(String) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
					graphic.getGeometry(),
					(Integer) graphic
							.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
					POI_LAYER.POI_ROOM);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (roomGidDict.containsKey(poiID)) {
			int gid = roomGidDict.get(poiID);
			setSelectedGraphics(new int[] { gid }, true);
		}
	}

	public NPPoi extractPoiOnCurrentFloor(double x, double y) {
		NPPoi poi = null;

		for (String poiID : roomDict.keySet()) {
			Graphic graphic = roomDict.get(poiID);
			if (GeometryEngine.contains(graphic.getGeometry(), new Point(x, y),
					getSpatialReference())) {
				poi = new NPPoi(
						(String) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_GEO_ID),
						(String) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID),
						(String) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_FLOOR_ID),
						(String) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_BUILDING_ID),
						(String) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_NAME),
						graphic.getGeometry(),
						(Integer) graphic
								.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_CATEGORY_ID),
						POI_LAYER.POI_ROOM);
				break;
			}
		}

		return poi;
	}
}
