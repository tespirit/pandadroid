package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.render.Renderer;

public class AnimationEditor extends TreeNodeEditor{
	
	private Animation mAnimation;
	private Player mPlayer;
	private JTextField mSkeleton;
	private Box mPanel;
	
	public AnimationEditor(Animation animation, Renderer renderer){
		this.mAnimation = animation;
		this.mPlayer = new Player();
		this.mPlayer.setAnimation(this.mAnimation);
		renderer.addTimeUpdate(this.mPlayer);
		
		for(int i = 0; i < animation.getChannelCount(); i++){
			this.add(new ChannelEditor(animation.getChannel(i)));
		}
		this.mPanel = Box.createVerticalBox();
		
		JLabel info = new JLabel();
		info.setText("Bamboo Animation Info");
		
		JLabel type = new JLabel();
		type.setText(this.mAnimation.toString());
		
		JLabel skeletonLabel = new JLabel();
		skeletonLabel.setText("Skeleton Root");
		
		this.mSkeleton = new JTextField();
		this.mSkeleton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mPlayer.setSkeleton(mSkeleton.getText());
			}
		});
		skeletonLabel.setLabelFor(this.mSkeleton);
		
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
		
		this.mPanel.add(info);
		this.mPanel.add(type);
		this.mPanel.add(skeletonLabel);
		this.mPanel.add(this.mSkeleton);
		this.mPanel.add(play);
		this.mPanel.add(pause);
		this.mPanel.add(reverse);
		this.mPanel.add(restart);
	}

	@Override
	public Component getEditorPanel() {
		return this.mPanel;
	}

	@Override
	public Component getPropertyPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mAnimation);
	}
}
