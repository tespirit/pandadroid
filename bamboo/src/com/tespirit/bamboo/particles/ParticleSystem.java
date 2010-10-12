package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public abstract class ParticleSystem{
	
	protected Vector3d mForce;
	
	public ParticleSystem(){
		mForce = new Vector3d();
	}

	public void setForce(Vector3d force){
		this.mForce.copy(force);
	}
	
	public void zeroOutForce(){
		this.mForce.set(0, 0, 0);
	}
	
	public abstract void update(float deltaTime);
	public abstract void render();
	public abstract void add(Particle p);
	public abstract void remove(Particle p);
	public abstract void recycle();
}
