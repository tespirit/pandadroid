package com.tespirit.panda3d.app;

import com.tespirit.panda3d.controllers.Controller2d;
import com.tespirit.panda3d.controllers.Dof3;
import com.tespirit.panda3d.controllers.MatrixController2d;
import com.tespirit.panda3d.controllers.RotateController2d;
import com.tespirit.panda3d.controllers.TranslateController2d;
import com.tespirit.panda3d.scenegraph.Model;

public class MatrixSelect extends SelectController2d{
	MatrixController2d selectController;
	Controller2d previousController;

	public MatrixSelect(Panda3dView view, boolean rotate) {
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
		}
	}

	@Override
	public void select(Model model) {
		if(model == null){
			this.deselect();
		} else {
			this.previousController = this.view.getTouchMoveController();
			this.selectController.setControlled(model);
			this.view.setTouchMoveController(this.selectController);
		}
	}

}
