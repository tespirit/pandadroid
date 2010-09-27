package com.tespirit.bamboo.animation;

import java.util.ArrayList;

import com.tespirit.bamboo.render.TimeUpdate;

import android.os.SystemClock;

public class Animation implements TimeUpdate{
	ArrayList<Channel> channels;
	DofStream dofs;
	long time;
	long startTime;
	boolean playing;
	
	public Animation(int channels){
		this.channels = new ArrayList<Channel>();
		this.dofs = new DofStream(channels);
		this.playing = false;
	}
	
	public void attachSkeleton(Joint joint){
		joint.setDofs(this.dofs);
	}
	
	public void addChannel(Channel c){
		channels.add(c);
	}
	
	public void play(){
		this.startTime = SystemClock.elapsedRealtime();
		this.playing = true;
	}
	
	public void stop(){
		this.playing = false;
	}
	
	public void rewind(){
		this.startTime = SystemClock.elapsedRealtime();
	}
	
	public void update(long time){
		this.dofs.reset();
		if(this.playing){
			for(Channel c : this.channels){
				this.dofs.setNext(c.getValue(time-this.startTime));
			}
			this.dofs.reset();
		}
	}
}
