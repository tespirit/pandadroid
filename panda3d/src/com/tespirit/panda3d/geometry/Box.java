package com.tespirit.panda3d.geometry;

public class Box extends Mesh {
	
	public Box(){
		this.vertexBuffer = new VertexBuffer(8);
		
		this.vertexBuffer.addPosition(-1, -1, -1);
		this.vertexBuffer.addPosition(1, -1, -1);
		this.vertexBuffer.addPosition(1, -1, 1);
		this.vertexBuffer.addPosition(-1, -1, 1);
		this.vertexBuffer.addPosition(1, 1, 1);
		this.vertexBuffer.addPosition(-1, 1, 1);
		this.vertexBuffer.addPosition(1, 1, -1);
		this.vertexBuffer.addPosition(-1, 1, -1);
		
		this.indexBuffer = new IndexBuffer(12*3);
		this.indexBuffer.addTriangle(0, 1, 2);
		this.indexBuffer.addTriangle(0, 2, 3);
		
		this.indexBuffer.addTriangle(3, 2, 4);
		this.indexBuffer.addTriangle(3, 4, 5);
		
		this.indexBuffer.addTriangle(5, 4, 6);
		this.indexBuffer.addTriangle(5, 6, 7);
		
		this.indexBuffer.addTriangle(0, 7, 1);
		this.indexBuffer.addTriangle(6, 1, 7);
		
		this.indexBuffer.addTriangle(2, 1, 4);
		this.indexBuffer.addTriangle(4, 1, 6);
		
		this.indexBuffer.addTriangle(0, 3, 7);
		this.indexBuffer.addTriangle(3, 5, 7);
		
		this.vertexBuffer.resetBufferPosition();
		this.indexBuffer.resetBufferPosition();

	}

}
