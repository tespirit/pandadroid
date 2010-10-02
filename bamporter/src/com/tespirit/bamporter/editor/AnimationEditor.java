package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.scenegraph.Node;

public class AnimationEditor extends TreeNodeEditor{
	
	private Animation mAnimation;
	private JTextField mSkeleton;
	private Box mPanel;
	
	public AnimationEditor(Animation animation){
		this.mAnimation = animation;
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
				Node node = Node.getNode(mSkeleton.getText());
				if(node != null && node instanceof Joint){
					mAnimation.attachSkeleton((Joint)node);
				}
			}
		});
		skeletonLabel.setLabelFor(this.mSkeleton);
		
		JButton play = new JButton();
		play.setText("Play");
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mAnimation.play(Calendar.getInstance().getTimeInMillis());
			}
		});
		
		JButton stop = new JButton();
		stop.setText("Stop");
		stop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				mAnimation.stop();
			}
		});
		
		this.mPanel.add(info);
		this.mPanel.add(type);
		this.mPanel.add(skeletonLabel);
		this.mPanel.add(this.mSkeleton);
		this.mPanel.add(play);
		this.mPanel.add(stop);
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
