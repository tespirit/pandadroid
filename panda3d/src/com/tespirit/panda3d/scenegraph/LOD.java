package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;

public class LOD extends Node{
	
	private Node[] lods;
	private float[] maxDistances;
	private int currentLevel;
	
	
	public LOD(){
		super();
		this.currentLevel = 0;
		
	}
	
	public LOD(String name){
		super(name);
		this.currentLevel = 0;
	}
	
	@Override
	public Matrix3d getWorldTransform(){
		//TODO: see about maybe handling this...
		return null;
	}
	
	@Override
	public void update(Matrix3d transform){
		for(int i = 0; i < this.getChildCount(); i++){
			this.getChild(i).update(transform);
		}
	}
	
	public void setLODCount(int count){
		this.lods = new Node[count];
		this.maxDistances = new float[count];
	}
	
	public void addLOD(int level, Node node, float distance){
		this.lods[level] = node;
		this.maxDistances[level] = distance;
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.getChild().getBoundingBox();
	}
	
	public Node getChild(){
		return this.lods[this.currentLevel];
	}

	@Override
	public Node getChild(int i) {
		return this.lods[this.currentLevel];
	}

	@Override
	public int getChildCount() {
		if(this.lods.length>0)
			return 1;
		else
			return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return null;
	}
}
