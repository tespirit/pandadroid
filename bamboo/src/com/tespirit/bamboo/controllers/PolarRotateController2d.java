package com.tespirit.bamboo.controllers;

public class PolarRotateController2d extends BaseMatrixController2d{
	private float mDistance;
	private float mAzmuth;
	private float mIncline;
	private float mScale;
	
	public PolarRotateController2d(float distance){
		this.mDistance = distance;
		this.mScale = 1.0f;
	}
	
	public PolarRotateController2d(){
		this(1);
	}
	

	public void setScale(float scale){
		this.mScale = scale;
	}
	
	public void setDistance(float distance){
		this.mDistance = distance;
	}
	

	@Override
	public void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime) {
		this.mAzmuth += deltaX*this.mScale;
		this.mIncline += deltaY*this.mScale;
		this.mControlled.PolarRotate(this.mDistance, this.mAzmuth, this.mIncline, 0f);
	}

}
