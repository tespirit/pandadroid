package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public class TriangleList extends Primitive{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8960016570515255553L;
	protected VertexBuffer vertexBuffer;
	
	public TriangleList(int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.renderAsTriangleStrip(); //default setting.
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.vertexBuffer;
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.vertexBuffer.computeBoundingBox(boundingBox);
	}
	
	public void renderWireFrame(){
		this.renderAsLineStrip();
	}
	
	public void renderSolid(){
		this.renderAsTriangleStrip();
	}

	@Override
	public void render() {
		TriangleList.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			TriangleList.renderer = this;
		}
		public abstract void render(TriangleList triangles);
	}
}
