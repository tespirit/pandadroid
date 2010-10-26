package com.tespirit.bamporter.standardEditors;

import java.util.Iterator;

import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.properties.BooleanProperty;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.ComboProperty;

public class PlayerEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new Editor((Player)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Player.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5040559450730841051L;
		private Player mPlayer;
		private ComboProperty<JointDisplay> mSkeletons;
		private ComboProperty<ClipDisplay> mClips;
		
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

		protected Editor(Player data) {
			super(data, false);
			this.mPlayer = data;
			this.addEditor(data.getAnimation());

			this.mSkeletons = this.addProperty(new ComboProperty<JointDisplay>("Skeleton"){
				@Override
				public void setValue(JointDisplay value) {
					mPlayer.setSkeleton(value.mJoint);
				}
				
				@Override
				public JointDisplay getValue(){
					if(mPlayer.getSkeleton() != null){
						JointDisplay jd = new JointDisplay(mPlayer.getSkeleton());
						return jd;
					} else {
						return null;
					}
				}
			});

			this.mClips = this.addProperty(new ComboProperty<ClipDisplay>("Clips"){
				@Override
				public void setValue(ClipDisplay value) {
					mPlayer.setActiveClip(value.mClip.getName());
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
			
			
			for(int i = 0; i < this.mPlayer.getAnimation().getClipCount(); i++){
				Clip clip = this.mPlayer.getAnimation().getClip(i);
				this.mClips.addItem(new ClipDisplay(clip));
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
				if(joint.getChannelCount() == this.mPlayer.getAnimation().getChannelCount()){
					this.mSkeletons.addItem(new JointDisplay((Joint)node));	
				}
			}
			for(int i = 0; i < node.getChildCount(); i++){
				this.updateSkeletons(node.getChild(i));
			}
		}
		
		@Override
		protected void initPanel(){
			this.updateSkeletons();
			if(this.mPlayer.getSkeleton() == null){
				this.mSkeletons.selectIndex(1);	
			}
		}
	}
}
