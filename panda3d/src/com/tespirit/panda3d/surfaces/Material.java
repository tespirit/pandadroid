package com.tespirit.panda3d.surfaces;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.tespirit.panda3d.render.ComponentRenderer;

/**
 * This is a simple material that is just solid colors.
 * This is the base material added to any mesh created that
 * has no assigned material.
 * @author Todd Espiritu Santo
 *
 */
public class Material extends Surface{
	private FloatBuffer ambient;
	private FloatBuffer diffuse;
	private FloatBuffer specular;
	private FloatBuffer emission;
	
	private static final int COLOR_SIZE = 4;
	
	public Material(){
		this.ambient = createBuffer();
		this.diffuse = createBuffer();
		this.specular = createBuffer();
		this.emission = createBuffer();
		
		this.setAmbient(0.5f, 0.5f, 0.5f);
		this.setDiffuse(0.5f, 0.5f, 0.5f);
		this.setSpecular(1.0f, 1.0f, 1.0f);
		this.setEmission(0.0f, 0.0f, 0.0f);
	}
	
	private FloatBuffer createBuffer(){
		ByteBuffer temp = ByteBuffer.allocateDirect(Material.COLOR_SIZE * 4);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		this.diffuse.put(r);
		this.diffuse.put(g);
		this.diffuse.put(b);
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
		return this.specular;
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient.put(r);
		this.ambient.put(g);
		this.ambient.put(b);
		this.ambient.put(1.0f);
		this.ambient.position(0);
	}
	
	public FloatBuffer getAmbientBuffer(){
		return this.ambient;
	}
	
	public void setEmission(float r, float g, float b){
		this.emission.put(r);
		this.emission.put(g);
		this.emission.put(b);
		this.emission.put(1.0f);
		this.emission.position(0);
	}
	
	public FloatBuffer getEmissionBuffer(){
		return this.emission;
	}

	@Override
	public void render() {
		Material.renderer.render(this);
	}

	@Override
	public void setup() {
		Material.renderer.setup(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Material.renderer = this;
		}
		public abstract void render(Material material);
		public abstract void setup(Material material);
	}
}
