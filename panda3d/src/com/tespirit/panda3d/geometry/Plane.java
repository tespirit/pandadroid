package com.tespirit.panda3d.geometry;

public class Plane extends Mesh{
	
	public Plane(){
		this.vertexBuffer = new VertexBuffer(4);
		
		this.vertexBuffer.addPosition(-1.0f,  1.0f, 0.0f);
		this.vertexBuffer.addPosition(-1.0f, -1.0f, 0.0f);
		this.vertexBuffer.addPosition(1.0f, -1.0f, 0.0f);
		this.vertexBuffer.addPosition(1.0f,  1.0f, 0.0f);
		
		this.indexBuffer = new IndexBuffer(6);
		this.indexBuffer.addTriangle(0, 1, 2);
		this.indexBuffer.addTriangle(0, 2, 3);
				
		this.vertexBuffer.resetBufferPosition();
		this.indexBuffer.resetBufferPosition();

	}
}
