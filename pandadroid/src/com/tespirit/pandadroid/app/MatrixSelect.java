package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.MatrixController2d;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.scenegraph.Model;

public class MatrixSelect extends SelectController2d{
	MatrixController2d selectController;
	Controller2d previousController;

	public MatrixSelect(PandadroidView view, boolean rotate) {
		super(view);
		if(rotate){
			this.selectController = new RotateController2d(Dof3.Y, Dof3.X);
			this.selectController.setScale(0.25f);
		} else {
			this.selectController = new TranslateController2d(Dof3.X, Dof3.negativeY);
			this.selectController.setScale(0.01f);
		}
		
	}

	@Override
	public void deselect() {
		if(this.previousController != null){
			this.view.setTouchMoveController(this.previousController);
			this.previousController = null;
		}
	}

	@Override
	public void select(Model model) {
		if(model == null){
			this.deselect();
		} else {
			this.selectController.setControlled(model);
			if(this.previousController == null){
				this.previousController = this.view.getTouchMoveController();
				this.view.setTouchMoveController(this.selectController);
			}
		}
	}

}
