package com.tespirit.bamboo.animation;

public class Clip {

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
	
	public long getStart(){
		return this.mStart;
	}
	
	public long getEnd(){
		return this.mEnd;
	}
}
