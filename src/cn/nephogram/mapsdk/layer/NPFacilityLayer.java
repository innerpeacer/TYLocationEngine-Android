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

import android.annotation.SuppressLint;
import android.content.Context;
import cn.nephogram.mapsdk.NPMapType;
import cn.nephogram.mapsdk.NPPictureSymbol;
import cn.nephogram.mapsdk.NPRenderingScheme;
import cn.nephogram.mapsdk.poi.NPPoi;
import cn.nephogram.mapsdk.poi.NPPoi.POI_LAYER;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;

public class NPFacilityLayer extends GraphicsLayer {
	static final String TAG = NPFacilityLayer.class.getSimpleName();
	private Context context;

	@SuppressLint("UseSparseArrays")
	private Map<Integer, List<Graphic>> groupedGraphicDict = new HashMap<Integer, List<Graphic>>();
	private Map<String, Graphic> facilityDict = new HashMap<String, Graphic>();
	private Map<String, Integer> facilityGidDict = new HashMap<String, Integer>();

	private NPRenderingScheme renderingScheme;

	public NPFacilityLayer(Context context, NPRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;

		setRenderer(createFacilityRender());
	}

	private Renderer createFacilityRender() {
		UniqueValueRenderer facilityRenderer = new UniqueValueRenderer();
		List<UniqueValue> uvInfo = new ArrayList<UniqueValue>();

		for (Integer colorID : renderingScheme.getIconSymbolDictionary()
				.keySet()) {
			String icon = renderingScheme.getIconSymbolDictionary()
					.get(colorID);
			int resourceID = context.getResources().getIdentifier(icon,
					"drawable", context.getPackageName());
			NPPictureSymbol ps = new NPPictureSymbol(context.getResources()
					.getDrawable(resourceID));
			ps.setWidth(16);
			ps.setHeight(16);

			UniqueValue uv = new UniqueValue();
			uv.setSymbol(ps);
			uv.setValue(new Integer[] { colorID });
			uvInfo.add(uv);
		}

		facilityRenderer.setField1("COLOR");
		facilityRenderer.setUniqueValueInfos(uvInfo);
		return facilityRenderer;
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();
		groupedGraphicDict.clear();
		facilityDict.clear();
		facilityGidDict.clear();

		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			for (Graphic graphic : graphics) {
				int categoryID = (Integer) graphic.getAttributeValue("COLOR");
				if (!groupedGraphicDict.keySet().contains(categoryID)) {
					List<Graphic> graphicList = new ArrayList<Graphic>();
					groupedGraphicDict.put(categoryID, graphicList);
				}

				List<Graphic> graphicList = groupedGraphicDict.get(categoryID);
				graphicList.add(graphic);

				String poiID = (String) graphic
						.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				facilityDict.put(poiID, graphic);

				int gid = addGraphic(graphic);
				facilityGidDict.put(poiID, gid);
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
		groupedGraphicDict.clear();
		facilityDict.clear();
		facilityGidDict.clear();

		JsonFactory factory = new JsonFactory();

		try {
			InputStream inStream = context.getAssets().open(path);
			JsonParser parser = factory.createJsonParser(inStream);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			for (Graphic graphic : graphics) {
				int categoryID = (Integer) graphic.getAttributeValue("COLOR");
				if (!groupedGraphicDict.keySet().contains(categoryID)) {
					List<Graphic> graphicList = new ArrayList<Graphic>();
					groupedGraphicDict.put(categoryID, graphicList);
				}

				List<Graphic> graphicList = groupedGraphicDict.get(categoryID);
				graphicList.add(graphic);

				String poiID = (String) graphic
						.getAttributeValue(NPMapType.GRAPHIC_ATTRIBUTE_POI_ID);
				facilityDict.put(poiID, graphic);

				int gid = addGraphic(graphic);
				facilityGidDict.put(poiID, gid);
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

	// - (void)showFacilityWithCategory:(int)categoryID
	// {
	// [self removeAllGraphics];
	// NSArray *array = groupedFacilityDict[@(categoryID)];
	// [self addGraphics:array];
	// }
	//
	// - (void)showAllFacilities
	// {
	// [self removeAllGraphics];
	// for (NSArray *array in groupedFacilityDict.allValues) {
	// [self addGraphics:array];
	// }
	// }
	//
	// - (NSArray *)getAllFacilityCategoryIDOnCurrentFloor
	// {
	// return [groupedFacilityDict allKeys];
	// }

	public void showFacilityWithCategory(int categoryID) {
		removeAll();
		if (groupedGraphicDict.keySet().contains(categoryID)) {
			List<Graphic> graphicList = groupedGraphicDict.get(categoryID);

			Graphic[] graphics = new Graphic[graphicList.size()];
			graphics = graphicList.toArray(graphics);
			addGraphics(graphics);
		}
	}

	public void showAllFacilities() {
		removeAll();
		for (List<Graphic> graphicList : groupedGraphicDict.values()) {
			Graphic[] graphics = new Graphic[graphicList.size()];
			graphics = graphicList.toArray(graphics);
			addGraphics(graphics);
		}
	}

	public List<Integer> getAllFacilityCategoryIDOnCurrentFloor() {
		return new ArrayList<Integer>(groupedGraphicDict.keySet());
	}

	public NPPoi getPoiWithPoiID(String pid) {
		NPPoi result = null;
		Graphic graphic = facilityDict.get(pid);
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
					POI_LAYER.POI_FACILITY);
		}
		return result;
	}

	public void highlightPoi(String poiID) {
		if (facilityGidDict.containsKey(poiID)) {
			int gid = facilityGidDict.get(poiID);
			setSelectedGraphics(new int[] { gid }, true);
		}
	}
}
