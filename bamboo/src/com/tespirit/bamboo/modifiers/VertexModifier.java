package com.tespirit.bamboo.modifiers;

import java.io.Serializable;

import com.tespirit.bamboo.primitives.VertexBuffer;

public abstract class VertexModifier implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 189440987112291791L;
	
	protected VertexBuffer mOriginalBuffer;
	protected VertexBuffer mModifiedBuffer;
	
	public void setVertexBuffer(VertexBuffer vertexBuffer){
		this.mOriginalBuffer = vertexBuffer;
		this.mModifiedBuffer = new VertexBuffer(vertexBuffer);
	}
	
	public VertexBuffer getModifiedBuffer(){
		return this.mModifiedBuffer;
	}
	
	public abstract void update();
}
