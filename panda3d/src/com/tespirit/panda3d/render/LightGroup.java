package com.tespirit.panda3d.render;

import java.util.ArrayList;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.AxisAlignedBox;
import com.tespirit.panda3d.vectors.Matrix3d;

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
