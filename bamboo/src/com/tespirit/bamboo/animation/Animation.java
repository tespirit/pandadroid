package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.tespirit.bamboo.render.TimeUpdate;

public class Animation implements TimeUpdate, Externalizable{
	private ArrayList<Channel> channels;
	private ArrayList<Clip> clips;
	private Clip currentClip;

	private DofStream dofs;
	private boolean playing;
	
	public Animation(){
		
	}
	
	public Animation(int channelCount){
		this.init(channelCount);
	}
	
	public void init(int channelCount){
		this.channels = new ArrayList<Channel>();
		this.clips = new ArrayList<Clip>();
		this.dofs = new DofStream(channelCount);
		this.playing = false;
	}
	
	public Channel getChannel(int i){
		return channels.get(i);
	}
	
	public int getChannelCount(){
		return channels.size();
	}
	
	public void addClip(Clip clip){
		this.clips.add(clip);
		if(this.currentClip == null){
			this.currentClip = clip;
		}
	}
	
	public void activateClip(int i){
		this.currentClip = this.clips.get(i);
	}
	
	public void attachSkeleton(Joint joint){
		joint.setDofs(this.dofs);
	}
	
	public void addChannel(Channel c){
		channels.add(c);
	}
	
	public void play(long initialTime){
		this.currentClip.setInitialTime(initialTime);
		this.playing = true;
	}
	
	public void stop(){
		this.playing = false;
	}
	
	public void update(long time){
		this.dofs.reset();
		if(this.playing){
			for(Channel c : this.channels){
				this.dofs.setNext(c.getValue(this.currentClip.getClipTime(time)));
			}
			this.dofs.reset();
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
    		this.addClip(new Clip(in.readLong(), in.readLong()));
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.channels.size());
		for(Channel c : this.channels){
			out.writeObject(c);
		}
		out.writeInt(this.clips.size());
		for(Clip c : this.clips){
			out.writeLong(c.getStart());
			out.writeLong(c.getEnd());
		}
	}
}
