package com.tespirit.bamboo.surfaces;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.Resource;

public abstract class Surface implements Resource{
	
	public static final byte BLEND_NONE = 0;
	public static final byte BLEND_ADD = 1;
	public static final byte BLEND_ALPHA = 2;
	public static final byte BLEND_TYPE_COUNT = 3;
	
	
	private static Surface defaultSurface = new Color();

	public static Surface getDefaultSurface(){
		return Surface.defaultSurface;
	}
	
	private byte mBlending;
	
	public byte getBlendMethod(){
		return this.mBlending;
	}
	
	public boolean enableBlending(){
		return this.mBlending > Surface.BLEND_NONE;
	}
	
	public boolean hasAlpha(){
		return this.mBlending >= Surface.BLEND_ALPHA;
	}
	
	public boolean autoDetectAlpha(){
		return this.mBlending < Surface.BLEND_ADD;
	}
	
	public void setBlending(byte blend){
		this.mBlending = blend;
	}
	
	public abstract void renderStart();
	
	public abstract void renderEnd();
	
	protected void write(ObjectOutput out) throws IOException{
		out.writeByte(this.mBlending);
	}
	
	protected void read(ObjectInput in) throws IOException{
		this.mBlending = in.readByte();
	}
	
}
