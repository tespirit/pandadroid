package com.tespirit.panda3d.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


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
	
	public void setLightId(int id){
		this.lightId = id;
	}
	
	public int getLightId(){
		return this.lightId;
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
	
	public FloatBuffer getDiffuseBuffer(){
		return this.diffuse;
	}
	
	public void setSpecular(float r, float g, float b){
		this.specular.put(r);
		this.specular.put(g);
		this.specular.put(b);
		this.specular.put(1.0f);
		this.specular.position(0);
	}
	
	public FloatBuffer getSpecularBuffer(){
		return this.diffuse;
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient.put(r);
		this.ambient.put(g);
		this.ambient.put(b);
		this.ambient.put(1.0f);
		this.ambient.position(0);
	}
	
	public FloatBuffer getAmbientBuffer(){
		return this.diffuse;
	}
	
	public void setPosition(float x, float y, float z){
		this.position.put(x);
		this.position.put(y);
		this.position.put(z);
		this.position.put(1.0f);
		this.position.position(0);
	}
	
	public FloatBuffer getPositionBuffer(){
		return this.diffuse;
	}
	
	public void render(){
		Light.renderer.render(this);
	}
	
	public void setup(){
		Light.renderer.setup(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Light.renderer = this;
		}
		public abstract void render(Light light);
		public abstract void setup(Light light);
	}
}
