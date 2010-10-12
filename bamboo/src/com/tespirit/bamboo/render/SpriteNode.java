package com.tespirit.bamboo.render;

import com.tespirit.bamboo.vectors.Matrix3d;

public abstract class SpriteNode extends RenderableNode{

	public SpriteNode(){
		super();
	}
	
	public SpriteNode(String name){
		super(name);
	}
	
	@Override
	public void update(Matrix3d transform){
		this.getRenderManager().addSprite(this);
	}
}
