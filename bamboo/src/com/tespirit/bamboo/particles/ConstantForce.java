package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.Vector3d;

public class ConstantForce implements ParticleForce, Externalizable{
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
	
	//IO
	private static final long serialVersionUID = 4904780095829788312L;

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(this.mForce.getX());
		out.writeFloat(this.mForce.getY());
		out.writeFloat(this.mForce.getZ());
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mForce.set(in.readFloat(), in.readFloat(), in.readFloat());
	}
}
