package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public class Channel implements Externalizable{
	private ArrayList<KeyFrame> keys;
	long lastTime;
	int lastFrame;
	
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


	//IO
	private static final long serialVersionUID = -132392613099886504L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int count = in.readInt();
    	this.keys = new ArrayList<KeyFrame>();
    	for(int i = 0; i < count; i++){
    		this.addKeyFrame(new KeyFrame(in.readFloat(), in.readLong()));
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.keys.size());
		for(KeyFrame k : this.keys){
			out.writeFloat(k.value);
			out.writeLong(k.time);
		}
	}
}
