package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.render.RenderManager;

public class ChannelEditor extends TreeNodeEditor{
	private Channel mChannel;
	
	public ChannelEditor(Channel channel, RenderManager renderManager){
		super(false);
		this.mChannel = channel;
	}
	
	
	@Override
	public Component getPropertyPanel() {
		String output = "Bamboo Channel Info\n\n";
		output += "Type: " + this.mChannel.toString() + "\n";
		output += "Key Frame Count: " + this.mChannel.getKeyFrameCount();
		Util.getTextInfo().setText(output);
		return Util.getTextInfo();
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mChannel);
	}


	@Override
	public void recycle() {
		this.mChannel = null;
	}

}
