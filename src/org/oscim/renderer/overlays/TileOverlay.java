/*
 * Copyright 2013 ...
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

import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.core.MercatorProjection;
import org.oscim.core.Tile;
import org.oscim.renderer.GLRenderer;
import org.oscim.renderer.GLRenderer.Matrices;
import org.oscim.renderer.LineRenderer;
import org.oscim.renderer.LineTexRenderer;
import org.oscim.renderer.MapTile;
import org.oscim.renderer.PolygonRenderer;
import org.oscim.renderer.ScanBox;
import org.oscim.renderer.TileSet;
import org.oscim.renderer.layer.Layer;
import org.oscim.utils.FastMath;
import org.oscim.utils.Matrix4;
import org.oscim.view.MapView;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TileOverlay extends RenderOverlay  implements  OnTouchListener  {
	//private final TouchHandler mTouchHandler ;
	private TileSet mTileSet;
MapView mMapView ;

int scaleSize;
static float width ;
ScanBox box;
  static double centerx;
static  double  centery;
int tileX , tileY;
private final boolean oneTime = true;
private static float pixelx;
private static float pixely;
	public TileOverlay(MapView mapView) {
		super(mapView);
mMapView = mapView ;
mapView.setOnTouchListener(this);
width =mMapView.getWidth();
		this.isReady = true;
		scaleSize= Tile.TILE_SIZE;


	//	float pixelx;
		//float  pixely;
		//float [] a =   {-200,200,200,200,200,-200,-200,-200};

//ScanBox2 sBox = new ScanBox2 ();
//sBox.scan(a, mapView.getMapViewPosition().getMapPosition().zoomLevel);
//Toast.makeText(mMapView.getContext(),String.valueOf(sBox.Ypoints[0]), Toast.LENGTH_SHORT).show();


	}

	@Override
	public void update(MapPosition curPos, boolean positionChanged, boolean tilesChanged,
			Matrices matrices) {

		// TODO Auto-generated method stub

	}

	@Override
	public void compile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(MapPosition pos, Matrices m) {
		GLES20.glDepthMask(true);
		GLES20.glStencilMask(0xFF);

		centerx = this.mMapPosition.x;
		centery= this.mMapPosition.y;
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
				| GLES20.GL_STENCIL_BUFFER_BIT);



		// get current tiles

		mTileSet = GLRenderer.getVisibleTiles(mTileSet);
		//mTileSet.tiles[0].
		mDrawCnt = 0;


		GLES20.glDepthFunc(GLES20.GL_LESS);

		// load texture for line caps
		LineRenderer.beginLines();
		//mTileSet.getTile(x, y)
int x =0;
boolean off = true ;
		   //box.scan(a, pos.zoomLevel);
		for(int i = 0; i <mTileSet.cnt; i++){
			MapTile t = mTileSet.tiles[i];


//Toast.makeText(this.mMapView.getContext(), String.valueOf(mTileSet.tiles[0].pixelY), Toast.LENGTH_SHORT).show();

			//if(pos.x > 0 && pos.x  < 200)
//Log.v("me2",String.valueOf( mTileSet.tiles[i].tileX));
//Log.v("me",String.valueOf( i));
			Log.v("me2","X value " +  String.valueOf(t.tileX));
			Log.v("me2","Y value "+ String.valueOf(t.tileY));
			Log.v("me2","I value "+ String.valueOf(i));
		/*	if(tileX+ tileY * 2 <= mTileSet.cnt)
			{
				int j = (tileX+ tileY * 2);
			drawTile(mTileSet.tiles[j], pos, m);
			mMapView.redrawMap(true);
			  }
*/


			this.pixelx = (float) mMapView.getMapPosition().getMapPosition().x;
			this.pixely = (float) mMapView.getMapPosition().getMapPosition().y;
			if (t.tileX == this.tileX && t.tileY ==  this.tileY ){
				drawTile(t,pos,m);

				mMapView.redrawMap(true);

			}
			//break;

		}

		LineRenderer.endLines();

	}





	private static Matrix4 scaleMatrix  = new Matrix4();
