package com.tespirit.bamboo.modifiers;

import com.tespirit.bamboo.primitives.VertexBuffer;

public abstract class VertexModifier implements Modifier{
	protected VertexBuffer mOriginalBuffer;
	protected VertexBuffer mModifiedBuffer;
	
	public void setVertexBuffer(VertexBuffer vertexBuffer){
		this.mOriginalBuffer = vertexBuffer;
		this.mModifiedBuffer = new VertexBuffer(vertexBuffer);
	}
	
	public VertexBuffer getModifiedBuffer(){
		return this.mModifiedBuffer;
		//return this.mOriginalBuffer;
	}
}
