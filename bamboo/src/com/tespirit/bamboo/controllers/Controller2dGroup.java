package com.tespirit.bamboo.controllers;

import java.util.ArrayList;

import com.tespirit.bamboo.render.UpdateManager;

/**
 * This allows for creating a list of 2d controllers to execute
 * @author Todd Espiritu Santo
 *
 */
public class Controller2dGroup implements Controller2d {

	private ArrayList<Controller2d> mControllers;

	@Override
	public void update() {
		for(Controller2d controller : this.mControllers){
			controller.update();
		}
	}

	@Override
	public void init(float x, float y, long time) {
		for(Controller2d controller : this.mControllers){
			controller.init(x, y, time);
		}
	}

	@Override
	public void set(float x, float y, long time) {
		for(Controller2d controller : this.mControllers){
			controller.set(x, y, time);
		}
	}

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		for(Controller2d controller : this.mControllers){
			controller.setUpdateManager(updateManager);
		}
	}

}
