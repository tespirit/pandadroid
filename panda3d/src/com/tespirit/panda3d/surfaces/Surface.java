package com.tespirit.panda3d.surfaces;

public abstract class Surface{
	
	private static Surface defaultSurface = new Material();

	public static Surface getDefaultSurface(){
		return Surface.defaultSurface;
	}
	
	public abstract void render();
	
	public abstract void setup();
}
