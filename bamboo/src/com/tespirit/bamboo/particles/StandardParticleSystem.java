package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class StandardParticleSystem implements ParticleSystem, Externalizable{

	private class ListNode{
		private ListNode mNext;
		private ListNode mPrev;
		
		private ListNode(){
		}
		
		private void insertBefore(ListNode p){
			p.mNext = this;
			p.mPrev = this.mPrev;
			this.mPrev.mNext = p;
			this.mPrev = p;
		}
		
		protected void update(float deltaTime){
			//VOID
		}
		
		protected boolean isAlive(){
			return true;
		}
		
		protected void render(){
			//VOID
		}
		
		protected void remove(){
			this.mNext.mPrev = this.mPrev;
			this.mPrev.mNext = this.mNext;
			this.mNext = null;
			this.mPrev = null;
		}
	}
	
	private class ParticleNode extends ListNode{
		private Particle mParticle;
		
		private ParticleNode(Particle particle){
			this.mParticle = particle;
		}
		
		protected void update(float deltaTime){
			for(ParticleForce force : mForces){
				force.apply(this.mParticle);
			}
			this.mParticle.update(deltaTime);
		}
		
		protected boolean isAlive(){
			return this.mParticle.isAlive();
		}
		
		protected void render(){
			this.mParticle.render();
		}
	}
	
	private ListNode mHead;
	private ListNode mEnd;
	private List<ParticleForce> mForces;
	
	public StandardParticleSystem(){
		this.mForces = new ArrayList<ParticleForce>();
		this.mHead = new ListNode();
		this.mEnd = new ListNode();
		this.mHead.mNext = this.mEnd;
		this.mEnd.mPrev = this.mHead;
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
	
	public ParticleForce getForce(int i){
		return this.mForces.get(i);
	}
	
	public void update(float deltaTime){
		ListNode node = this.mHead.mNext;
		while(node != this.mEnd){
			node.update(deltaTime);
			if(node.isAlive()){
				node = node.mNext;
			} else {
				ListNode temp = node.mNext;
				node.remove();
				node = temp;
			}
		}
	}
	
	public void render(){
		ListNode node = this.mHead.mNext;
		while(node != this.mEnd){
			node.render();
			node = node.mNext;
		}
	}
	
	@Override
	public void recycle(){
		ListNode node = this.mEnd.mPrev;
		while(node != null){
			node.mNext.remove();
			node = node.mPrev;
		}
		this.mHead.remove();
	}

	@Override
	public void add(Particle p) {
		this.mEnd.insertBefore(new ParticleNode(p));
	}

	@Override
	public void remove(Particle p) {
		//TODO: implement this.
	}

	//IO
	private static final long serialVersionUID = -372026429838597827L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.mForces.size());
		for(ParticleForce force : this.mForces){
			out.writeObject(force);
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		int size = in.readInt();
		for(int i = 0; i < size; i++){
			this.mForces.add((ParticleForce)in.readObject());
		}
		
	}
}
