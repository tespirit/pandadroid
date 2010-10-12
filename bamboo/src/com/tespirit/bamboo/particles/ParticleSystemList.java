package com.tespirit.bamboo.particles;


/**
 * Use this to create a dynamic amount of particles. This is a good particle system for testing and tuning particle properties
 * for particle generators.
 * @author Todd Espiritu Santo
 *
 */
public class ParticleSystemList extends ParticleSystem{
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
	
	public ParticleSystemList(){
		this.mHead = new ListNode();
		this.mEnd = new ListNode();
		this.mHead.mNext = this.mEnd;
		this.mEnd.mPrev = this.mHead;
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
}
