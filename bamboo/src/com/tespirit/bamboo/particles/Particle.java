package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public abstract class Particle {
	private Vector3d mPosition;
	private Vector3d mVelocity;
	private Vector3d mForce;
	private float mMass;
	
	public Particle(){
		float[] buffer = Vector3d.createBuffer(3);
		this.mPosition = new Vector3d(buffer);
		this.mVelocity = new Vector3d(buffer, Vector3d.SIZE);
		this.mForce = new Vector3d(buffer, Vector3d.SIZE*2);
		this.mMass = 1;
	}
	
	public void setInitialVelocity(Vector3d initialVelocity){
		this.mVelocity.copy(initialVelocity);
	}
	
	public void setInitialPosition(Vector3d initialPosition){
		this.mPosition.copy(initialPosition);
	}
	
	public void setMass(float mass){
		this.mMass = mass;
	}
	
	public void applyForce(Vector3d force){
		this.mForce.add(force);
	}
	
	public void update(float deltaTime){
		this.mVelocity.addScale(this.mForce, deltaTime/this.mMass);
		this.mPosition.addScale(this.mVelocity, deltaTime);
		this.mForce.set(0, 0, 0);
	}
	
	public Vector3d getPosition(){
		return this.mPosition;
	}
	
	public Vector3d getVelocity(){
		return this.mVelocity;
	}
	
	public float getMass(){
		return this.mMass;
	}
	
	public abstract void render();
	
	public abstract boolean isAlive();
}
