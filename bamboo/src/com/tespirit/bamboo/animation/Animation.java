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
	
	public Animation(){
		
	}
	
	public Animation(int channelCount){
		this.init(channelCount);
	}
	
	public void init(int channelCount){
		this.mChannels = new ArrayList<Channel>();
		this.mClips = new ArrayList<Clip>();
		this.mClipLookup = new HashMap<String, Integer>();
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
	
	public int getClipId(String clipName){
		return this.mClipLookup.get(clipName);
	}
	
	public void addChannel(Channel c){
		mChannels.add(c);
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
		int count = in.readInt();
    	this.init(count);
    	for(int i = 0; i < count; i++){
    		this.addChannel((Channel)in.readObject());
    	}
    	
    	count = in.readInt();
    	for(int i = 0; i < count; i++){
    		this.addClip(new Clip(in.readUTF(), in.readLong(), in.readLong()));
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.mChannels.size());
		for(Channel c : this.mChannels){
			out.writeObject(c);
		}
		out.writeInt(this.mClips.size());
		for(Clip c : this.mClips){
			out.writeUTF(c.getName());
			out.writeLong(c.getStart());
			out.writeLong(c.getEnd());
		}
	}
}
