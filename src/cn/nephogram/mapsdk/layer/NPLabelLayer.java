package cn.nephogram.mapsdk.layer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
import com.esri.core.symbol.TextSymbol.VerticalAlignment;

public class NPLabelLayer extends GraphicsLayer {
	static final String TAG = NPLabelLayer.class.getSimpleName();
	private Context context;

	public NPLabelLayer(Context context, SpatialReference spatialReference,
			Envelope envelope) {
		super(spatialReference, envelope);
		this.context = context;
	}

	public void loadContentsFromFileWithInfo(String path) {
		removeAll();

		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			for (Graphic graphic : graphics) {
				String name = (String) graphic.getAttributeValue("NAME");

				if (name != null && name.length() > 0) {
					TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
					ts.setFontFamily("DroidSansFallback.ttf");
					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

					Graphic g = new Graphic(graphic.getGeometry(), ts);
					addGraphic(g);
				}
			}
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
			for (Graphic graphic : graphics) {
				String name = (String) graphic.getAttributeValue("NAME");

				if (name != null && name.length() > 0) {
					TextSymbol ts = new TextSymbol(10, name, Color.BLACK);
					ts.setFontFamily("DroidSansFallback.ttf");
					ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
					ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

					Graphic g = new Graphic(graphic.getGeometry(), ts);
					addGraphic(g);
				}
			}
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
