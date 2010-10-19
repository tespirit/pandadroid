package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

/**
 * This class controls the creation of particles that are part of an emitter.
 * By having this as a class, different emitters can use the same particle generator.
 * @author Todd Espiritu Santo
 *
 */
public abstract class ParticleGenerator<P extends Particle>{
	private Vector3d mOffsetPosition;
	
	public ParticleGenerator(){
		this.mOffsetPosition = new Vector3d();
	}
	
	public void update(ParticleEmitter<P> emitter, float deltaTime){
		for(int i = 0; i < this.getBirthAmount(deltaTime); i++){
			@SuppressWarnings("unchecked")
			P p = (P) emitter.getParticleSysetm().add();
			if(p == null){
				return;
			}
			this.mOffsetPosition.set(this.getWidthOffset(), 0.0f, this.getLengthOffset());
			this.mOffsetPosition.add(emitter.getWorldTransform().getTranslation());
			p.setInitialPosition(this.mOffsetPosition);
			p.setInitialVelocity(emitter.getWorldTransform().transform(this.getVelocity()));
			p.setMass(this.getMass());
			this.initParticle(p);
		}
	}
	
	protected void initParticle(P p){
		//VOID
	}

	public abstract float getDecayPercent();
	public abstract float getLifeSpan();
	public abstract float getScale();
	public abstract int getBirthAmount(float deltaTime);
	public abstract Vector3d getVelocity();
	public abstract float getMass();
	public abstract float getWidthOffset();
	public abstract float getLengthOffset();
}
