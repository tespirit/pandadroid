package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.AxisController2d;
import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.MatrixController2d;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.scenegraph.Model;

public class MatrixSelect extends SelectSwitchController2d{

	public MatrixSelect(PandadroidView view, boolean rotate) {
		super(view);
		AxisController2d controller;
		if(rotate){
			controller = new RotateController2d(Dof3.Y, Dof3.X);
			controller.setScale(0.25f);
		} else {
			controller = new TranslateController2d(Dof3.X, Dof3.negativeY);
			controller.setScale(0.01f);
		}
		this.mSelectController = controller;
	}

	@Override
	protected boolean setControlled(Model model) {
		if(model == null){
			return false;
		}
		((MatrixController2d)this.mSelectController).setControlled(model);
		return true;
	}

}
