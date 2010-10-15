package com.tespirit.bamporter.standardEditors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.BooleanProperty;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.ComboProperty;
import com.tespirit.bamporter.properties.StringProperty;

public class AnimationEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new Editor((Animation)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Animation.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -1574225716204462101L;
		private Animation mAnimation;
		private Player mPlayer;
		private ComboProperty<ClipDisplay> mClips;
		private ComboProperty<JointDisplay> mSkeletons;
		private Set<String> mClipLookup;
		
		protected Editor(Animation animation){
			super(animation);
			this.mAnimation = animation;
			this.mPlayer = new Player();
			this.mPlayer.setAnimation(this.mAnimation);
			this.getRenderManager().addUpdater(this.mPlayer);
			
			this.addProperty(new StringProperty("Name"){
				@Override
				public void setValue(String value) {
					mAnimation.setName(value);
					updateEditor();
				}
				@Override
				public String getValue() {
					return mAnimation.getName();
				}
			});
			this.addProperty(new StringProperty("Channels", true){
				@Override
				public void setValue(String value) {
					//VOID
				}
				@Override
				public String getValue() {
					return String.valueOf(mAnimation.getChannelCount());
				}
			});
			this.mClips = this.addProperty(new ComboProperty<ClipDisplay>("Clips"){
				@Override
				public void setValue(ClipDisplay value) {
					mPlayer.setActiveClip(value.mClip.getName());
				}
			});
			this.addProperty(new ButtonProperty("New Clip"){
				@Override
				public void onClick() {
					String name = Util.promptString("Please enter a clip name");
					if(name != null && name.length() > 0 && !mClipLookup.contains(name)){
						addNewClip(new Clip(name, 0, 0));
					}
				}
			});
			this.mSkeletons = this.addProperty(new ComboProperty<JointDisplay>("Skeleton"){
				@Override
				public void setValue(JointDisplay value) {
					mPlayer.setSkeleton(value.mJoint);
				}
			});
			this.addProperty(new BooleanProperty("Play", true){
				@Override
				public void setValue(Boolean value) {
					if(value){
						mPlayer.play();
					} else {
						mPlayer.pause();
					}
				}

				@Override
				public Boolean getValue() {
					mPlayer.pause();
					return false;
				}
			});
			this.addProperty(new ButtonProperty("Restart"){
				@Override
				public void onClick() {
					mPlayer.restart();
				}
			});
			
			this.mClipLookup = new HashSet<String>();
			
			for(int i = 0; i < this.mAnimation.getClipCount(); i++){
				Clip clip = this.mAnimation.getClip(i);
				this.addNewEditor(clip);
				this.mClipLookup.add(clip.getName());
				this.mClips.addItem(new ClipDisplay(clip));
			}
		}
		
		@Override
		protected void initPanel(){
			this.updateSkeletons();
			this.mSkeletons.selectIndex(1);
		}

		public void addNewClip(Clip clip){
			this.mAnimation.addClip(clip);
			this.mClipLookup.add(clip.getName());
			this.mClips.addItem(new ClipDisplay(clip));
			this.addEditor(clip);
		}
		
		public void insertClip(Clip clip, int index){
			this.mClips.insertItem(new ClipDisplay(clip), index);
			this.mClipLookup.add(clip.getName());
			this.insertEditor(clip, index);
		}
		
		public void refreshClips() {
			for(int i = 0; i < this.mAnimation.getClipCount(); i++){
				Clip clip = this.mAnimation.getClip(i);
				if(!this.mClipLookup.contains(clip.getName())){
					this.insertClip(clip, i);
				}
			}
		}
		
		@Override
		public void removeEditor(TreeNodeEditor node){
			super.removeEditor(node);
			if(node instanceof ClipEditor.Editor){
				Clip clip = ((ClipEditor.Editor)node).getClip();
				this.mClipLookup.remove(clip.getName());
				this.mClips.removeItem(new ClipDisplay(clip));
				this.mAnimation.removeClip(clip);
			}
		}
		
		public int getClipCount(){
			return this.mAnimation.getClipCount();
		}
		
		private class ClipDisplay{
			Clip mClip;
			public ClipDisplay(Clip clip){
				this.mClip = clip;
			}
			
			@Override
			public String toString(){
				return this.mClip.getName();
			}
			
			@Override
			public boolean equals(Object o){
				if(o instanceof ClipDisplay){
					return ((ClipDisplay)o).mClip.getName().equals(this.mClip.getName());
				}
				return false;
			}
		}
		
		private class JointDisplay{
			Joint mJoint;
			public JointDisplay(Joint joint){
				this.mJoint = joint;
			}
			
			@Override
			public String toString(){
				if(this.mJoint != null){
					return this.mJoint.getName();
				} else {
					return "";
				}
			}
		}
		
		public void updateSkeletons(){
			this.mSkeletons.removeAllItems();
			this.mSkeletons.addItem(new JointDisplay(null));
			for(Iterator<Node> i = this.getRenderManager().getSceneIterator(); i.hasNext();){
				this.updateSkeletons(i.next());
			}
		}
		
		private void updateSkeletons(Node node){
			if(node instanceof Joint){
				Joint joint = (Joint)node;
				if(joint.getChannelCount() == this.mAnimation.getChannelCount()){
					this.mSkeletons.addItem(new JointDisplay((Joint)node));	
				}
			}
			for(int i = 0; i < node.getChildCount(); i++){
				this.updateSkeletons(node.getChild(i));
			}
		}
		
		@Override
		public String getNodeName(){
			return this.mAnimation.getName();
		}
	
		@Override
		public void recycle() {
			this.getRenderManager().removeUpdater(this.mPlayer);
			this.mAnimation = null;
			this.mPlayer = null;
			this.mClips = null;
			this.mSkeletons = null;
			super.recycle();
		}
	}
}
