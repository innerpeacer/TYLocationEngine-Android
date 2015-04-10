package cn.nephogram.mapsdk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

public class NPPathCalibration {

	private static final double DEFAULT_BUFFER_WIDTH = 1.0;

	private List<Geometry> featureArray = new ArrayList<Geometry>();
	private double width = DEFAULT_BUFFER_WIDTH;

	private Geometry unionPathLine = new Polyline();
	private Geometry unionPathPolygon = new Polygon();

	public NPPathCalibration(String path) {
		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(new File(path));
			FeatureSet set = FeatureSet.fromJson(parser);

			for (Graphic graphic : set.getGraphics()) {
				featureArray.add(graphic.getGeometry());
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Point calibrationPoint(Point point) {
		Point result = point;
		if (GeometryEngine.contains(unionPathPolygon, point,
				NPMapEnvironment.defaultSpatialReference())) {
			Proximity2DResult proximityResult = GeometryEngine
					.getNearestCoordinate(unionPathLine, point, false);
			result = proximityResult.getCoordinate();
		}
		return result;
	}

	public void setBufferWidth(double w) {
		width = w;
		unionPathLine = GeometryEngine.union(
				(Geometry[]) featureArray.toArray(),
				NPMapEnvironment.defaultSpatialReference());
		unionPathPolygon = GeometryEngine.buffer(unionPathLine,
				NPMapEnvironment.defaultSpatialReference(), width, null);
	}
}
