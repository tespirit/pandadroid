package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JComboBox;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.app.BamporterEditor;

public class AnimationEditor extends TreeNodeEditor{
	
	private RenderManager mRenderManager;
	private Animation mAnimation;
	private Player mPlayer;
	private AnimationEditorPanel mPropertyPanel;
	
	public AnimationEditor(Animation animation, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mAnimation = animation;
		this.mPlayer = new Player();
		this.mPlayer.setAnimation(this.mAnimation);
		this.mRenderManager.addUpdater(this.mPlayer);

		this.mPropertyPanel = new AnimationEditorPanel();
		for(int i = 0; i < this.mAnimation.getClipCount(); i++){
			Clip clip = this.mAnimation.getClip(i);
			this.add(new ClipEditor(clip));
			this.mPropertyPanel.mClips.addItem(clip.getName());
		}
		
		if(this.mAnimation.getName() != null){
			this.mPropertyPanel.mName.setText(this.mAnimation.getName());
		}
		this.updateSkeletons();
		
	}
	
	public void addClip(Clip clip){
		this.mAnimation.addClip(clip);
		this.add(new ClipEditor(clip));
		JComboBox clips = this.mPropertyPanel.mClips;
		clips.addItem(clip.getName());
		BamporterEditor.getInstance().reloadNavigator();
	}
	
	public void removeClip(Clip clip){
		this.mAnimation.removeClip(clip);
		JComboBox clips = this.mPropertyPanel.mClips;
		clips.removeItem(clip.getName());
	}
	
	public int getClipCount(){
		return this.mAnimation.getClipCount();
	}
	
	public void updateSkeleton(){
		
	}
	
	public void updateSkeletons(){
		JComboBox skeletons = this.mPropertyPanel.mSkeletons;
		
		skeletons.removeAllItems();
		skeletons.addItem("");
		for(Iterator<Node> i = this.mRenderManager.getSceneIterator(); i.hasNext();){
			this.updateSkeletons(i.next());
		}
	}
	
	private void updateSkeletons(Node node){
		if(node instanceof Joint){
			this.mPropertyPanel.mSkeletons.addItem(node.getName());
		}
		for(int i = 0; i < node.getChildCount(); i++){
			this.updateSkeletons(node.getChild(i));
		}
	}
	
	public void updatePlay(){
		
	}
	
	public void play(){
		
	}
	
	public void pause(){
		
	}
	
	public void reverse(){
		
	}
	
	public void restart(){
		
	}

	@Override
	public Component getPropertyPanel() {
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
		for(Enumeration<TreeNodeEditor> children = this.children(); children.hasMoreElements();){
			children.nextElement().recycle();
		}
	}

}
