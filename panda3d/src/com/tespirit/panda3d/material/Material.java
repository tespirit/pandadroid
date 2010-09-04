package com.tespirit.panda3d.material;


import javax.microedition.khronos.opengles.GL10;

public class Material {
	private float[] ambient;
	private float[] diffuse;
	private float[] specular;
	private float[] emission;
	
	private static Material defaultMat = new Material();
	
	public Material(){
		this.ambient = new float[4];
		this.setAmbient(0.5f, 0.5f, 0.5f);
		
		this.diffuse = new float[4];
		this.setDiffuse(0.5f, 0.5f, 0.5f);
		
		this.specular = new float[4];
		this.setSpecular(1.0f, 1.0f, 1.0f);
		
		this.emission = new float[4];
		this.setEmission(0.0f, 0.0f, 0.0f);
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
	
	public void setEmission(float r, float g, float b){
		this.emission[0] = r;
		this.emission[1] = b;
		this.emission[2] = g;
		this.emission[3] = 1.0f;
	}
	
	public float[] getEmission(){
		return this.emission;
	}
	
	public void apply(GL10 gl){
		//gl.glMaterialfv(face, pname, params, offset)
		gl.glColor4f(this.diffuse[0], this.diffuse[1], this.diffuse[2], this.diffuse[3]);
	}

	public static Material getDefaultMaterial(){
		return defaultMat;
	}
}
