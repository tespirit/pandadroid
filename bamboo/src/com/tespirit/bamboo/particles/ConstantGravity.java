package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.Vector3d;

public class ConstantGravity implements ParticleForce, Externalizable{
	private Vector3d mAcceleration;
	private Vector3d mTempAccel;
	
	private static final float EARTH_GRAVITY = -9.8f;
	
	public ConstantGravity(){
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
	
	//IO
	private static final long serialVersionUID = 267022522616565056L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.mAcceleration.getX());
		out.writeFloat(this.mAcceleration.getY());
		out.writeFloat(this.mAcceleration.getZ());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mAcceleration.set(in.readFloat(), in.readFloat(), in.readFloat());
	}
}
