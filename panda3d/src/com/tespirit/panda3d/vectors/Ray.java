package com.tespirit.panda3d.vectors;

public class Ray {
	private Vector3d position;
	private Vector3d direction;
	
	public Ray(){
		float[] buffer = Vector3d.createBuffer(2);
		this.position = new Vector3d(buffer);
		this.direction = new Vector3d(buffer, Vector3d.SIZE);
		//this will make sure that this vector only rotates when transformed.
		this.direction.makeDirectional();
	}
	
	public void copy(Ray ray){
		this.position.copy(ray.position);
		this.direction.copy(ray.direction);
	}
	
	public Ray clone(){
		Ray ray = new Ray();
		ray.copy(this);
		return ray;
	}
	
	public Vector3d getDirection(){
		return this.direction;
	}
	
	public Vector3d getPosition(){
		return this.position;
	}
	
	public void setPostion(float x, float y, float z){
		this.position.set(x, y, z);
	}
	
	public void setPosition(Vector3d v){
		this.position.copy(v);
	}
	
	public void setDirection(float x, float y, float z){
		this.direction.set(x, y, z);
		this.direction.normalize();
	}
	
	public void setDirection(Vector3d v){
		this.direction.copy(v);
		this.direction.normalize();
		this.direction.makeDirectional();
	}
	
	public Ray transformBy(Ray in, Matrix3d m){
		m.transform(in.position, this.position);
		m.transform(in.direction, this.direction);
		this.direction.normalize();
		return this;
	}
	
	public Ray transformBy(Matrix3d m){
		m.transform(this.position);
		m.transform(this.direction);
		this.direction.normalize();
		return this;
	}
}
