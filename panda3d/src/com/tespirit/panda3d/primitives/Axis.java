package com.tespirit.panda3d.primitives;

public class Axis extends LineList{

	public Axis() {
		super(6, new int[]{VertexBuffer.POSITION, VertexBuffer.COLOR});
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(1, 0, 0);
		
		this.vertexBuffer.addColor(1, 0, 0);
		this.vertexBuffer.addColor(1, 0, 0);
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(0, 1, 0);
		
		this.vertexBuffer.addColor(0, 1, 0);
		this.vertexBuffer.addColor(0, 1, 0);
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(0, 0, 1);
		
		this.vertexBuffer.addColor(0, 0, 1);
		this.vertexBuffer.addColor(0, 0, 1);
		
		this.vertexBuffer.resetBufferPosition();
	}

}
