package com.tespirit.panda3d.controllers;

import java.util.ArrayList;

/**
 * This allows for creating a list of 2d controllers to execute
 * @author Todd Espiritu Santo
 *
 */
public class Controller2dGroup implements Controller2d {

	private ArrayList<Controller2d> controllers;
	
	@Override
	public void update(float x, float y) {
		for(Controller2d controller : this.controllers){
			controller.update(x, y);
		}
	}

	@Override
	public void update(float x, float y, long time) {
		for(Controller2d controller : this.controllers){
			controller.update(x, y, time);
		}
	}

}
