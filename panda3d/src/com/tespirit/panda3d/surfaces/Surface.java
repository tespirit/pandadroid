package com.tespirit.panda3d.material;


import javax.microedition.khronos.opengles.GL10;

public abstract class Material {
	
	private static Material defaultMat = new Simple();
	
	public abstract void apply(GL10 gl);

	public static Material getDefaultMaterial(){
		return defaultMat;
	}
}
