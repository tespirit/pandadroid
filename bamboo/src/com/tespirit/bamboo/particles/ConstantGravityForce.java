package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public class ConstantGravityForce implements ParticleForce{
	private Vector3d mAcceleration;
	private Vector3d mTempAccel;
	
	private static final float EARTH_GRAVITY = -9.8f;
	
	public ConstantGravityForce(){
		this.mAcceleration = new Vector3d(0, EARTH_GRAVITY, 0);
		this.mTempAccel = new Vector3d();
	}

	@Override
	public void apply(Particle particle) {
		this.mTempAccel.copy(this.mAcceleration);
		this.mTempAccel.scale(particle.getMass());
		particle.applyForce(this.mTempAccel);
	}
	
	public Vector3d getAcceleration(){
		return this.mAcceleration;
	}
	
	public void setAcceleration(Vector3d acceleration){
		this.mAcceleration.copy(acceleration);
	}
}