static float posscale;
private static int mDrawCnt;
	private static void drawTile(MapTile tile, MapPosition pos, Matrices m) {
		MapTile t = tile;
		//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		 //posscale = pos.scale;
		//if (t.holder != null)
		//	t = t.holder;

		if (t.layers == null || t.layers.vbo == null) {
			//Log.d(TAG, "missing data " + (t.layers == null) + " " + (t.vbo == null));
			return;
		}

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, t.layers.vbo.id);

		// place tile relative to map position
		float div = FastMath.pow(tile.zoomLevel - pos.zoomLevel);
		float x = (float) (tile.pixelX- pos.x * div);
		float y = (float) (tile.pixelY - pos.y * div);
		float scale = pos.scale / div;

		//float scale = pos.scale/ div;
 // Log.v("me", String.valueOf(y));
	//	x += 400 / scale;
		//y += 400 / scale;

		float ratio =
				(1f/ width) *.1f;
	m.mvp.multiplyMM(m.proj, m.mvp);
	m.mvp.setTransScale(ratio,  ratio, scale / GLRenderer.COORD_SCALE);
		m.mvp.multiplyMM(m.viewproj, m.mvp);

		scaleMatrix.setScale(2, 2, 1);
		//float ratio =
			//(1f/ width) *.1f;
		// m.mvp.setScale(ratio, ratio, 1);
		m.mvp.multiplyMM(scaleMatrix, m.mvp);

		// set depth offset (used for clipping to tile boundaries)
		GLES20.glPolygonOffset(1, mDrawCnt++);
		if (mDrawCnt > 20)
			mDrawCnt = 0;

		// simple line shader does not take forward shortening into account
		int simpleShader = 0; //= (pos.tilt < 1 ? 1 : 0);

		boolean clipped = false;
 //pos.zoomLevel +=3;
		for (Layer l = t.layers.baseLayers; l != null;) {
			switch (l.type) {
				case Layer.POLYGON:
					l = PolygonRenderer.draw(pos, l, m, !clipped, true);
					clipped = true;
					break;

				case Layer.LINE:
					if (!clipped) {
						// draw stencil buffer clip region
						PolygonRenderer.draw(pos, null, m, true, true);
						clipped = true;
					}
					l = LineRenderer.draw(t.layers, l, pos, m, div, simpleShader);
					break;

				case Layer.TEXLINE:
					if (!clipped) {
						// draw stencil buffer clip region
						PolygonRenderer.draw(pos, null, m, true, true);
						clipped = true;
					}
					l = LineTexRenderer.draw(t.layers, l, pos, m, div);
					break;

				default:
					// just in case
					l = l.next;
			}
		}

		// clear clip-region and could also draw 'fade-effect'
		PolygonRenderer.drawOver(m);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub

		tileX=MercatorProjection.pixelYToTileY(arg1.getY(),mMapView.getMapViewPosition().getMapPosition().zoomLevel);
		tileY=MercatorProjection.pixelXToTileX(arg1.getX(),mMapView.getMapPosition().getMapPosition().zoomLevel);


		//mMapView.getMapViewPosition().fromScreenPixels(arg1.getX(), arg1.getY());
GeoPoint geo =		mMapView.getMapViewPosition().fromScreenPixels(arg1.getX(), arg1.getY());




this.tileX =  (int) MercatorProjection.longitudeToTileX(geo.getLongitude(), mMapView.getMapPosition().getMapPosition().zoomLevel);
this.tileY =(int) MercatorProjection.latitudeToTileY(geo.getLatitude(), mMapView.getMapPosition().getMapPosition().zoomLevel);

		pixelx = arg1.getX();
		pixely= arg1.getY();
		Log.v("me","the screen touchx " +  String.valueOf(MercatorProjection.longitudeToTileX(geo.getLongitude(), mMapView.getMapPosition().getMapPosition().zoomLevel)));

	Log.v("me","the screen touch y " +  String.valueOf(MercatorProjection.latitudeToTileY(geo.getLatitude(), mMapView.getMapPosition().getMapPosition().zoomLevel)));
		return false;
	}









}


