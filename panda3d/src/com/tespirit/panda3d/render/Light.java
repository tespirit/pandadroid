package com.tespirit.panda3d.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Light {
	
	private static final int COLOR_SIZE = 4;
	
	private FloatBuffer ambient;
	private FloatBuffer diffuse;
	private FloatBuffer specular;
	private FloatBuffer position;
	
	private int lightId;
	
	public Light(){
		this.ambient = createBuffer();
		this.diffuse = createBuffer();
		this.specular = createBuffer();
		this.position = createBuffer();
		
		this.setAmbient(0.5f, 0.5f, 0.5f);
		this.setDiffuse(1.0f, 1.0f, 1.0f);
		this.setSpecular(1.0f, 1.0f, 1.0f);
		this.setPosition(3.0f, 1.0f, 2.0f);
	}
	
	private FloatBuffer createBuffer(){
		ByteBuffer temp = ByteBuffer.allocateDirect(Light.COLOR_SIZE * 4);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	
	public void init(GL10 gl, int lightId){
		if(lightId < GL10.GL_MAX_LIGHTS) {
			this.lightId = GL10.GL_LIGHT0+lightId;
			gl.glEnable(this.lightId);
		}
	}
	
	public void render(GL10 gl){
        gl.glLightfv(this.lightId, GL10.GL_AMBIENT, this.ambient);
        gl.glLightfv(this.lightId, GL10.GL_DIFFUSE, this.diffuse);
        gl.glLightfv(this.lightId, GL10.GL_SPECULAR, this.specular);
		gl.glLightfv(this.lightId, GL10.GL_POSITION, this.position);
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		this.diffuse.put(r);
		this.diffuse.put(b);
		this.diffuse.put(g);
		this.diffuse.put(a);
		this.diffuse.position(0);
	}
	
	public void setDiffuse(float r, float g, float b){
		this.diffuse.put(r);
		this.diffuse.put(g);
		this.diffuse.put(b);
		this.diffuse.put(1.0f);
		this.diffuse.position(0);
	}
	
	public void setSpecular(float r, float g, float b){
		this.specular.put(r);
		this.specular.put(g);
		this.specular.put(b);
		this.specular.put(1.0f);
		this.specular.position(0);
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient.put(r);
		this.ambient.put(g);
		this.ambient.put(b);
		this.ambient.put(1.0f);
		this.ambient.position(0);
	}
	
	public void setPosition(float x, float y, float z){
		this.position.put(x);
		this.position.put(y);
		this.position.put(z);
		this.position.put(1.0f);
		this.position.position(0);
	}
}
