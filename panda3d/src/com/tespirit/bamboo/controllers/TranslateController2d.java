package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class TranslateController2d extends MatrixController2d{
	Vector3d temp;
	
	public TranslateController2d(){
		super();
		this.temp = new Vector3d();
	}
	
	public TranslateController2d(Dof3 xMap, Dof3 yMap){
		super(xMap,yMap);
		this.temp = new Vector3d();
	}
	
	public TranslateController2d(Vector3d xMapAxis, Vector3d yMapAxis){
		super(xMapAxis,yMapAxis);
		this.temp = new Vector3d();
	}

	@Override
	public void update(float x, float y) {
		this.temp.copy(this.xMapAxis);
		this.temp.scale(x*this.scale);
		this.m.translate(this.temp);
		
		this.temp.copy(this.yMapAxis);
		this.temp.scale(y*this.scale);
		this.m.translate(this.temp);
	}
	
	@Override
	public void update(float x, float y, long time) {
		this.update(x, y);
	}

}
