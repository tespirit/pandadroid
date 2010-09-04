package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;
import com.tespirit.panda3d.geometry.*;

public class Model extends Node{
	private Matrix3d transform;
	private AxisAlignedBox boundingBox;
	private Geometry geometry;
	
	public Model(){
		
	}
	
	public Model(String name){
		super(name);
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * Models cannot have children.
	 */
	@Override
	public Node getChild(int i) {
		return null;
	}

	/**
	 * Models cannot have children.
	 */
	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return transform;
	}
	
	public Geometry getGeometry(){
		return this.geometry;
	}
	
	public void setGeometry(Geometry g){
		this.geometry = g;
	}
}
