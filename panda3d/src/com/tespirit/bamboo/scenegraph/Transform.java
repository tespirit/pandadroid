package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.vectors.Matrix3d;

public abstract class Transform extends Node{
	protected Matrix3d transform;
	protected Matrix3d worldTransform;
	
	public Transform(){
		super();
		float[] m = new float[Matrix3d.SIZE*2];
		this.transform = new Matrix3d(m);
		this.transform.identity();
		this.worldTransform = new Matrix3d(m, Matrix3d.SIZE);
	}
	
	public Transform(String name){
		super(name);
		float[] m = new float[Matrix3d.SIZE*2];
		this.transform = new Matrix3d(m);
		this.transform.identity();
		this.worldTransform = new Matrix3d(m, Matrix3d.SIZE);
	}

	@Override
	public Matrix3d getTransform() {
		return this.transform;
	}

	@Override
	public void update(Matrix3d transform) {
		this.worldTransform.multiply(transform,this.transform);
		for(int i = 0; i < this.getChildCount(); i++){
			this.getChild(i).update(this.worldTransform);
		}
	}
	
	@Override
	public Matrix3d getWorldTransform() {
		return this.worldTransform;
	}
	
}
