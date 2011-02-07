package com.tespirit.bamporter.standardEditors;

import java.util.HashSet;
import java.util.Set;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.ButtonProperty;
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
			this.addProperty(new ButtonProperty("New Clip"){
				@Override
				public void onClick() {
					String name = Util.promptString("Please enter a clip name");
					if(name != null && name.length() > 0 && !mClipLookup.contains(name)){
						addNewClip(new Clip(name, 0, 0));
					}
				}
			});
			
			this.mClipLookup = new HashSet<String>();
			
			for(int i = 0; i < this.mAnimation.getClipCount(); i++){
				Clip clip = this.mAnimation.getClip(i);
				this.addNewEditor(clip);
				this.mClipLookup.add(clip.getName());
			}
		}

		public void addNewClip(Clip clip){
			this.mAnimation.addClip(clip);
			this.mClipLookup.add(clip.getName());
			((PlayerEditor.Editor)this.getParent()).addClip(clip);
			this.addEditor(clip);
		}
		
		public void insertClip(Clip clip, int index){
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
				this.mAnimation.removeClip(clip);
				((PlayerEditor.Editor)this.getParent()).removeClip(clip);
			}
		}
		
		public int getClipCount(){
			return this.mAnimation.getClipCount();
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
			super.recycle();
		}
	}
}
