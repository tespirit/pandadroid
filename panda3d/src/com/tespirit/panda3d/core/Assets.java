package com.tespirit.panda3d.core;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This is a static class for grabbing resources used by the panda 3d engine.
 * it provides convenient functions for particular types of resources to load,
 * such as textures, models, animations, etc.
 * As this engine evolves, more special formats should be accounted for (like sound).
 * @author Todd Espiritu Santo
 *
 */
public class Assets {
	private android.content.res.AssetManager assets;
	
	private static Assets manager;
	
	/**
	 * this must be called first.
	 * @param contex
	 */
	public static void init(Context contex){
		Assets.manager = new Assets(contex);
	}
	
	public static Assets getManager(){
		return Assets.manager;
	}
	
	private Assets(Context contex){
		this.assets = contex.getAssets();
	}
	
	public Bitmap openBitmap(String textureName){
		InputStream stream;
		Bitmap bitmap;
		
		try{
			stream = this.assets.open(textureName);
		} catch (IOException e){
			return null;
		}
		
		try{
			bitmap = BitmapFactory.decodeStream(stream);
		} catch (Exception e){
			return null;
		} finally {
			try {
				stream.close();
				stream = null;
				
			} catch (IOException e) { }
		}
		
		return bitmap;
	}
}
