package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.Vector3d;

public class Gravity implements ParticleForce, Externalizable{

	public static final float GRAVITATIONAL_CONSTANT = 6.673e-11f;
	
	private Vector3d mPosition;
	private Vector3d mForce;
	private float mStrength;
	
	public Gravity(){
		this.mPosition = new Vector3d();
		this.mForce = new Vector3d();
		this.mStrength = 0;
	}
	
	@Override
	public void apply(Particle particle) {
		this.mForce.sub(this.mPosition, particle.getPosition());
		float dist2 = this.mForce.magnitude2();
		if(dist2 > 0){
			float scale = (particle.getMass()*this.mStrength)/dist2;
			this.mForce.normalize();
			this.mForce.scale(scale);
			particle.applyForce(this.mForce);
		}
	}
	
	public void setStrength(float strength){
		this.mStrength = strength;
	}
	
	public float getStrength(){
		return this.mStrength;
	}
	
	public void setPosition(Vector3d position){
		this.mPosition.copy(position);
	}
	
	public Vector3d getPosition(){
		return this.mPosition;
	}


	//IO
	private static final long serialVersionUID = -2865545048524748751L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.mStrength);
		out.writeFloat(this.mPosition.getX());
		out.writeFloat(this.mPosition.getY());
		out.writeFloat(this.mPosition.getZ());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mStrength = in.readFloat();
		this.mPosition.set(in.readFloat(), in.readFloat(), in.readFloat());
	}
	
}
