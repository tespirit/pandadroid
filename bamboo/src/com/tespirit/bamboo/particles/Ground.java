package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.Vector3d;

public class Ground implements ParticleForce, Externalizable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -390810931935824970L;
	float mFriction;
	float mElasticity;
	float mHeight;
	
	public Ground(){
		this.mFriction = 0.5f;
		this.mElasticity = 0.5f;
		this.mHeight = 0;
	}

	@Override
	public void apply(Particle particle) {
		float y = this.mHeight - particle.getPosition().getY();
		if(y > 0){
			Vector3d currentVelocity = particle.getVelocity();
			Vector3d position = particle.getPosition().clone();
			if(currentVelocity.getY() < 0.0001){
				position.setY(this.mHeight);
			} else {
				position.setY(y);
			}
			particle.setInitialPosition(position);
			float friction = (1-this.mFriction);
			particle.setInitialVelocity(new Vector3d(currentVelocity.getX()*friction,
													 -currentVelocity.getY()*this.mElasticity,
													 currentVelocity.getZ()*friction));
		}
	}
	
	public float getFriction(){
		return this.mFriction;
	}
	
	public void setFriction(float friction){
		this.mFriction = friction;
	}
	
	public float getElasticity(){
		return this.mElasticity;
	}
	
	public void setElasticity(float elasticity){
		this.mElasticity = elasticity;
	}
	
	public float getHeight(){
		return this.mHeight;
	}
	
	public void setHeight(float height){
		this.mHeight = height;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.mElasticity);
		out.writeFloat(this.mFriction);
		out.writeFloat(this.mHeight);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mElasticity = in.readFloat();
		this.mFriction = in.readFloat();
		this.mHeight = in.readFloat();
	}
	
}
