package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.scenegraph.Model;

public abstract class SelectController2d implements Controller2d{
	protected PandadroidView view;
	protected float x;
	protected float y;
	protected long time;
	
	public SelectController2d(PandadroidView view){
		this.view = view;
	}

	@Override
	public void update(float x, float y) {
		this.x = x;
		this.y = y;
		this.select(this.view.getRenderer().selectModel(x, y));
	}

	@Override
	public void update(float x, float y, long time) {
		this.x = x;
		this.y = y;
		this.time = time;
		this.select(this.view.getRenderer().selectModel(x, y));
	}
	
	public abstract void select(Model model);
	public abstract void deselect();
}
