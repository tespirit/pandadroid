package com.tespirit.bamporter.opengl;

import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.Vector3d;

public class UVTextureViewer {
	VertexIndices mVi;
	Primitives.Plane mPlane;
	Surface mSurface;
	Color mLines;
	
	UVTextureViewer(Model model){
		this.mSurface = model.getSurface();
		VertexIndices vi = (VertexIndices)model.getPrimitive();
		VertexBuffer vb = vi.getVertexBuffer();
		VertexBuffer uvs = new VertexBuffer(vb.getCount(), new int[]{VertexBuffer.POSITION, VertexBuffer.TEXCOORD});
		this.mVi = new VertexIndices(uvs, vi.getIndexBuffer());
		uvs.lock();
		Vector3d temp = new Vector3d();
		while(vb.nextTexcoord(temp)){
			uvs.addTexcoord(temp.getX(), temp.getY());
			uvs.addPosition(temp.add(new Vector3d(-0.5f, -0.5f, 0.0f)));
		}
		uvs.unlock();
		
		this.mLines = new Color();
		this.mLines.setColor(1f, 1f, 1f);
		
		this.mPlane = new Primitives.Plane(1.0f, -1.0f);
		
	}
	
	public void render(){
		this.mSurface.renderStart();
		this.mPlane.render();
		this.mVi.renderAsTriangles();
		this.mVi.render();
		this.mSurface.renderEnd();
		this.mVi.renderAsLines();
		this.mLines.renderStart();
		this.mVi.render();
		this.mLines.renderEnd();
	}
}
