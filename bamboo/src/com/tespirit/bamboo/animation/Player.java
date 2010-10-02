package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.TimeUpdate;
import com.tespirit.bamboo.scenegraph.Node;

/**
 * This class is responsible for handling playing of animations to skeletons.
 * @author Todd Espiritu Santo
 *
 */
public class Player implements TimeUpdate, Externalizable{
	private Animation mAnimation;
	private Joint mSkeleton;
	private long mCurrentTime;
	private Clock mClock;
	private int mCurrentClipId;
	private DofStream mDofs;
	private State mState;
	
	private String mSkeletonName = "";
	private String mAnimationName = "";
	
	
	private enum State{
		forward,
		backward,
		pause,
		stop
	}
	
	public Player(){
		this.mState = State.stop;
		this.mSkeletonName = "";
		this.mAnimationName = "";
	}
	
	public void setAnimation(String name){
		Animation animation = Animation.getAnimation(name);
		if(animation != null){
			this.setAnimation(animation);
		}
	}
	
	public void setAnimation(Animation animation){
		this.mAnimationName = animation.getName();
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
		this.mSkeletonName = skeleton.getName();
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
	
	//IO
	private static final long serialVersionUID = 1834788358743094655L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.setAnimation(in.readUTF());
		this.setSkeleton(in.readUTF());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.mAnimationName);
		out.writeUTF(this.mSkeletonName);
	}
}
