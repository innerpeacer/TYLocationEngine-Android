package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import cn.nephogram.mapsdk.NPRenderingScheme;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;

public class NPAssetLayer extends GraphicsLayer {
	static final String TAG = NPAssetLayer.class.getSimpleName();

	private Context context;
	private NPRenderingScheme renderingScheme;

	public NPAssetLayer(Context context, NPRenderingScheme renderingScheme,
			SpatialReference spatialReference, Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
		this.renderingScheme = renderingScheme;

		setRenderer(createAssetRenderer());
	}

	private Renderer createAssetRenderer() {
		UniqueValueRenderer assetRenderer = new UniqueValueRenderer();
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
		assetRenderer.setDefaultSymbol(renderingScheme.getDefaultFillSymbol());

		assetRenderer.setField1("COLOR");
		assetRenderer.setUniqueValueInfos(uvInfo);
		return assetRenderer;
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();
		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			addGraphics(graphics);
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
		JsonFactory factory = new JsonFactory();

		try {
			InputStream inStream = context.getAssets().open(path);
			JsonParser parser = factory.createJsonParser(inStream);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			addGraphics(graphics);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
