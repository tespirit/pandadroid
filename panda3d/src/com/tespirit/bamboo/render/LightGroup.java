package com.tespirit.bamboo.render;

import java.util.ArrayList;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;

public class LightGroup extends Node{
	
	private ArrayList<Light> lights;
	
	public LightGroup(){
		this.lights = new ArrayList<Light>();
	}
	
	public void addLight(Light light){
		this.lights.add(light);
	}
	
	@Override
	public void update(Matrix3d transform){
		for(int i = 0; i < this.lights.size(); i++){
			this.lights.get(i).update(transform);
		}
	}
	
	@Override
	public Matrix3d getWorldTransform(){
		//TODO: see about maybe handling this...
		return null;
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	public Node getChild(int i) {
		return this.lights.get(i);
	}

	@Override
	public int getChildCount() {
		return this.lights.size();
	}

	@Override
	public Matrix3d getTransform() {
		return null;
	}
	
	/**
	 * This is a quick light initialization to some preset settings.
	 */
	public void createBasic(){
		Light light1 = new Light();
		light1.setPosition(50.0f, 50.0f, 50.0f);
		this.addLight(light1);
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
