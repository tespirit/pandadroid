package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.app.BamporterFrame;

public class AnimationEditor extends TreeNodeEditor{
	
	private RenderManager mRenderManager;
	private Animation mAnimation;
	private Player mPlayer;
	private SimplePanel mPropertyPanel;
	private JTextField mName;
	private JComboBox mClips;
	private JButton mNewClip;
	private JToggleButton mPlay;
	private JComboBox mSkeletons;
	private ClipEditor mCurrentClip;
	
	public AnimationEditor(Animation animation, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mAnimation = animation;
		this.mPlayer = new Player();
		this.mPlayer.setAnimation(this.mAnimation);
		this.mRenderManager.addUpdater(this.mPlayer);
		
		for(int i = 0; i < this.mAnimation.getClipCount(); i++){
			this.add(new ClipEditor(this.mAnimation.getClip(i)));
		}
	}
	
	private void generatePanel(){
		this.mPropertyPanel = new SimplePanel();
		this.mName = this.mPropertyPanel.createTextField("Name");
		this.mClips = this.mPropertyPanel.createComboBox("Clips");
		this.mNewClip = this.mPropertyPanel.createButton("New Clip");
		this.mSkeletons = this.mPropertyPanel.createComboBox("Skeleton");
		
		this.mPlay = this.mPropertyPanel.createToggleButton("Play");
		JButton restart = this.mPropertyPanel.createButton("Restart");
		
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
				BamporterFrame.getInstance().reloadNavigator();
			}
		});
		
		this.mClips.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mPlayer.setActiveClip(mClips.getSelectedIndex());
				mCurrentClip.setPlayState(false);
				mCurrentClip = getSelectedClipEditor();
			}
		});
		
		this.mNewClip.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String name = Util.promptString("Please enter a clip name");
				if(name != null && name.length() > 0){
					addClip(new Clip(name,0,0));
				}
			}
		});
		
		this.mSkeletons.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mSkeletons.getSelectedIndex() == 0){
					mPlayer.removeSkeleton();
				} else {
					mPlayer.setSkeleton((String)mSkeletons.getSelectedItem());
				}
			}
		});
		
		mPlay.addActionListener(new ActionListener(){
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
	}
	
	public void addClip(Clip clip){
		this.mAnimation.addClip(clip);
		this.add(new ClipEditor(clip));
		if(this.mClips != null){
			this.mClips.addItem(clip.getName());
		}
		BamporterFrame.getInstance().reloadNavigator();
	}
	
	public void removeClip(Clip clip){
		this.mAnimation.removeClip(clip);
		if(this.mClips != null){
			this.mClips.removeItem(clip.getName());
			this.mPlayer.setActiveClip(0);
		}
	}
	
	public int getClipCount(){
		return this.mAnimation.getClipCount();
	}
	
	public void updateSkeletons(){
		if(this.mSkeletons != null){
			this.mSkeletons.removeAllItems();
			this.mSkeletons.addItem("");
			for(Iterator<Node> i = this.mRenderManager.getSceneIterator(); i.hasNext();){
				this.updateSkeletons(i.next());
			}
		}
	}
	
	private void updateSkeletons(Node node){
		if(node instanceof Joint){
			this.mSkeletons.addItem(node.getName());
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
	
	public ClipEditor getSelectedClipEditor(){
		if(this.mClips != null){
			return (ClipEditor)this.getChildAt(this.mClips.getSelectedIndex());
		}
		else return null;
	}
	

	@Override
	public Component getPropertyPanel() {
		if(this.mPropertyPanel == null){
			this.generatePanel();
		}
		return this.mPropertyPanel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mAnimation);
	}

	@Override
	public void recycle() {
		this.mRenderManager.removeUpdater(this.mPlayer);
		this.mRenderManager = null;
		this.mAnimation = null;
		this.mPlayer = null;
		this.mPropertyPanel = null;
		this.mClips = null;
		this.mName = null;
		this.mSkeletons = null;
		this.mNewClip = null;
		for(Enumeration<TreeNodeEditor> children = this.children(); children.hasMoreElements();){
			children.nextElement().recycle();
		}
	}

}
