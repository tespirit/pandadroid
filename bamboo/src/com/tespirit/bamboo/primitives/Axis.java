package com.tespirit.bamboo.primitives;

public class Axis extends LineList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 567484416852582765L;

	public Axis() {
		super(6, new int[]{VertexBuffer.POSITION, VertexBuffer.COLOR});
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(1, 0, 0);
		
		this.vertexBuffer.addColor(0.5f, 0.5f, 0.5f);
		this.vertexBuffer.addColor(1, 0, 0);
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(0, 1, 0);
		
		this.vertexBuffer.addColor(0.5f, 0.5f, 0.5f);
		this.vertexBuffer.addColor(0, 1, 0);
		
		this.vertexBuffer.addPosition(0, 0, 0);
		this.vertexBuffer.addPosition(0, 0, 1);
		
		this.vertexBuffer.addColor(0.5f, 0.5f, 0.5f);
		this.vertexBuffer.addColor(0, 0, 1);
		
		this.vertexBuffer.resetBufferPosition();
	}

}