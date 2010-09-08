package com.tespirit.panda3d.material;

import com.tespirit.panda3d.core.*;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Texture extends Material{
	
	private String textureName;
	
	private int textureId;
	
	public Texture(){
		TextureManager.getInstance().addTexture(this);
	}
	
	public void setTexture(String textureName){
		this.textureName = textureName;
	}
	
	public boolean init(GL10 gl){
		Bitmap bitmap = Assets.getManager().openBitmap(this.textureName);
		if(bitmap == null){
			return false;
		}
		
		int[] tempTextures = new int[1];
		gl.glGenTextures(1, tempTextures, 0);
		this.textureId = tempTextures[0];
		
		/* currently only mip mapping is supported. */
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textureId);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);

		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		} else {
			this.buildMipmap(gl, bitmap);
		}
		
		bitmap.recycle();
		return true;
	}
	
	/**
	 * Original code: Savas Ziplies and Mike Miller
	 * @param gl
	 * @param bitmap
	 */
	private void buildMipmap(GL10 gl, Bitmap bitmap) {
		//
		int level = 0;
		//
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		//
		while(height >= 1 || width >= 1) {
			//First of all, generate the texture from our bitmap and set it to the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
			
			//
			if(height == 1 || width == 1) {
				break;
			}

			//Increase the mipmap level
			level++;

			//
			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
			
			//Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}

	@Override
	public void apply(GL10 gl) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textureId);
	}
}
