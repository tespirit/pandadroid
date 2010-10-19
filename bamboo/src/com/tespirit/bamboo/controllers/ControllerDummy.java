package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.UpdateManager;

public class ControllerDummy implements Controller1d, Controller2d, Controller3d{

	private static ControllerDummy instance = new ControllerDummy();
	
	public static ControllerDummy getInstance(){
		return ControllerDummy.instance;
		
	}

	@Override
	public void init(float z, long time) {
		// VOID
	}

	@Override
	public void set(float x, long time) {
		// VOID
	}

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		// VOID
	}

	@Override
	public void update() {
		// VOID
	}

	@Override
	public void begin(float x, float y, long time) {
		// VOID
	}

	@Override
	public void applyChange(float x, float y, long time) {
		// VOID
	}
	
	@Override
	public void end(){
		//VOID
	}

	@Override
	public void init(float x, float y, float z, long time) {
		// VOID
	}

	@Override
	public void set(float x, float y, float z, long time) {
		// VOID
	}
}
