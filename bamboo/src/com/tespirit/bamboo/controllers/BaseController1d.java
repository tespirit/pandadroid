package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.UpdateManager;

public abstract class BaseController1d implements Controller1d{
	private float mX;
	private float mDeltaX;
	
	private long mTime;
	private long mDeltaTime;
	
	UpdateManager mUpdateManager;
	
	@Override
	public void init(float x,long time){
		this.mX = x;
		this.mTime = time;
	}

	@Override
	public void set(float x, long time) {
		this.mDeltaX += x - this.mX;
		this.mDeltaTime = time - this.mTime;
		this.mX = x;
		this.mTime = time;
		mUpdateManager.addSingleUpdater(this);
	}
	
	@Override
	public void update(){
		//TODO: syncronize this
		this.update(this.mX, this.mDeltaX, this.mTime, this.mDeltaTime);
		this.mDeltaX = 0;
		this.mDeltaTime = 0;
	}
	
	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		this.mUpdateManager = updateManager;
	}
	
	protected abstract void update(float x, float deltaX, long time, long deltaTime);
	
}
