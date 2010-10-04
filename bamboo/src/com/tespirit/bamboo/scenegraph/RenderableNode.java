package com.tespirit.bamboo.scenegraph;

public abstract class RenderableNode extends Node{
	
	public RenderableNode(){
		this(null);
	}
	
	public RenderableNode(String name){
		super(name);
	}
	
	/**
	 * This is called every frame in rendering the scene.
	 */
	public abstract void render();
}
