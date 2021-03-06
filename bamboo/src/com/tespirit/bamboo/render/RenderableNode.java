package com.tespirit.bamboo.render;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;

public abstract class RenderableNode extends Node{
	
	public RenderableNode(){
		this(null);
	}
	
	public RenderableNode(String name){
		super(name);
	}
	
	@Override
	public void update(Matrix3d transform){
		this.getRenderManager().addRenderable(this);
	}
	
	protected void registerResource(Resource dynamicLoader){
		if(this.getRenderManager() != null){
			this.getRenderManager().loadResource(dynamicLoader);
		}
	}
	
	protected void unregisterResource(Resource dynamicLoader){
		if(this.getRenderManager() != null){
			this.getRenderManager().unloadResource(dynamicLoader);
		}
	}
	
	public abstract boolean alphaSort();
	
	
	/**
	 * This is called every frame in rendering the scene.
	 */
	public abstract void render();
}
