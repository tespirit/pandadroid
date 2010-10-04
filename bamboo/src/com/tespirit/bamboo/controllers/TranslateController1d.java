package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class TranslateController1d extends AxisController1d{

	Vector3d mTemp;
	
	public TranslateController1d(Dof3 axis) {
		super(axis);
		this.mTemp = new Vector3d();
	}

	@Override
	protected void update(float x, float deltaX, long time, long deltaTime) {
		this.mTemp.copy(this.mAxis);
		this.mTemp.scale(deltaX*this.mScale);
		this.mControlled.translate(this.mTemp);
	}
}
