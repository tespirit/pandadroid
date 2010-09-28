package com.tespirit.bamboo.animation;

import java.io.Serializable;

public class Clip implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4458717474108576702L;

	private long mInitialTime;
	private long mStart;
	private long mEnd;
	
	public Clip(long start, long end){
		this.mStart = start;
		this.mEnd = end;
	}
	
	public void setInitialTime(long initialTime){
		this.mInitialTime = initialTime;
	}
	
	public long getClipTime(long currentTime){
		return ((currentTime - this.mInitialTime)-this.mStart)%this.mEnd + this.mStart;
	}
}
