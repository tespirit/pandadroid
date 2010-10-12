package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public abstract class ConstantForceParticleSystem implements ParticleSystem
{
	
	protected Vector3d mForce;
	
	public ConstantForceParticleSystem(){
		mForce = new Vector3d();
	}

	public void setForce(Vector3d force){
		this.mForce.copy(force);
	}
	
	public void zeroOutForce(){
		this.mForce.set(0, 0, 0);
	}
	
	public Vector3d getForce(){
		return this.mForce;
	}
}
