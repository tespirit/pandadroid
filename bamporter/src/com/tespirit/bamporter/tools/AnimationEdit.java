package com.tespirit.bamporter.tools;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.io.BambooAsset;

public class AnimationEdit {
	private static final long DEFAULT_FRAME_LENGTH = 1000/30; //30fps;
	
	/**
	 * 
	 * @param anim
	 */
	public static void removeUnusedKeys(Animation anim){
		//TODO: implement later, as this is a bit more complicated...
		
	}
	
	/**
	 * This merges all animations from source to the first matching animation in destination. A
	 * matching animation is any animation with the same amount of channels.
	 * @param source
	 * @param destination
	 */
	public static void mergeAnimation(BambooAsset source, BambooAsset destination){
		for(Animation sourceAnimation : source.getAnimations()){
			for(Animation destAnimation : destination.getAnimations()){
				if(sourceAnimation.getChannelCount() == destAnimation.getChannelCount()){
					mergeAnimation(sourceAnimation, destAnimation);
					break;
				}
			}
		}
	}
	
	private static int clipUid;
	public static void mergeAnimation(Animation source, Animation dest){
		long frameOffset = dest.getTotalLength() + DEFAULT_FRAME_LENGTH;
		for(int i = 0; i < source.getChannelCount(); i++){
			mergeChannel(source.getChannel(i), dest.getChannel(i), frameOffset);
		}
		for(int i = 0; i < source.getClipCount(); i++){
			Clip sourceClip = source.getClip(i);
			String name = sourceClip.getName();
			while(dest.hasClip(name)){
				name = sourceClip.getName()+clipUid;
			}
			dest.addClip(new Clip(name, frameOffset+sourceClip.getStart(),frameOffset+sourceClip.getEnd()));
		}
	}
	
	private static void mergeChannel(Channel source, Channel dest, long frameOffset){
		dest.reserve(dest.getKeyFrameCount() + source.getKeyFrameCount());
		for(int i = 0; i < source.getKeyFrameCount(); i++){
			Channel.KeyFrame frame = source.getKeyFrame(i);
			dest.addKeyFrame(new Channel.KeyFrame(frame, frameOffset));
		}
	}
}
