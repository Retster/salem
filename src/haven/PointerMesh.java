/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.awt.Color;
import java.nio.*;
import javax.media.opengl.*;

import org.apxeolog.salem.HConfig;

public class PointerMesh extends QuadMesh {
	public PointerMesh(VertexBuf vert, short[] ind) {
		super(vert, ind);
	}

	public Color getCol(float z1, float z2) {
		if (HConfig.cl_grid_mode == MapView.GRID_MODE_SIMPLE) {
			return Color.GRAY;
		} else {
			float val = z1 - z2;
			if (val == 0.0f) return Color.GREEN;
			else return Color.RED;
		}
	}
	
	public void sdraw(GOut g) {
		GL gl = g.gl;
		VertexBuf.VertexArray vbuf = null;
		for (int i = 0; i < vert.bufs.length; i++) {
			if (vert.bufs[i] instanceof VertexBuf.VertexArray)
				vbuf = (VertexBuf.VertexArray) vert.bufs[i];
		}
		float zshare = 0.15f;
		float[] colorBuf = Utils.c2fa(Color.RED);
		gl.glLineWidth(3.0f);
		gl.glBegin(GL.GL_LINES);
		for (int i = 0; i < num * 4; i += 4) {
			int v1 = indb.get(i);
			int v2 = indb.get(i + 1);
			int v3 = indb.get(i + 2);
			int v4 = indb.get(i + 3);
			
			gl.glColor4f(colorBuf[0], colorBuf[1], colorBuf[2], colorBuf[3]);
			vbuf.setAddZ(g, v1, zshare);
			vbuf.setAddZ(g, v2, zshare);
			gl.glColor4f(colorBuf[0], colorBuf[1], colorBuf[2], colorBuf[3]);
			vbuf.setAddZ(g, v2, zshare);
			vbuf.setAddZ(g, v3, zshare);
			gl.glColor4f(colorBuf[0], colorBuf[1], colorBuf[2], colorBuf[3]);
			vbuf.setAddZ(g, v3, zshare);
			vbuf.setAddZ(g, v4, zshare);
			gl.glColor4f(colorBuf[0], colorBuf[1], colorBuf[2], colorBuf[3]);
			vbuf.setAddZ(g, v4, zshare);
			vbuf.setAddZ(g, v1, zshare);
		}
		gl.glEnd();
	}
}
