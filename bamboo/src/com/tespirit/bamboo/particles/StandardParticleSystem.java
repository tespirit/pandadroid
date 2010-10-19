package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class StandardParticleSystem implements ParticleSystem, Externalizable{
	protected Particle[] mPatricles;
	protected int mActiveCount;
	protected int mMaxCount;

	private List<ParticleForce> mForces;
	
	public StandardParticleSystem(){
		this(1000);
	}
	
	public StandardParticleSystem(int maxCount){
		this.mMaxCount = maxCount;
		this.mActiveCount = 0;
		this.mForces = new ArrayList<ParticleForce>();
	}
	
	public void addForce(ParticleForce force){
		this.mForces.add(force);
	}
	
	public void removeForce(ParticleForce force){
		this.mForces.remove(force);
	}
	
	public void clearForces(){
		this.mForces.clear();
	}
	
	public int getForceCount(){
		return this.mForces.size();
	}
	
	public int getMaxParticleCount(){
		return this.mPatricles.length;
	}
	
	public ParticleForce getForce(int i){
		return this.mForces.get(i);
	}
	
	@Override
	public void update(float deltaTime){
		int i = 0;
		while(i < this.mActiveCount){
			Particle p = this.mPatricles[i];
			for(ParticleForce force : mForces){
				force.apply(p);
			}
			p.update(deltaTime);
			if(p.isAlive()){
				i++;
			} else {
				this.mActiveCount--;
				this.mPatricles[i] = this.mPatricles[this.mActiveCount];
				this.mPatricles[this.mActiveCount] = p;
				this.mActiveCount--;
			}
		}
	}
	
	@Override
	public void render(){
		for(int i = 0; i < this.mActiveCount; i++){
			this.mPatricles[i].render();
		}
	}
	
	@Override
	public void recycle(){
		for(int i = 0; i < this.mPatricles.length; i++){
			this.mPatricles[i] = null;
		}
	}
	
	@Override
	public Particle add() {
		if(this.mActiveCount < this.mPatricles.length){
			this.mActiveCount++;
			return this.mPatricles[this.mActiveCount-1];
		} else {
			return null;
		}
	}
	

	@Override
	public void remove(Particle p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(ParticleEmitter<?> p) {
		this.mPatricles = new Particle[this.mMaxCount];
		for(int i = 0; i < this.mPatricles.length; i++){
			this.mPatricles[i] = p.newParticle();
		}
	}

	//IO
	private static final long serialVersionUID = -372026429838597827L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.mMaxCount);
		out.writeInt(this.mForces.size());
		for(ParticleForce force : this.mForces){
			out.writeObject(force);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mMaxCount = in.readInt();
		int size = in.readInt();
		for(int i = 0; i < size; i++){
			this.mForces.add((ParticleForce)in.readObject());
		}
		
	}
}
