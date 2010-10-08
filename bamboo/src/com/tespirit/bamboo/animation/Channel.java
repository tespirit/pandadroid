package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public class Channel implements Externalizable{
	private ArrayList<KeyFrame> mKeys;
	private long mTotalLength;
	private long mPrevTime;
	private int mPrevFrame;
	
	public Channel(){
		this.mKeys = new ArrayList<KeyFrame>();
	}
	
	public void reserve(int keyFrameCount){
		this.mKeys.ensureCapacity(keyFrameCount);
	}
	
	public static class KeyFrame{
		private float mValue;
		private long mTime;
		
		/**
		 * 
		 * @param value
		 * @param time - milliseconds
		 */
		public KeyFrame(float value, long time){
			this.mValue = value;
			this.mTime = time;
		}
		
		//used to clone a keyframe at a time offset
		public KeyFrame(KeyFrame keyFrame, long timeOffset){
			this.mValue = keyFrame.mValue;
			this.mTime = keyFrame.mTime + timeOffset;
		}
		
		public void setTime(long time){
			this.mTime = time;
		}
		
		public float getValue(){
			return this.mValue;
		}
		
		public long getTime(){
			return this.mTime;
		}
	}
	
	public void removeKeyFrames(long start, long end){
		ArrayList<KeyFrame> frames = new ArrayList<KeyFrame>();
		
		for(KeyFrame frame : this.mKeys){
			if(frame.mTime > start && frame.mTime < end){
				frames.add(frame);
			}
		}
		
		this.mKeys.removeAll(frames);
	}
	
	public int getKeyFrameCount(){
		return this.mKeys.size();
	}
	
	public KeyFrame getKeyFrame(int i){
		return this.mKeys.get(i);
	}
	
	public long getTotalLength(){
		return this.mTotalLength;
	}
	
	/**
	 * This currently does not sort, so data inputed must be sorted.
	 * @param k
	 */
	public void addKeyFrame(KeyFrame k){
		this.mKeys.add(k);
		this.mTotalLength = k.mTime;
	}
	
	/**
	 * 
	 * @param time milliseconds
	 * @return
	 */
	public float getValue(long time){
		if(this.mKeys.size() == 0) return 0;
		if(time < this.mPrevTime){
			this.mPrevFrame = 0;
		}
		this.mPrevTime = time;
		for(; this.mPrevFrame < this.mKeys.size(); this.mPrevFrame++){
			KeyFrame frame = this.mKeys.get(this.mPrevFrame);
			if(frame.mTime > time){
				//for now no interpolation
				if(this.mPrevFrame > 0){
					this.mPrevFrame--;
				}
				return this.mKeys.get(this.mPrevFrame).mValue;
			}
		}
		this.mPrevFrame = 0;
		return this.mKeys.get(this.mKeys.size()-1).mValue;
	}


	//IO
	private static final long serialVersionUID = -132392613099886504L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int count = in.readInt();
    	this.mKeys = new ArrayList<KeyFrame>(count);
    	for(int i = 0; i < count; i++){
    		this.addKeyFrame(new KeyFrame(in.readFloat(), in.readLong()));
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.mKeys.size());
		for(KeyFrame k : this.mKeys){
			out.writeFloat(k.mValue);
			out.writeLong(k.mTime);
		}
	}
}
