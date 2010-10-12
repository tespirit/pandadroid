package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

/**
 * This class controls the creation of particles that are part of an emitter.
 * By having this as a class, different emitters can use the same particle generator.
 * @author Todd Espiritu Santo
 *
 */
public abstract class ParticleGenerator{
	
	public void update(ParticleEmitter emitter, float deltaTime){
		for(int i = 0; i < this.getBirthAmount(deltaTime); i++){
			Particle p = emitter.createParticle();
			p.setInitialPosition(emitter.getWorldTransform().getTranslation());
			p.setInitialVelocity(emitter.getWorldTransform().transform(this.getVelocity()));
			p.setMass(this.getMass());
			if(p instanceof StandardParticle){
				StandardParticle sp = (StandardParticle)p;
				sp.setScale(this.getScale());
				sp.setLifeSpan(this.getLifeSpan(), this.getDecayPercent());
			}
		}
	}

	public abstract float getDecayPercent();
	public abstract float getLifeSpan();
	public abstract float getScale();
	public abstract int getBirthAmount(float deltaTime);
	public abstract Vector3d getVelocity();
	public abstract float getMass();
}
