package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.BaseController2d;
import com.tespirit.bamboo.scenegraph.Model;

public abstract class SelectController2d extends BaseController2d{
	protected PandadroidView mView;
	
	public SelectController2d(PandadroidView view){
		this.mView = view;
	}

	@Override
	public void update() {
		this.select(this.mView.getRenderer().selectModel(this.mX, this.mY));
	}

	public abstract void select(Model model);
	public abstract void deselect();
}
