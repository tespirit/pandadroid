package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.animation.Animation;

public class AnimationEditor extends TreeNodeEditor{
	
	private Animation mAnimation;
	
	public AnimationEditor(Animation animation){
		this.mAnimation = animation;
		for(int i = 0; i < animation.getChannelCount(); i++){
			this.add(new ChannelEditor(animation.getChannel(i)));
		}
	}

	@Override
	public Component getEditorPanel() {
		String output = "Bamboo Animation Info\n\n";
		output += "Type: "+this.mAnimation.toString()+"\n";
		output += "Channel count: " + this.mAnimation.getChannelCount() + "\n";
		EditorPanels.getTextInfo().setText(output);
		return EditorPanels.getTextInfo();
	}

	@Override
	public Component getPropertyPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
