package com.tespirit.panda3d.controllers;

import com.tespirit.panda3d.vectors.Vector3d;

public class RotateController2d extends MatrixController2d{
	
	public RotateController2d(){
		super();
	}
	
	public RotateController2d(Dof3 xMap, Dof3 yMap){
		super(xMap,yMap);
	}
	
	public RotateController2d(Vector3d xMapAxis, Vector3d yMapAxis){
		super(xMapAxis,yMapAxis);
	}

	@Override
	public void update(float x, float y) {
		this.m.rotateAxis(this.scale*x, this.xMapAxis);
		this.m.rotateAxis(this.scale*y, this.yMapAxis);
	}

	@Override
	public void update(float x, float y, long time) {
		this.update(x, y);
	}

}
