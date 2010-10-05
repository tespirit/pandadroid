package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Clip;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.render.RenderManager;

public class AnimationEditor extends TreeNodeEditor{
	
	private RenderManager mRenderManager;
	private Animation mAnimation;
	private Player mPlayer;
	private JTextField mSkeleton;
	private Box mPanel;
	private JComboBox mClips;
	
	public AnimationEditor(Animation animation, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mAnimation = animation;
		this.mPlayer = new Player();
		this.mPlayer.setAnimation(this.mAnimation);
		this.mRenderManager.addUpdater(this.mPlayer);
		
		for(int i = 0; i < this.mAnimation.getClipCount(); i++){
			Clip clip = this.mAnimation.getClip(i);
			this.add(new ClipEditor(clip));
		}
		this.mPanel = Box.createVerticalBox();
		
		this.mClips = new JComboBox();
		this.mClips.setEditable(false);
		this.mClips.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.setActiveClip(mClips.getSelectedIndex());
			}
		});
		
		JLabel info = new JLabel();
		info.setText("Bamboo Animation Info");
		
		JLabel type = new JLabel();
		type.setText(this.mAnimation.toString());
		
		this.mSkeleton = new JTextField();
		this.mSkeleton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.setSkeleton(mSkeleton.getText());
			}
		});
		
		JButton play = new JButton();
		play.setText("Play");
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.play();
			}
		});
		
		JButton pause = new JButton();
		pause.setText("Pause");
		pause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.pause();
			}
		});
		
		JButton reverse = new JButton();
		reverse.setText("Play Reverse");
		reverse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.playReverse();
			}
		});
		
		JButton restart = new JButton();
		restart.setText("Restart");
		restart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.restart();
			}
		});
		
		JButton newClip = new JButton();
		newClip.setText("Create Clip");
		newClip.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = EditorPanels.promptString("Please type in a name for this clip.");
				if(name != null && name.length() > 0){
					Clip clip = new Clip(name, 0,0);
					mAnimation.addClip(clip);
					add(new ClipEditor(clip));
					refreshClips();
				}
			}
		});
		
		this.mPanel.add(info);
		this.mPanel.add(type);
		

		this.mPanel.add(new JLabel("Clips"));
		this.mPanel.add(this.mClips);
		this.mPanel.add(newClip);
		
		this.mPanel.add(new JLabel("Player"));
		this.mPanel.add(new JLabel("Skeleton"));
		this.mPanel.add(this.mSkeleton);
		this.mPanel.add(play);
		this.mPanel.add(pause);
		this.mPanel.add(reverse);
		this.mPanel.add(restart);
		
		
		this.refreshClips();
	}
	
	public void refreshClips(){
		this.mClips.removeAllItems();
		for(int i = 0; i < this.mAnimation.getClipCount(); i++){
			Clip clip = this.mAnimation.getClip(i);
			this.mClips.addItem(clip.getName());
		}
		EditorPanels.refreshNavigator(this);
	}
	
	public Animation getAnimation(){
		return this.mAnimation;
	}

	@Override
	public Component getEditorPanel() {
		return this.mPanel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mAnimation);
	}

	@Override
	public void recycle() {
		if(this.mRenderManager != null){
			this.mRenderManager.removeUpdater(this.mPlayer);
		}
		this.mRenderManager = null;
		this.mAnimation = null;
		this.mPlayer = null;
		this.mSkeleton = null;
		this.mPanel = null;
		for(Enumeration<TreeNodeEditor> children = this.children(); children.hasMoreElements();){
			children.nextElement().recycle();
		}
	}

}
