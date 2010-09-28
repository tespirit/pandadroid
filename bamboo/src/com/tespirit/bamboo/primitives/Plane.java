package com.tespirit.bamboo.primitives;

public class Plane extends TriangleIndices{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6902879558888631757L;

	public Plane(){
		super(6, 4, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD});
		
		this.vertexBuffer.addPosition(-0.5f,  0.5f, 0.0f);
		this.vertexBuffer.addPosition(-0.5f, -0.5f, 0.0f);
		this.vertexBuffer.addPosition(0.5f, -0.5f, 0.0f);
		this.vertexBuffer.addPosition(0.5f,  0.5f, 0.0f);
		
		this.vertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
		this.vertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
		this.vertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
		this.vertexBuffer.addNormal(0.0f, 0.0f, 1.0f);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
		
		this.indexBuffer.addTriangle(0, 1, 2);
		this.indexBuffer.addTriangle(0, 2, 3);
				
		this.vertexBuffer.resetBufferPosition();
		this.indexBuffer.resetBufferPosition();
	}
}
