package com.tespirit.panda3d.primitives;

import com.tespirit.panda3d.render.ComponentRenderer;
import com.tespirit.panda3d.vectors.AxisAlignedBox;

/**
 * This is a standard mesh class that has a vertex buffer and an index buffer.
 * @author Todd Espiritu Santo
 *
 */
public class TriangleIndices extends Primitive {
	protected VertexBuffer vertexBuffer;
	protected IndexBuffer indexBuffer;
	
	public TriangleIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.renderAsTriangles(); //default setting.
		this.indexBuffer = new IndexBuffer(indexCount, vertexCount);
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
		TriangleIndices.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			TriangleIndices.renderer = this;
		}
		
		public abstract void render(TriangleIndices triangles);
	}
}
