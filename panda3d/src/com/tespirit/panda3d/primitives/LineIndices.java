package com.tespirit.panda3d.primatives;

public class LineIndices extends LineList{
	private IndexBuffer indexBuffer;
	
	public LineIndices(int pointCount, int indexCount){
		super(pointCount);
		this.indexBuffer = new IndexBuffer(indexCount, pointCount);
	}
	
	public IndexBuffer getIndexBuffer(){
		return this.indexBuffer;
	}
}
