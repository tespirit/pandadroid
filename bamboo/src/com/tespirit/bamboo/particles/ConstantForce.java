package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public class ConstantForce implements ParticleForce{
	public Vector3d mForce;
	
	public ConstantForce(){
		this.mForce = new Vector3d();
	}
	
	public void setForce(Vector3d force){
		this.mForce.copy(force);
	}
	
	public Vector3d getForce(){
		return this.mForce;
	}

	@Override
	public void apply(Particle particle) {
		particle.applyForce(this.mForce);
	}
}
