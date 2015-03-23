package cn.nephogram.mapsdk;

import android.graphics.drawable.Drawable;

import com.esri.core.symbol.PictureMarkerSymbol;

public class NPPictureSymbol extends PictureMarkerSymbol {
	private static final long serialVersionUID = 1L;

	public NPPictureSymbol(Drawable d) {
		super(d);
	}

	public void setWidth(float w) {
		super.setWidth(w);
	}

	public void setHeight(float h) {
		super.setHeight(h);
	}
}
