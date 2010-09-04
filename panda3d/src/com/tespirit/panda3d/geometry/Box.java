package com.tespirit.panda3d.geometry;

public class Box extends Mesh {
	
	public Box(){
		this.vertexBuffer = new VertexBuffer(24, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD});
		
		this.vertexBuffer.addPosition(-1, -1, -1);
		this.vertexBuffer.addPosition(1, -1, -1);
		this.vertexBuffer.addPosition(1, -1, 1);
		this.vertexBuffer.addPosition(-1, -1, 1);
		
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		
		this.vertexBuffer.addPosition(-1, -1, 1);
		this.vertexBuffer.addPosition(1, -1, 1);
		this.vertexBuffer.addPosition(1, 1, 1);
		this.vertexBuffer.addPosition(-1, 1, 1);
		
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		
		this.vertexBuffer.addPosition(-1, 1, 1);
		this.vertexBuffer.addPosition(1, 1, 1);
		this.vertexBuffer.addPosition(1, 1, -1);
		this.vertexBuffer.addPosition(-1, 1, -1);
		
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		
		this.vertexBuffer.addPosition(-1, 1, -1);
		this.vertexBuffer.addPosition(1, 1, -1);
		this.vertexBuffer.addPosition(1, -1, -1);
		this.vertexBuffer.addPosition(-1, -1, -1);
		
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		
		this.vertexBuffer.addPosition(1, -1, -1);
		this.vertexBuffer.addPosition(1, -1, 1);
		this.vertexBuffer.addPosition(1, 1, 1);
		this.vertexBuffer.addPosition(1, 1, -1);
		
		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		
		this.vertexBuffer.addPosition(-1, -1, 1);
		this.vertexBuffer.addPosition(-1, -1, -1);
		this.vertexBuffer.addPosition(-1, 1, -1);
		this.vertexBuffer.addPosition(-1, 1, -1);
		
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		
		this.indexBuffer = new IndexBuffer(12*3);
		this.indexBuffer.addTriangle(0, 1, 2);
		this.indexBuffer.addTriangle(0, 2, 3);
		
		this.indexBuffer.addTriangle(4, 5, 6);
		this.indexBuffer.addTriangle(4, 6, 7);
		
		this.indexBuffer.addTriangle(8, 9, 10);
		this.indexBuffer.addTriangle(8, 10, 11);
		
		this.indexBuffer.addTriangle(12, 13, 14);
		this.indexBuffer.addTriangle(12, 14, 15);
		
		this.indexBuffer.addTriangle(16, 18, 17);
		this.indexBuffer.addTriangle(16, 19, 18);
		
		this.indexBuffer.addTriangle(20, 22, 21);
		this.indexBuffer.addTriangle(20, 23, 22);
		
		this.vertexBuffer.resetBufferPosition();
		this.indexBuffer.resetBufferPosition();

	}
}
