package com.tespirit.bamboo.animation;

import java.util.ArrayList;

public class Channel {
	private ArrayList<KeyFrame> keys;
	long lastTime;
	int lastFrame;
	long endTime;
	long startTime;
	
	public Channel(){
		this.keys = new ArrayList<KeyFrame>();
	}
	
	public static class KeyFrame{
		float value;
		long time;
		
		/**
		 * 
		 * @param value
		 * @param time - milliseconds
		 */
		public KeyFrame(float value, long time){
			this.value = value;
			this.time = time;
		}
		
	}
	
	public void addKeyFrame(KeyFrame k){
		this.keys.add(k);
	}
	
	public void setRange(long start, long end){
		this.startTime = start;
		this.endTime = end;
	}
	
	/**
	 * 
	 * @param time milliseconds
	 * @return
	 */
	public float getValue(long time){
		if(this.keys.size() == 0) return 0;
		time = ((time-this.startTime)% this.endTime)+this.startTime;
		if(time < this.lastTime){
			this.lastFrame = 0;
		}
		this.lastTime = time;
		for(; this.lastFrame < this.keys.size(); this.lastFrame++){
			KeyFrame frame = this.keys.get(this.lastFrame);
			if(frame.time > time){
				//for now no interpolation
				if(this.lastFrame > 0){
					this.lastFrame--;
				}
				return this.keys.get(this.lastFrame).value;
			}
		}
		this.lastFrame = 0;
		return this.keys.get(this.keys.size()-1).value;
	}
}
