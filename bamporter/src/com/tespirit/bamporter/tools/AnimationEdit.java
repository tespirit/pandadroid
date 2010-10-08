package com.tespirit.bamporter.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.io.BambooAsset;

public class AnimationEdit {
	private static final long DEFAULT_FRAME_LENGTH = 1000/30; //30fps;
	
	
	private static class OCCompare implements Comparator<OverlappingClips>{
		@Override
		public int compare(OverlappingClips o1, OverlappingClips o2) {
			return (int)(o1.mStart - o2.mStart);
		}
	}
	
	private static class OverlappingClips{
		ArrayList<Clip> mClips;
		
		long mStart;
		long mEnd;
		
		OverlappingClips(){
			mClips = new ArrayList<Clip>();
			mStart = Long.MAX_VALUE;
			mEnd = Long.MIN_VALUE;
		}
		
		boolean add(Clip clip){
			if(mClips.size() == 0 || (clip.getStart() <= mEnd && clip.getEnd() >= mStart)){
				mClips.add(clip);
				if(mStart > clip.getStart()){
					mStart = clip.getStart();
				}
				if(mEnd < clip.getEnd()){
					mEnd = clip.getEnd();
				}
				return true;
			}
			return false;
		}
	}
	/**
	 * 
	 * @param anim
	 */
	public static void removeUnusedKeys(Animation anim){
		ArrayList<OverlappingClips> clips = new ArrayList<OverlappingClips>();
		for(int i = 0; i < anim.getClipCount(); i++){
			insertClip(clips, anim.getClip(i));
		}
		Collections.sort(clips, new OCCompare());
		long start = 0;
		for(OverlappingClips oc : clips){
			if(oc.mStart > start){
				for(int i = 0; i < anim.getChannelCount(); i++){
					anim.getChannel(i).removeKeyFrames(start, oc.mStart);
				}
				start = oc.mEnd;
			}
		}
	}
	
	private static void insertClip(ArrayList<OverlappingClips> clips, Clip clip){
		for(OverlappingClips oc : clips){
			if(oc.add(clip)){
				return;
			}
		}
		clips.add(new OverlappingClips());
		clips.get(0).add(clip);
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
