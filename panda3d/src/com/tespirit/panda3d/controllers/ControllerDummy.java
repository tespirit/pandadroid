package com.tespirit.panda3d.controllers;

public class ControllerDummy implements Controller1d, Controller2d, Controller3d{

	private static ControllerDummy instance = new ControllerDummy();
	
	public static ControllerDummy getInstance(){
		return ControllerDummy.instance;
		
	}
	
	private ControllerDummy(){
		
	}
	
	@Override
	public void update(float x) {
		//VOID
	}

	@Override
	public void update(float x, long time) {
		//VOID
	}

	@Override
	public void update(float x, float y) {
		//VOID
	}

	@Override
	public void update(float x, float y, long time) {
		//VOID
	}

	@Override
	public void update(float x, float y, float z) {
		//VOID
	}

	@Override
	public void update(float x, float y, float z, long time) {
		//VOID
	}
}
