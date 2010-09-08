package com.tespirit.panda3d.material;


import javax.microedition.khronos.opengles.GL10;

/**
 * This is a simple material that is just solid colors.
 * This is the base material added to any mesh created that
 * has no assigned material.
 * @author Todd Espiritu Santo
 *
 */
public class Simple extends Material{
	private float[] values;
	private static final int ambient = 0;
	private static final int diffuse = 4;
	private static final int specular = 8;
	private static final int emission = 12;
	private static final int size = 16;
	
	public Simple(){
		this.values = new float[Simple.size];
		this.setAmbient(0.5f, 0.5f, 0.5f);
		this.setDiffuse(0.5f, 0.5f, 0.5f);
		this.setSpecular(1.0f, 1.0f, 1.0f);
		this.setEmission(0.0f, 0.0f, 0.0f);
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		this.values[Simple.diffuse] = r;
		this.values[Simple.diffuse+1] = b;
		this.values[Simple.diffuse+2] = g;
		this.values[Simple.diffuse+3] = a;
	}
	
	public void setDiffuse(float r, float g, float b){
		this.values[Simple.diffuse] = r;
		this.values[Simple.diffuse+1] = b;
		this.values[Simple.diffuse+2] = g;
		this.values[Simple.diffuse+3] = 1.0f;
	}
	
	public void setSpecular(float r, float g, float b){
		this.values[Simple.specular] = r;
		this.values[Simple.specular+1] = b;
		this.values[Simple.specular+2] = g;
		this.values[Simple.specular+3] = 1.0f;
	}
	
	public void setAmbient(float r, float g, float b){
		this.values[Simple.ambient] = r;
		this.values[Simple.ambient+1] = b;
		this.values[Simple.ambient+2] = g;
		this.values[Simple.ambient+3] = 1.0f;
	}
	
	public void setEmission(float r, float g, float b){
		this.values[Simple.emission] = r;
		this.values[Simple.emission+1] = b;
		this.values[Simple.emission+2] = g;
		this.values[Simple.emission+3] = 1.0f;
	}
	
	@Override
	public void apply(GL10 gl){
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, this.values, Simple.ambient);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, this.values, Simple.diffuse);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, this.values, Simple.specular);
		gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_EMISSION, this.values, Simple.emission);
	}
}
