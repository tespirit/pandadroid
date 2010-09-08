package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;
import com.tespirit.panda3d.geometry.*;
import com.tespirit.panda3d.material.*;

public class Model extends Node{
	private Matrix3d transform;
	private Geometry geometry;
	private Material material;
	
	public Model(){
		this.transform = new Matrix3d(); //optimize later to share a single matrix buffer.
		this.material = Material.getDefaultMaterial();
	}
	
	public Model(String name){
		super(name);
		this.transform = new Matrix3d(); //optimize later to share a single matrix buffer.
		this.material = Material.getDefaultMaterial();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.geometry.getBoundingBox();
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
	
	public Material getMaterial(){
		return this.material;
	}
	
	public void setMaterial(Material material){
		this.material = material;
	}
}
