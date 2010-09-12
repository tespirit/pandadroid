package com.tespirit.panda3d.primitives;

import com.tespirit.panda3d.vectors.AxisAlignedBox;

public class LineIndices extends Primitive{
	private VertexBuffer vertexBuffer;
	private IndexBuffer indexBuffer;
	
	public LineIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
		this.indexBuffer = new IndexBuffer(indexCount, vertexCount);
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
		// TODO Auto-generated method stub
		
	}
}
