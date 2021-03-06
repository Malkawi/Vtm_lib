/*
 * Copyright 2012, Hannes Janetzek
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
import org.oscim.core.Tile;
import org.oscim.renderer.GLRenderer;
import org.oscim.renderer.GLRenderer.Matrices;
import org.oscim.utils.FastMath;
import org.oscim.view.MapView;

public abstract class RenderOverlay   {

	protected final MapView mMapView;
	// keep the Position for which the Overlay is rendered
	protected MapPosition mMapPosition;

	// flag to set when data is ready for (re)compilation.
	public boolean newData;

	// flag set by GLRenderer when data is compiled
	public boolean isReady;

	public RenderOverlay(MapView mapView) {
		mMapView = mapView;
		mMapPosition = new MapPosition();
	}

	// /////////////// called from GLRender Thread ////////////////////////
	/**
	 * called 1. by GLRenderer. Set 'newData' true when 'compile()' should be
	 * called
	 * before next 'render()'
	 *
	 * @param curPos TODO
	 * @param positionChanged
	 *            true when MapPosition has changed
	 * @param tilesChanged
	 *            true when current tiles changed
	 * @param matrices TODO
	 */
	public abstract void update(MapPosition curPos, boolean positionChanged, boolean tilesChanged, Matrices matrices);

	/**
	 * called 2. compile everything for drawing
	 * Set 'isReady' true when things are ready for 'render()'
	 */
	public abstract void compile();

	/**
	 * called 3. draw overlay
	 *
	 * @param pos
	 *            current MapPosition
	 * @param m
	 *            current render matrices + matrix for temporary use
	 */
	public abstract void render(MapPosition pos, Matrices m);

	/**
	 * Utility: set m.mvp matrix relative to the difference of current MapPosition
	 * and the last updated Overlay MapPosition
	 *
	 * @param curPos ...
	 * @param m ...
	 * @param project
	 *  apply view and projection, or just view otherwise
	 */
	protected void setMatrix(MapPosition curPos, Matrices m, boolean project) {
		MapPosition oPos = mMapPosition;

		float div = FastMath.pow(oPos.zoomLevel - curPos.zoomLevel);

		// translate relative to map center
		float x = (float) (oPos.x - curPos.x * div);
		float y = (float) (oPos.y - curPos.y * div);

		// flip around date-line
		float max = (Tile.TILE_SIZE << oPos.zoomLevel);
		if (x < -max / 2)
			x = max + x;
		else if (x > max / 2)
			x = x - max;

		// scale to current tile world coordinates
		float scale = curPos.scale / div;

		// set scale to be relative to current scale
		float s = (curPos.scale / oPos.scale) / div;

		m.mvp.setTransScale(x * scale, y * scale,
				s / GLRenderer.COORD_SCALE);

		m.mvp.multiplyMM(project ? m.viewproj : m.view, m.mvp);
	}

	/**
	 * Utility: set m.mvp matrix relative to the difference of current MapPosition
	 * and the last updated Overlay MapPosition and add m.viewproj
	 *
	 * @param curPos ...
	 * @param m ...
	 */
	protected void setMatrix(MapPosition curPos, Matrices m) {
		setMatrix(curPos, m, true);
	}

//	/**
//	 * Utility: update mMapPosition
//	 *
//	 * @return true if position has changed
//	 */
//	protected boolean updateMapPosition() {
//		return mMapView.getMapViewPosition().getMapPosition(mMapPosition);
//	}
}
