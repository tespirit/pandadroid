package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.animation.Channel;

public class ChannelEditor extends TreeNodeEditor{
	private Channel mChannel;
	
	public ChannelEditor(Channel channel){
		super(false);
		this.mChannel = channel;
	}
	
	
	@Override
	public Component getEditorPanel() {
		String output = "Bamboo Channel Info\n\n";
		output += "Type: " + this.mChannel.toString() + "\n";
		output += "Key Frame Count: " + this.mChannel.getKeyFrameCount();
		EditorPanels.getTextInfo().setText(output);
		return EditorPanels.getTextInfo();
	}

	@Override
	public Component getPropertyPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mChannel);
	}

}
