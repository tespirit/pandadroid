package com.tespirit.panda3d.material;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class TextureManager {
	private static TextureManager tm;
	
	private ArrayList<Texture> textures;
	
	private TextureManager(){
		this.textures = new ArrayList<Texture>();
	}
	
	public void addTexture(Texture t){
		this.textures.add(t);
	}
	
	public void initTextures(GL10 gl){
		Iterator<Texture> itr = this.textures.iterator();
		while(itr.hasNext()){
			itr.next().init(gl);
		}
	}
	
	public static TextureManager getInstance(){
		if(TextureManager.tm == null){
			TextureManager.tm = new TextureManager();
		}
		return tm;
	}
	
	
}
