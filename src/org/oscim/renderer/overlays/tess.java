/*
 * Copyright 2012 Hannes Janetzek
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.oscim.renderer.overlays;

import org.oscim.core.MapPosition;
import org.oscim.overlay.Overlay;
import org.oscim.renderer.GLRenderer.Matrices;
import org.oscim.renderer.layer.Layer;
import org.oscim.renderer.layer.LineTexLayer;
import org.oscim.renderer.layer.TextItem;
import org.oscim.theme.renderinstruction.Line;
import org.oscim.view.MapView;

import android.graphics.Color;
public class tess extends Overlay {


	public tess(MapView mapView) {
		super(mapView);

		mLayer = new TestOverlay(mapView);

	}

public class TestOverlay extends BasicOverlay {

	TextItem labels;

	float drawScale;
	float[] points = {
			-1, -1,
			1, -1,
			1, 1,
			-1, 1,
			-1, -1
			};
	private boolean first = true;
long timertick;
	public TestOverlay(MapView mapView) {
		super(mapView);

		// draw a rectangle
		//LineLayer ll = (LineLayer) layers.getLayer(1, Layer.LINE);
		//ll.line = new Line(Color.BLUE, 1.0f, Cap.BUTT);
		//ll.width = 2;

		//short[] index = { (short) points.length };
		//ll.addLine(points, index, true);
timertick =System.currentTimeMillis()+2000;
		LineTexLayer lt = (LineTexLayer) layers.getLayer(2, Layer.TEXLINE);
		lt.line = new Line(Color.argb(60, 152,251,152), 6.0f, 0);
		lt.width = 20;
		lt.addLine(points, null);

		float[] points2 = {
				-2, -2,
				2, -2,
				2, 2,
				-2, 2,
				-2, -2
				};
		lt = (LineTexLayer) layers.getLayer(3, Layer.TEXLINE);
		lt.line = new Line(Color.RED, 1.0f, 0);
		lt.width = 8;

		lt.addLine(points2, null);

		//
		// PolygonLayer pl = (PolygonLayer) layers.getLayer(0, Layer.POLYGON);
		// pl.area = new Area(Color.argb(128, 255, 0, 0));
		//
		// float[] ppoints = {
		// 0, 256,
		// 0, 0,
		// 256, 0,
		// 256, 256,
		// };
		// short[] pindex = { (short) ppoints.length };
		// pl.addPolygon(ppoints, pindex);

		//SymbolLayer sl = new SymbolLayer();
		//SymbolItem it = new SymbolItem();
		//
		//it.x = 0;
		//it.y = 0;
		//// billboard always faces camera
		//it.billboard = true;
		//
		//try {
		//	it.bitmap = BitmapUtils.createBitmap("file:/sdcard/cheshire.png");
		//} catch (IOException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//sl.addSymbol(it);
		//
		//SymbolItem it2 = new SymbolItem();
		//it2.bitmap = it.bitmap;
		//it2.x = 0;
		//it2.y = 0;
		//// billboard always faces camera
		//it2.billboard = false;
		//
		//sl.addSymbol(it2);
		//sl.fixed = false;
		//
		//layers.textureLayers = sl;

		// TextLayer tl = new TextLayer();
		// Text t = Text.createText(20, 2, Color.WHITE, Color.BLACK, false);
		// TextItem ti = new TextItem(0, 0, "check one, check two", t);
		// ti.x1 = 0;
		// ti.y1 = 0;
		// ti.x2 = (short) Tile.TILE_SIZE;
		// ti.y2 = (short) Tile.TILE_SIZE;
		//
		// tl.addText(ti);
		//
		// layers.textureLayers = tl;
	}
float width =.2F;
	@Override
	public synchronized void update(MapPosition curPos, boolean positionChanged,
			boolean tilesChanged, Matrices matrices) {
		// keep position constant (or update layer relative to new position)
		//mMapPosition.copy(curPos);
		//matrices.mvp
		if (first) {
			// fix at initial position
			mMapPosition.copy(curPos);

			first = false;
			//((SymbolLayer) (layers.textureLayers)).prepare();

			// pass layers to be uploaded and drawn to GL Thread
			// afterwards never modify 'layers' outside of this function!
			newData = true;
		}


if(timertick < System.currentTimeMillis() && curPos.zoomLevel >=16){

points = new float [
				]{-.3F, -.3F,
		.3F, -.3F,
		.3F, .3F,
		-.3F, .3F,
		-.3F, -.3F} ;
	//short[] index = { (short) points.length };
	//ll.addLine(points, index, true);

layers.clear();


timertick =System.currentTimeMillis()+20;
	LineTexLayer lt = (LineTexLayer) layers.getLayer(0, Layer.TEXLINE);


	lt.line = new Line(Color.argb((int)(20+width+40), 152,251,152), 7.0f , 0);
	lt.width = 20+ width;
	if(lt.width>= 80 ){
		lt.width=1;
		width= 20;
	}
	lt.addLine(points, null);

	width+=3;
	newData = true;
	//mMapPosition.copy(curPos);
//curPos.setFromLatLon(curPos.lat+.000000001F, curPos.lon, curPos.zoomLevel);
	//mMapPosition.copy(curPos);
}






	}


}


}
