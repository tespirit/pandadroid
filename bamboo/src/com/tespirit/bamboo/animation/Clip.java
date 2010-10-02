package com.tespirit.bamboo.animation;

public class Clip {

	private String mName;
	private long mStart;
	private long mEnd;
	
	public Clip(String name, long start, long end){
		this.mName = name;
		this.mStart = start;
		this.mEnd = end;
	}
	
	public String getName(){
		return this.mName;
	}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public long getClipTime(long currentTime){
		return (currentTime -this.mStart)%this.mEnd + this.mStart;
	}
	
	public long getStart(){
		return this.mStart;
	}
	
	public long getEnd(){
		return this.mEnd;
	}
}
