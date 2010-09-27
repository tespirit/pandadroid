package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public class LineList extends Primitive{
	protected VertexBuffer vertexBuffer;
	
	public LineList(int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.renderAsLineStrip();
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.vertexBuffer;
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		vertexBuffer.computeBoundingBox(boundingBox);
	}

	@Override
	public void render() {
		LineList.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			LineList.renderer = this;
		}
		
		public abstract void render(LineList lines);
	}
}
