package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.Updater;

public interface Controller1d extends Updater {
	public void set(float x, long time);
	public void init(float z, long time);
}
