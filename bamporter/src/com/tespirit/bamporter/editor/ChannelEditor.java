package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.animation.Channel;
import com.tespirit.bamboo.render.RenderManager;

public class ChannelEditor extends TreeNodeEditor{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9219286824739948996L;
	private Channel mChannel;
	
	public ChannelEditor(Channel channel, RenderManager renderManager){
		super(false);
		this.mChannel = channel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mChannel);
	}


	@Override
	public void recycle() {
		this.mChannel = null;
	}


	@Override
	protected Component generatePanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
