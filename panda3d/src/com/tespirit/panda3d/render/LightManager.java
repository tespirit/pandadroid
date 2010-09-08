package com.tespirit.panda3d.render;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

public class LightManager {
	
	private ArrayList<Light> lights;
	
	public LightManager(){
		this.lights = new ArrayList<Light>();
	}
	
	public void addLight(Light light){
		this.lights.add(light);
	}
	
	public void initLights(GL10 gl){
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnable(GL10.GL_NORMALIZE);
		for(int i = 0; i < this.lights.size(); i++){
			this.lights.get(i).init(gl, i);
		}
	}
	
	public void applyLights(GL10 gl){
		Iterator<Light> itr = this.lights.iterator();
		while(itr.hasNext()){
			itr.next().render(gl);
		}
	}
	
	public void createSimple(){
		this.addLight(new Light());
	}
}
