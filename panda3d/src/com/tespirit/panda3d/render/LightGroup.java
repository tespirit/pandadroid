package com.tespirit.panda3d.render;

import java.util.ArrayList;

public class LightGroup {
	
	private ArrayList<Light> lights;
	
	public LightGroup(){
		this.lights = new ArrayList<Light>();
	}
	
	public void addLight(Light light){
		this.lights.add(light);
	}
	
	public int getLightCount(){
		return this.lights.size();
	}
	
	public Light getLight(int i){
		return this.lights.get(i);
	}
	
	/**
	 * This is a quick light initialization to some preset settings.
	 */
	public void createBasic(){
		this.addLight(new Light());
	}
	
	/**
	 * This is a quick light initialization for day time looking lighting.
	 */
	public void createDayTime(){
		//TODO: implement later
	}
	
	/**
	 * This is a quick light initialization for night time looking lighting.
	 */
	public void createNightTime(){
		//TODO: implement later
	}
}
