package com.tespirit.bamporter.standardEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;


import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.SimplePanel;

public class AnimationEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new Editor((Animation)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Animation.class;
	}
	
	public class Editor extends TreeNodeEditor{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -1574225716204462101L;
		private Animation mAnimation;
		private Player mPlayer;
		private JTextField mName;
		private JComboBox mClips;
		private JButton mNewClip;
		private JToggleButton mPlay;
		private JComboBox mSkeletons;
		private ClipEditor.Editor mCurrentClip;
		private Set<String> mClipLookup;
		
		protected Editor(Animation animation){
			super(animation);
			this.mAnimation = animation;
			this.mPlayer = new Player();
			this.mPlayer.setAnimation(this.mAnimation);
			this.getRenderManager().addUpdater(this.mPlayer);
			this.mClipLookup = new HashSet<String>();
			
			for(int i = 0; i < this.mAnimation.getClipCount(); i++){
				this.addNewEditor(this.mAnimation.getClip(i));
				this.mClipLookup.add(this.mAnimation.getClip(i).getName());
			}
		}
		
		@Override
		protected Component generatePanel(){
			SimplePanel panel = new SimplePanel();
			this.mName = panel.createTextField("Name");
			
			panel.createLabel("Channels", String.valueOf(this.mAnimation.getChannelCount()));
			
			this.mClips = panel.createComboBox("Clips");
			this.mNewClip = panel.createButton("New Clip");
			this.mSkeletons = panel.createComboBox("Skeleton");
			
			this.mPlay = panel.createToggleButton("Play");
			JButton restart = panel.createButton("Restart");
			
			for(int i = 0; i < this.mAnimation.getClipCount(); i++){
				this.mClips.addItem(this.mAnimation.getClip(i).getName());
			}
			
			this.mCurrentClip = this.getSelectedClipEditor();
			
			this.updateSkeletons();
			
			if(this.mAnimation.getName() != null){
				this.mName.setText(this.mAnimation.getName());
			}
			
			this.mName.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					mAnimation.setName(mName.getText());
					updateEditor();
				}
			});
			
			this.mClips.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(mClips.getSelectedIndex() < getChildCount() && mClips.getSelectedIndex() > 0){	
						mPlayer.setActiveClip(mClips.getSelectedIndex());
						mCurrentClip.setPlayState(false);
						mCurrentClip = getSelectedClipEditor();
					}
				}
			});
			
			this.mNewClip.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					String name = Util.promptString("Please enter a clip name");
					if(name != null && name.length() > 0){
						addNewClip(new Clip(name, 0, 0));
					}
				}
			});
			
			this.mSkeletons.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(mSkeletons.getSelectedIndex() == 0){
						mPlayer.removeSkeleton();
						mPlay.setEnabled(false);
					} else {
						mPlayer.setSkeleton(((JointDisplay)mSkeletons.getSelectedItem()).mJoint);
						mPlay.setEnabled(true);
					}
				}
			});
			
			this.mPlay.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if(mPlay.getModel().isSelected()){
						mPlayer.play();
					} else {
						mPlayer.pause();
					}
					mCurrentClip.setPlayState(mPlay.getModel().isSelected());
				}
			});
			
			restart.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					mPlayer.restart();
				}
			});
			
			if(this.mSkeletons.getItemCount() > 1){
				this.mSkeletons.setSelectedIndex(1);
			} else {
				this.mPlay.setEnabled(false);
			}
			
			return panel;
		}
		
		public void addNewClip(Clip clip){
			this.mAnimation.addClip(clip);
			this.mClipLookup.add(clip.getName());
			if(this.mClips != null){
				this.mClips.addItem(clip.getName());
			}
			this.addEditor(clip);
		}
		
		public void insertClip(Clip clip, int index){
			if(this.mClips != null){
				if(index > this.mClips.getItemCount()){
					this.mClips.addItem(clip.getName());
				} else {
					this.mClips.insertItemAt(clip.getName(), index);
				}
			}
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
				int id = this.mPlayer.getClipId(clip.getName());
				if(this.mClips != null){
					int clipIndex = this.mClips.getSelectedIndex();
					this.mClips.removeItem(clip.getName());
					if(clipIndex >= id){
						if(clipIndex == 0){
							this.mClips.setSelectedIndex(0);
						} else {
							this.mClips.setSelectedIndex(clipIndex-1);
						}
					}
				}
				this.mAnimation.removeClip(clip);
			}
		}
		
		public int getClipCount(){
			return this.mAnimation.getClipCount();
		}
		
		private class JointDisplay{
			Joint mJoint;
			public JointDisplay(Joint joint){
				this.mJoint = joint;
			}
			
			@Override
			public String toString(){
				return this.mJoint.getName();
			}
		}
		
		public void updateSkeletons(){
			if(this.mSkeletons != null){
				this.mSkeletons.removeAllItems();
				this.mSkeletons.addItem("");
				for(Iterator<Node> i = this.getRenderManager().getSceneIterator(); i.hasNext();){
					this.updateSkeletons(i.next());
				}
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
		
		public boolean playClip(String name){
			if(this.mPlayer.getSkeleton() != null){
				this.mClips.setSelectedItem(name);
				this.mPlayer.setActiveClip(name);
				this.mPlayer.play();
				this.mPlay.getModel().setSelected(true);
				return true;
			} else {
				Util.alertError("Please set a skeleton to use in the player.");
				return false;
			}
		}
		
		public void pauseClip(){
			if(this.mPlay != null){
				this.mPlayer.pause();
				this.mPlay.getModel().setSelected(false);
			}
		}
		
		public ClipEditor.Editor getSelectedClipEditor(){
			if(this.mClips != null){
				return (ClipEditor.Editor)this.getChildAt(this.mClips.getSelectedIndex());
			}
			else return null;
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
			this.mName = null;
			this.mSkeletons = null;
			this.mNewClip = null;
			super.recycle();
		}
	}
}
