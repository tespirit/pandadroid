package com.tespirit.bamboo.animation;

import java.io.Serializable;
import java.util.ArrayList;

import com.tespirit.bamboo.render.TimeUpdate;

import android.os.SystemClock;

public class Animation implements TimeUpdate, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2061900709721915476L;
	ArrayList<Channel> channels;
	DofStream dofs;
	long time;
	long startTime;
	boolean playing;
	Clip currentClip;
	ArrayList<Clip> clips;
	
	public Animation(int channels){
		this.channels = new ArrayList<Channel>();
		this.dofs = new DofStream(channels);
		this.playing = false;
		this.clips = new ArrayList<Clip>();
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
}
