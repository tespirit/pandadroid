package com.tespirit.pandadroid.opengl1x;

import com.tespirit.bamboo.render.Clock;

public class AndroidClock implements Clock{

	private long mCurrentTime;
	private long mDeltaTime;
	
	@Override
	public long getCurrentTime() {
		return mCurrentTime;
	}

	@Override
	public long getDeltaTime() {
		return mDeltaTime;
	}

	@Override
	public void update() {
		long newTime = android.os.SystemClock.uptimeMillis();
		this.mDeltaTime = newTime - this.mCurrentTime;
		this.mCurrentTime = newTime;
	}

	@Override
	public void start() {
		this.mCurrentTime = android.os.SystemClock.uptimeMillis();
	}

}
