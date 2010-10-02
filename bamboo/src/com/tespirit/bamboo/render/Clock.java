package com.tespirit.bamboo.render;

public interface Clock {
	public long getCurrentTime();
	public long getDeltaTime();
	public void update();
}
