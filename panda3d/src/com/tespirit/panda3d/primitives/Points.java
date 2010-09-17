package com.tespirit.panda3d.primitives;

import com.tespirit.panda3d.render.ComponentRenderer;
import com.tespirit.panda3d.vectors.AxisAlignedBox;

public class Points extends Primitive{
	
	VertexBuffer vertexBuffer;
	
	public Points(int vertexCount, boolean hasCpvs){
		int[] types;
		if(hasCpvs){
			types = new int[]{VertexBuffer.POSITION, VertexBuffer.COLOR};
		} else {
			types = new int[]{VertexBuffer.POSITION};
		}
		this.vertexBuffer = new VertexBuffer(vertexCount, types);
		this.renderAsPoints();
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.vertexBuffer.computeBoundingBox(boundingBox);
	}

	@Override
	public void render() {
		Points.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Points.renderer = this;
		}
		public abstract void render(Points points);
	}

}
