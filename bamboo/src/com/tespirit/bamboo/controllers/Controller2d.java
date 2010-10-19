package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.Updater;

public interface Controller2d extends Updater{	
	public void begin(float x, float y, long time);
	public void applyChange(float x, float y, long time);
	public void end();
}
