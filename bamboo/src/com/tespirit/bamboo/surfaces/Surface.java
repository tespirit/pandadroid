package com.tespirit.bamboo.surfaces;

import com.tespirit.bamboo.render.Resource;

public abstract class Surface implements Resource{
	
	private static Surface defaultSurface = new Color();

	public static Surface getDefaultSurface(){
		return Surface.defaultSurface;
	}
	
	public abstract void render();
	
}
