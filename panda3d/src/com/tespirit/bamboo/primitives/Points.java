package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Vector3d;

public class Points extends Primitive{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1640806254288255365L;
	VertexBuffer vertexBuffer;
	
	public Points(Vector3d[] points){
		this(points.length);
		for(Vector3d point : points){
			this.vertexBuffer.setPosition(point);
		}
		this.vertexBuffer.resetBufferPosition();
	}
	
	public Points(Vector3d point){
		this(1);
		this.vertexBuffer.setPosition(point);
		this.vertexBuffer.resetBufferPosition();
	}
	
	public Points(int vertexCount){
		this(vertexCount, false);
	}
	
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

	public VertexBuffer getVertexBuffer(){
		return this.vertexBuffer;
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
