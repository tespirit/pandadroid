package com.tespirit.bamboo.animation;

import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.TimeUpdate;
import com.tespirit.bamboo.scenegraph.Node;

/**
 * This class is responsible for handling playing of animations to skeletons.
 * @author Todd Espiritu Santo
 *
 */
public class Player implements TimeUpdate{
	Animation mAnimation;
	Joint mSkeleton;
	long mCurrentTime;
	Clock mClock;
	int mCurrentClipId;
	DofStream mDofs;
	State mState;
	
	private enum State{
		forward,
		backward,
		pause,
		stop
	}
	
	public Player(){
		this.mState = State.stop;
	}
	
	public void setAnimation(Animation animation){
		this.mAnimation = animation;
		this.mCurrentClipId = 0; //defaults to the first clip.
		if(this.mDofs == null || this.mDofs.getCount() < animation.getChannelCount()){
			this.mDofs = new DofStream(animation.getChannelCount());
		}
		if(this.mSkeleton != null){
			this.mSkeleton.setDofs(this.mDofs);
			
		}
	}
	
	public void setSkeleton(String skeletonName){
		Node node = Node.getNode(skeletonName);
		if(node instanceof Joint){
			this.setSkeleton((Joint)node);
		}
	}
	
	public void setSkeleton(Joint skeleton){
		//remove dofs from the previous skeleton
		if(this.mSkeleton != null){
			this.mSkeleton.setDofs(null);
		}
		this.mSkeleton = skeleton;
		if(this.mDofs != null){
			this.mSkeleton.setDofs(this.mDofs);
		}
	}
	
	public void play(){
		if(this.mState == State.stop){
			this.restart();
		}
		this.mState = State.forward;
	}
	
	public void playReverse(){
		if(this.mState == State.stop){
			this.restart();
		}
		this.mState = State.backward;
	}
	
	public void pause(){
		this.mState = State.pause;
	}
	
	public void restart(){
		this.mCurrentTime = 0;
	}

	@Override
	public void update() {
		this.mDofs.reset();
		switch(this.mState){
		case forward:
			this.mCurrentTime += this.mClock.getDeltaTime();
			this.mAnimation.update(this.mCurrentClipId,
					   this.mCurrentTime, 
					   this.mDofs);
			break;
		case backward:
			this.mCurrentTime -= this.mClock.getDeltaTime();
			this.mAnimation.update(this.mCurrentClipId,
					   this.mCurrentTime, 
					   this.mDofs);
			break;
		}
		this.mDofs.reset();
	}
	
	@Override
	public void setClock(Clock clock){
		this.mClock = clock;
	}
}
