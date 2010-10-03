package com.tespirit.pandadroid.debug;

import com.tespirit.bamboo.render.Clock;

public class DebugClock implements Clock{
	private long mCurrentTime;
	private static final long mDeltaTime = 1000/30;

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
		this.mCurrentTime += mDeltaTime;
	}

	@Override
	public void start() {
		//VOID
	}
}
