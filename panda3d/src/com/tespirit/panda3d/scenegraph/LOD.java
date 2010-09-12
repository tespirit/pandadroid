package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;

public class LOD extends Node{
	
	private Node[] lods;
	private float[] maxDistances;
//	private Vector3d center;
	private int currentLevel;
	
	
	public LOD(){
//		this.center = new Vector3d();
		this.currentLevel = 0;
		
	}
	
	public LOD(String name){
		super(name);
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
