package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public class LineIndices extends Primitive{
	protected VertexBuffer vertexBuffer;
	protected IndexBuffer indexBuffer;
	
	public LineIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.indexBuffer = new IndexBuffer(indexCount, vertexCount);
		this.renderAsLineStrip();
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.vertexBuffer;
	}
	
	public IndexBuffer getIndexBuffer(){
		return this.indexBuffer;
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.vertexBuffer.computeBoundingBox(boundingBox);
	}

	@Override
	public void render() {
		LineIndices.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			LineIndices.renderer = this;
		}
		
		public abstract void render(LineIndices lines);
	}
}
