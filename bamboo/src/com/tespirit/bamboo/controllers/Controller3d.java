package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.Updater;

public interface Controller3d extends Updater{
	public void set(float x, float y, float z, long time);
	public void init(float x, float y, float z, long time);
}
