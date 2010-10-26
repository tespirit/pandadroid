package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.TimeUpdater;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.scenegraph.Node;

/**
 * This class is responsible for handling playing of animations to skeletons.
 * @author Todd Espiritu Santo
 *
 */
public class Player implements TimeUpdater, Externalizable{
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
	
	/**
	 * This allows you to set an active clip by name. it will return the clip id,
	 * incase you want to activate this again later.
	 * @param name
	 * @return
	 */
	public int setActiveClip(String name){
		this.mCurrentClipId = this.mAnimation.getClipId(name);
		return this.mCurrentClipId;
		
	}
	
	public void setActiveClip(int id){
		this.mCurrentClipId = id;
	}
	
	public int getClipId(String name){
		return this.mAnimation.getClipId(name);
	}
	
	public void setAnimation(Animation animation){
		this.mAnimationName = animation.getName();
		this.mAnimation = animation;
		this.mCurrentClipId = 0; //defaults to the first clip.
		if(this.mDofs == null || this.mDofs.getCount() < animation.getChannelCount()){
			this.mDofs = new DofStream(animation.getChannelCount());
		}
	}
	
	public Animation getAnimation(){
		return this.mAnimation;
	}
	
	public void setSkeleton(String skeletonName){
		Node node = Node.getNode(skeletonName);
		if(node instanceof Joint){
			this.setSkeleton((Joint)node);
		}
	}
	
	public void setSkeleton(Joint skeleton){
		this.mSkeletonName = skeleton.getName();
		this.mSkeleton = skeleton;
	}
	
	public void removeSkeleton(){
		this.mSkeleton = null;
		this.mSkeletonName = null;
	}
	
	public Joint getSkeleton(){
		return this.mSkeleton;
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
		switch(this.mState){
		case forward:
			this.mCurrentTime += this.mClock.getDeltaTime();
			break;
		case backward:
			this.mCurrentTime -= this.mClock.getDeltaTime();
			break;
		default:
			return;
		}
		
		this.mAnimation.update(this.mCurrentClipId,
				   this.mCurrentTime, 
				   this.mDofs);
		this.mDofs.reset();
		this.mSkeleton.update(this.mDofs);
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

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		//VOID: no need to dispatch updaters... yet :)
	}

}
