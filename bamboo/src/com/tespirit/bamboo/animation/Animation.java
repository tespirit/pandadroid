package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation implements Externalizable{
	private List<Channel> mChannels;
	private List<Clip> mClips;
	private Map<String, Integer> mClipLookup;
	private String mName;
	private long mTotalLength;
	
	private static Map<String, Animation> mLoadedAnimations = new HashMap<String, Animation>();
	
	public static Animation getAnimation(String name){
		return Animation.mLoadedAnimations.get(name);
	}
	
	public Animation(String name, int channelCount){
		this(name);
		this.init(channelCount,10);
	}
	
	public Animation(int channelCount){
		this(null);
		this.init(channelCount,10);
	}
	
	public Animation(String name){
		this.setName(name);
	}
	
	public Animation(){
		this(null);
	}
	
	public void setName(String name){
		if(this.getName() != null){
			Animation.mLoadedAnimations.remove(this.mName);
		}
		this.mName = name;
		if(name != null && name.length() > 0){
			Animation.mLoadedAnimations.put(this.mName, this);
		} else {
			this.mName = "";
		}
	}
	
	public String getName(){
		if(this.mName != null && this.mName.length() > 0){
			return this.mName;
		} else {
			return null;
		}
	}
	
	public void init(int channelCount, int clipCount){
		this.mChannels = new ArrayList<Channel>(channelCount);
		this.mClips = new ArrayList<Clip>(clipCount);
		this.mClipLookup = new HashMap<String, Integer>(channelCount);
	}
	
	public Channel getChannel(int i){
		return mChannels.get(i);
	}
	
	public int getChannelCount(){
		return mChannels.size();
	}
	
	public int addClip(Clip clip){
		this.mClips.add(clip);
		int id = this.mClips.size()-1;
		this.mClipLookup.put(clip.getName(), id);
		return this.mClips.size()-1;
	}
	
	public boolean hasClip(String clipName){
		return this.mClipLookup.containsKey(clipName);
	}
	
	public int getClipId(String clipName){
		return this.mClipLookup.get(clipName);
	}
	
	public int getClipCount(){
		return this.mClips.size();
	}
	
	public Clip getClip(int i){
		return this.mClips.get(i);
	}
	
	/**
	 * This should only be used in editing applications as clip id's will
	 * change.
	 * @param clip
	 */
	public void removeClip(Clip clip){
		this.mClips.remove(clip);
		this.mClipLookup.remove(clip.getName());
		clip = null;
	}

	public void addChannel(Channel c){
		mChannels.add(c);
		if(c.getTotalLength() > this.mTotalLength){
			this.mTotalLength = c.getTotalLength();
		}
	}
	
	public long getTotalLength(){
		return this.mTotalLength;
	}
	
	public void update(int clipId, long time, DofStream dofs){
		Clip clip = this.mClips.get(clipId);
		for(Channel c : this.mChannels){
			dofs.setNext(c.getValue(clip.getClipTime(time)));
		}
	}

	//IO
	private static final long serialVersionUID = 61212447333296652L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.setName(in.readUTF());
		int channelCount = in.readInt();
		int clipCount = in.readInt();
    	this.init(channelCount, clipCount);
    	for(int i = 0; i < channelCount; i++){
    		this.addChannel((Channel)in.readObject());
    	}
    	
    	for(int i = 0; i < clipCount; i++){
    		this.addClip(new Clip(in.readUTF(), in.readLong(), in.readLong()));
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.mName);
		out.writeInt(this.mChannels.size());
		out.writeInt(this.mClips.size());
		for(Channel c : this.mChannels){
			out.writeObject(c);
		}
		for(Clip c : this.mClips){
			out.writeUTF(c.getName());
			out.writeLong(c.getStart());
			out.writeLong(c.getEnd());
		}
	}
}
