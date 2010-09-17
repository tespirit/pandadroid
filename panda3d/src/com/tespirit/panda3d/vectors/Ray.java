package com.tespirit.panda3d.vectors;

public class Ray {
	Vector3d position;
	Vector3d direction;
	
	public Ray(){
		float[] buffer = new float[Vector3d.SIZE*2];
		this.position = new Vector3d(buffer);
		this.direction = new Vector3d(buffer, Vector3d.SIZE);
	}
	
	public void setPostion(float x, float y, float z){
		this.position.set(x, y, z);
	}
	
	public void setDirection(float x, float y, float z){
		this.direction.set(x, y, z);
		this.direction.setDirectional();
		this.direction.normalize();
	}
	
	public Ray transformBy(Matrix3d m){
		m.transform(this.position);
		m.transform(this.direction);
		return this;
	}
}
