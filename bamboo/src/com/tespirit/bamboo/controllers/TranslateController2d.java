package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class TranslateController2d extends AxisController2d{
	Vector3d mTemp;
	
	public TranslateController2d(){
		super();
		this.mTemp = new Vector3d();
	}
	
	public TranslateController2d(Dof3 xMap, Dof3 yMap){
		super(xMap,yMap);
		this.mTemp = new Vector3d();
	}
	
	public TranslateController2d(Vector3d xMapAxis, Vector3d yMapAxis){
		super(xMapAxis,yMapAxis);
		this.mTemp = new Vector3d();
	}

	@Override
	public void update() {
		this.mTemp.copy(this.mXAxis);
		this.mTemp.scale(this.mDeltaX*this.mScale);
		this.mControlled.translate(this.mTemp);
		
		this.mTemp.copy(this.mYAxis);
		this.mTemp.scale(this.mDeltaY*this.mScale);
		this.mControlled.translate(this.mTemp);
	}

}
