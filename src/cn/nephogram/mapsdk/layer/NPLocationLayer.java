package cn.nephogram.mapsdk.layer;

import android.graphics.Color;
import cn.nephogram.mapsdk.NPMapView.NPMapViewMode;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class NPLocationLayer extends GraphicsLayer {
	private MarkerSymbol locationSymbol;

	public NPLocationLayer() {
		super();

		locationSymbol = new SimpleMarkerSymbol(Color.GREEN, 5, STYLE.CIRCLE);
		SimpleRenderer render = new SimpleRenderer(locationSymbol);
		setRenderer(render);
	}

	public void setLcoationSymbol(MarkerSymbol symbol) {

		locationSymbol = symbol;
		SimpleRenderer render = new SimpleRenderer(locationSymbol);
		setRenderer(render);

	}

	public void updateDeviceHeading(double deviceHeading, double initAngle,
			NPMapViewMode mode) {
		switch (mode) {
		case NPMapViewModeDefault:
			locationSymbol.setAngle((float) (deviceHeading + initAngle));
			break;

		case NPMapViewModeFollowing:
			locationSymbol.setAngle(0);
			break;

		default:
			break;
		}
	}

	public void showLocation(Point location, double deviceHeading,
			double initAngle, NPMapViewMode mode) {
		removeAll();
		addGraphic(new Graphic(location, locationSymbol));
	}

	public void removeLocation() {
		removeAll();
	}
}
