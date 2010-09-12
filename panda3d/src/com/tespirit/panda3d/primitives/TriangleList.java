package com.tespirit.panda3d.primatives;

import com.tespirit.panda3d.vectors.AxisAlignedBox;
import com.tespirit.panda3d.vectors.Vector3d;

public class TriangleList extends Primative{
	protected VertexBuffer vertexBuffer;
	
	protected AxisAlignedBox boundingBox;
	
	public TriangleList(){
		this.boundingBox = new AxisAlignedBox();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.boundingBox;
	}
	
	public void computeBoundingBox(){
		Vector3d position = new Vector3d();
		while(this.vertexBuffer.nextVector3d(position, VertexBuffer.POSITION)){
			this.boundingBox.grow(position);
		}
		vertexBuffer.resetBufferPosition();
	}
}
