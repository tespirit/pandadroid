package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

public class GravityForce implements ParticleForce{
	public static final float GRAVITATIONAL_CONSTANT = 6.673e-11f;
	
	private Vector3d mPosition;
	private Vector3d mForce;
	private float mMass;
	
	public GravityForce(){
		this.mPosition = new Vector3d();
		this.mForce = new Vector3d();
		this.mMass = 1;//default.
	}
	
	@Override
	public void apply(Particle particle) {
		this.mForce.sub(this.mPosition, particle.getPosition());
		float dist2 = this.mForce.magnitude2();
		if(dist2 > 0){
			float scale = (GRAVITATIONAL_CONSTANT*particle.getMass()*this.mMass)/dist2;
			this.mForce.normalize();
			this.mForce.scale(scale);
			particle.applyForce(this.mForce);
		}
	}
	
	public void setMass(float mass){
		this.mMass = mass;
	}
	
	public float getMass(){
		return this.mMass;
	}
	
	public void setPosition(Vector3d position){
		this.mPosition.copy(position);
	}
	
	public Vector3d getPosition(){
		return this.mPosition;
	}
	
}
