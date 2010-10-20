package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public class StandardCameraController2d extends BaseMatrixController2d{
	private float mDistance;
	private float mAzmuth;
	private float mIncline;
	private float mScale;
	private Vector3d mCenter;
	private ControlState mSate;
	
	public static enum ControlState{
		pan,
		zoom,
		rotate,
		focusDistance
	}
	
	public StandardCameraController2d(){
		this.mDistance = 0.0f;
		this.mScale = 1.0f;
		this.mCenter = new Vector3d();
		this.mSate = ControlState.rotate;
	}
	
	public void setState(ControlState state){
		this.mSate = state;
	}

	public void setScale(float scale){
		this.mScale = scale;
	}
	
	public void set(float distance, float azmuth, float incline){
		this.mAzmuth = azmuth;
		this.mIncline = incline;
		this.mDistance = distance;
		this.mUpdateManager.addSingleUpdater(this);
	}
	
	public void setDistance(float d){
		this.mDistance = d;
		this.mUpdateManager.addSingleUpdater(this);
	}
	
	public void setCenter(Vector3d center){
		this.mCenter.copy(center);
		this.mUpdateManager.addSingleUpdater(this);
	}
	
	@Override
	public void applyChange(float x, float y, long time){
		super.applyChange(x, y, time);
		switch(mSate){
			case rotate:
				this.mAzmuth += this.mDeltaX*this.mScale;
				this.mIncline += this.mDeltaY*this.mScale;
				break;
			case pan:
				this.mCenter.addScale(this.mControlled.getXAxis(),this.mDeltaX*this.mScale*0.05f);
				this.mCenter.addScale(this.mControlled.getYAxis(),-this.mDeltaY*this.mScale*0.05f);
				break;
			case zoom:
				this.mCenter.addScale(this.mControlled.getZAxis(),this.mDeltaX*this.mScale*0.1f);
				break;
			case focusDistance:
				this.mDistance += this.mDeltaY*this.mScale;
		}
	}

	@Override
	public void update(float x, float y, float deltaX, float deltaY, long time,
			long deltaTime) {
		this.mControlled.PolarRotate(this.mDistance, this.mAzmuth, this.mIncline, 0f);
		this.mControlled.translate(this.mCenter);
	}

}
