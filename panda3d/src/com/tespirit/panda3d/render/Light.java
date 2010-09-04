package com.tespirit.panda3d.render;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class Light {
	private float[] ambient;
	private float[] diffuse;
	private float[] specular;
	private float[] position;
	
	private int lightId;
	
	private static final int[] lightIds = {
		GL10.GL_LIGHT0,
		GL10.GL_LIGHT1,
		GL10.GL_LIGHT2,
		GL10.GL_LIGHT3,
		GL10.GL_LIGHT4,
		GL10.GL_LIGHT5,
		GL10.GL_LIGHT6,
		GL10.GL_LIGHT7
	};
	
	public Light(){
		this.ambient = new float[4];
		this.setAmbient(0.2f, 0.2f, 0.2f);
		
		this.diffuse = new float[4];
		this.setDiffuse(1.0f, 1.0f, 1.0f);
		
		this.specular = new float[4];
		this.setSpecular(1.0f, 1.0f, 1.0f);
		
		this.position = new float[4];
		this.setPosition(2.0f, 10.0f, -15.0f);
	}
	
	public void init(GL10 gl, int lightId){
		this.lightId = Light.lightIds[lightId];
		gl.glEnable(this.lightId);
	}
	
	public void render(GL10 gl){
        gl.glLightfv(this.lightId, GL10.GL_AMBIENT, this.ambient, 0);
        gl.glLightfv(this.lightId, GL10.GL_DIFFUSE, this.diffuse, 0);
        gl.glLightfv(this.lightId, GL10.GL_SPECULAR, this.specular, 0);
		gl.glLightfv(this.lightId, GL10.GL_POSITION, this.position, 0);
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		this.diffuse[0] = r;
		this.diffuse[1] = b;
		this.diffuse[2] = g;
		this.diffuse[3] = a;
	}
	
	public void setDiffuse(float r, float g, float b){
		this.diffuse[0] = r;
		this.diffuse[1] = b;
		this.diffuse[2] = g;
		this.diffuse[3] = 1.0f;
	}
	
	public float[] getDiffuse(){
		return this.diffuse;
	}
	
	public void setSpecular(float r, float g, float b){
		this.specular[0] = r;
		this.specular[1] = b;
		this.specular[2] = g;
		this.specular[3] = 1.0f;
	}
	
	public float[] getSpecular(){
		return this.specular;
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient[0] = r;
		this.ambient[1] = b;
		this.ambient[2] = g;
		this.ambient[3] = 1.0f;
	}
	
	public float[] getAmbient(){
		return this.ambient;
	}
	
	public void setPosition(float x, float y, float z){
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
	}
	
	public float[] getPositon(){
		return this.position;
	}
}
