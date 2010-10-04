package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.Updater;

public interface Controller2d extends Updater{
	public void init(float x, float y, long time);
	public void set(float x, float y, long time);
}
