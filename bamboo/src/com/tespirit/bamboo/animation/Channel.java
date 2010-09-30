package com.tespirit.bamboo.animation;

import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2281752068919363207L;
	private ArrayList<KeyFrame> keys;
	long lastTime;
	int lastFrame;
	
	public Channel(){
		this.keys = new ArrayList<KeyFrame>();
	}
	
	public static class KeyFrame implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3417055351101570333L;
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
	
	public int getKeyFrameCount(){
		return this.keys.size();
	}
	
	public void addKeyFrame(KeyFrame k){
		this.keys.add(k);
	}
	
	/**
	 * 
	 * @param time milliseconds
	 * @return
	 */
	public float getValue(long time){
		if(this.keys.size() == 0) return 0;
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
