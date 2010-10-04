package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.scenegraph.Model;

public abstract class SelectSwitchController2d extends SelectController2d{

	private Controller2d mPreviousController;
	protected Controller2d mSelectController;
	
	public SelectSwitchController2d(PandadroidView view, Controller2d selectController) {
		super(view);
		this.mSelectController = selectController;
	}
	
	public SelectSwitchController2d(PandadroidView view){
		this(view, null);
	}
	
	@Override
	public void deselect() {
		if(this.mPreviousController != null){
			this.mView.setTouchMoveController(this.mPreviousController);
			this.mPreviousController = null;
		}
	}
	
	@Override
	public void select(Model model) {
		if(this.setControlled(model)){
			this.setControlled(model);
			if(this.mPreviousController == null){
				this.mPreviousController = this.mView.getTouchMoveController();
				this.mView.setTouchMoveController(this.mSelectController);
			}
		} else {
			this.deselect();
		}
	}
	
	/**
	 * return false to deselect, ie if model is null.
	 */
	protected abstract boolean setControlled(Model model);
}
