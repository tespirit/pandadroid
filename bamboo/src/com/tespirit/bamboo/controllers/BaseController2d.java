package com.tespirit.bamboo.controllers;

public abstract class BaseController2d implements Controller2d{
	protected float mX;
	protected float mY;
	protected float mDeltaX;
	protected float mDeltaY;
	
	protected long mTime;
	protected long mDeltaTime;
	
	@Override
	public void init(float x, float y, long time){
		this.mX = x;
		this.mY = y;
		this.mTime = time;
		this.mDeltaX = 0;
		this.mDeltaY = 0;
		this.mDeltaTime = 0;
	}

	@Override
	public void set(float x, float y, long time) {
		this.mDeltaX = x - this.mX;
		this.mDeltaY = y - this.mY;
		this.mDeltaTime = time - this.mTime;
		this.mX = x;
		this.mY = y;
		this.mTime = time;
		
	}
	
}
