package com.tespirit.panda3d.app;

import com.tespirit.panda3d.controllers.Controller2d;
import com.tespirit.panda3d.scenegraph.Model;

public abstract class SelectController2d implements Controller2d{
	protected Panda3dView view;
	public SelectController2d(Panda3dView view){
		this.view = view;
	}

	@Override
	public void update(float x, float y) {
		this.select(this.view.getRenderer().selectModel(x, y));
	}

	@Override
	public void update(float x, float y, long time) {
		this.select(this.view.getRenderer().selectModel(x, y));
	}
	
	public abstract void select(Model model);
	public abstract void deselect();
}
