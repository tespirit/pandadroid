package com.tespirit.panda3d.primitives;

import com.tespirit.panda3d.vectors.AxisAlignedBox;

public class LineList extends Primitive{
	private VertexBuffer vertexBuffer;
	
	public LineList(int vertexCount, int[] vertexTypes){
		this.vertexBuffer = new VertexBuffer(vertexCount, vertexTypes);
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
		// TODO Auto-generated method stub
		
	}
}
