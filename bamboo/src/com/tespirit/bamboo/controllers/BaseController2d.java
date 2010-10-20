package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.UpdateManager;

public abstract class BaseController2d implements Controller2d{
	protected float mX;
	protected float mY;
	protected float mDeltaX;
	protected float mDeltaY;
	
	protected long mTime;
	protected long mDeltaTime;
	
	protected UpdateManager mUpdateManager;
	
	@Override
	public void begin(float x, float y, long time){
		this.mX = x;
		this.mY = y;
		this.mTime = time;
	}

	@Override
	public void applyChange(float x, float y, long time) {
		this.mDeltaX += x - this.mX;
		this.mDeltaY += y - this.mY;
		this.mDeltaTime = time - this.mTime;
		this.mX = x;
		this.mY = y;
		this.mTime = time;
		mUpdateManager.addSingleUpdater(this);
	}
	
	@Override
	public void end(){
		//VOID
	}
	
	public void sendUpdate(){
		mUpdateManager.addSingleUpdater(this);
	}
	
	@Override
	public void update(){
		//TODO: syncronize this
		this.update(this.mX, this.mY, this.mDeltaX, this.mDeltaY, this.mTime, this.mDeltaTime);
		this.mDeltaX = 0;
		this.mDeltaY = 0;
		this.mDeltaTime = 0;
	}
	
	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		this.mUpdateManager = updateManager;
	}
	
	public abstract void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime);
	
}
