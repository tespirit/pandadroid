package com.tespirit.panda3d.surfaces;

import java.util.ArrayList;

public class TextureManager {
	private static TextureManager tm;
	
	private ArrayList<Texture> textures;
	
	private TextureManager(){
		this.textures = new ArrayList<Texture>();
	}
	
	public void addTexture(Texture t){
		this.textures.add(t);
	}
	
	public int getTextureCount(){
		return this.textures.size();
	}
	
	public Texture getTexture(int i){
		return this.textures.get(i);
	}
	
	public static TextureManager getInstance(){
		if(TextureManager.tm == null){
			TextureManager.tm = new TextureManager();
		}
		return tm;
	}
	
	
}
