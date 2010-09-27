package com.tespirit.bamboo.surfaces;

import java.io.Serializable;

public abstract class Surface implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7851576617137394266L;
	private static Surface defaultSurface = new Color();

	public static Surface getDefaultSurface(){
		return Surface.defaultSurface;
	}
	
	public abstract void render();
	
	public abstract void setup();
}
