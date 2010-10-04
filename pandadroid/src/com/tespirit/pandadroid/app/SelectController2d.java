package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.BaseController2d;
import com.tespirit.bamboo.scenegraph.Model;

public abstract class SelectController2d extends BaseController2d{
	protected PandadroidView mView;
	
	public SelectController2d(PandadroidView view){
		this.mView = view;
	}

	@Override
	protected void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime) {
		this.select(this.mView.getRenderer().selectModel(x, y));
	}

	public abstract void select(Model model);
	public abstract void deselect();
}
