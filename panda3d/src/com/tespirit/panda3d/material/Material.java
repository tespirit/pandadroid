package com.tespirit.panda3d.material;

import javax.microedition.khronos.opengles.GL10;

public class Material {
	private float red;
	private float green;
	private float blue;
	private float alpha;
	
	private static Material defaultMat = new Material();
	
	public Material(){
		this.red = 0.5f;
		this.green = 0.5f;
		this.blue = 0.5f;
		this.alpha = 1.0f;
	}
	
	public float getRed(){
		return this.red;
	}
	
	public float getGreen(){
		return this.green;
	}
	
	public float getBlue(){
		return this.blue;
	}
	
	public void setRed(float r){
		this.red = r;
	}
	
	public void setGreen(float g){
		this.green = g;
	}
	
	public void setBlue(float b){
		this.blue = b;
	}
	
	public void setAlpha(float a){
		this.alpha = a;
	}
	
	public void setColor(float r, float g, float b, float a){
		this.red = r;
		this.blue = b;
		this.green = g;
		this.alpha = a;
	}
	
	public void setColor(float r, float g, float b){
		this.red = r;
		this.blue = b;
		this.green = g;
		this.alpha = 1.0f;
	}
	
	public void apply(GL10 gl){
		gl.glColor4f(this.red, this.green, this.blue, this.alpha);
	}

	public static Material getDefaultMaterial(){
		return defaultMat;
	}
}
