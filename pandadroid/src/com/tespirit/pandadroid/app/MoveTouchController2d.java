package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.MoveController2d;
import com.tespirit.bamboo.scenegraph.Model;

public class MoveTouchController2d extends SelectSwitchController2d{
	public MoveTouchController2d(PandadroidView view){
		super(view, new MoveController2d(view.getRenderer()));
	}

	@Override
	protected boolean setControlled(Model model) {
		if(model == null){
			return false;
		}
		((MoveController2d)this.mSelectController).setControlled(model);
		return true;
	}

}
