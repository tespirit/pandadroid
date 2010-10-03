package com.tespirit.bamporter.opengl;

import java.util.Calendar;

import com.tespirit.bamboo.render.Clock;

public class CalendarClock implements Clock{
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
		long newTime = Calendar.getInstance().getTimeInMillis();
		this.mDeltaTime = newTime - this.mCurrentTime;
		this.mCurrentTime = newTime;
	}

	@Override
	public void start() {
		this.mCurrentTime = Calendar.getInstance().getTimeInMillis();
	}
}
