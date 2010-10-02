package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class TranslateController1d extends MatrixController1d{

	Vector3d mTemp;
	
	public TranslateController1d(Dof3 axis) {
		super(axis);
		this.mTemp = new Vector3d();
	}

	@Override
	public void update(float x) {
		this.mTemp.copy(this.mAxis);
		this.mTemp.scale(x*this.mScale);
		this.mMatrix.translate(this.mTemp);
	}

	@Override
	public void update(float x, long time) {
		this.update(x);
	}

}
