package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.UpdateManager;

public abstract class BaseController3d implements Controller3d{
	private float[] mValues;
	
	protected static final int X = 0;
	protected static final int Y = 1;
	protected static final int Z = 2;
	protected static final int DELTA_X = 3;
	protected static final int DELTA_Y = 4;
	protected static final int DELTA_Z = 5;
	
	private long mTime;
	private long mDeltaTime;
	
	UpdateManager mUpdateManager;
	
	protected BaseController3d(){
		this.mValues = new float[6];
	}
	
	@Override
	public void init(float x, float y, float z, long time){
		this.mValues[0] = x;
		this.mValues[1] = y;
		this.mValues[2] = z;
		this.mTime = time;
	}

	@Override
	public void set(float x, float y, float z, long time) {
		this.mValues[3] = x - this.mValues[0];
		this.mValues[4] = y - this.mValues[1];
		this.mValues[5] = z - this.mValues[2];
		this.mValues[0] = x;
		this.mValues[1] = y;
		this.mValues[2] = z;
		
		this.mDeltaTime = time - this.mTime;
		this.mTime = time;
		
		mUpdateManager.addSingleUpdater(this);
	}
	
	@Override
	public void update(){
		//TODO: syncronize this
		this.update(this.mValues.clone(), this.mTime, this.mDeltaTime);
		this.mValues[3] = 0;
		this.mValues[4] = 0;
		this.mValues[5] = 0;
		this.mDeltaTime = 0;
	}
	
	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		this.mUpdateManager = updateManager;
	}
	
	protected abstract void update(float[] values, long time, long deltaTime);
	
}
