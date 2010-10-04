package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class RotateController2d extends AxisController2d{
	
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
	protected void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime) {
		this.mControlled.rotateAxis(this.mScale*deltaX, this.mXAxis);
		this.mControlled.rotateAxis(this.mScale*deltaY, this.mYAxis);
	}

}
