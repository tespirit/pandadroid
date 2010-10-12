package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Vector3d;

/**
 * Use this to create a standard particle that has a life span, a scale value, and a decay percent (other attributes
 * might be added that also seem common, such as rotation).
 * @author Todd Espiritu Santo
 *
 */
public abstract class StandardParticle extends Particle{
	private float mLifeSpan;
	private float mDecayStartTime;
	private float mScale;

	public void setLifeSpan(float lifeSpan, float decayPercent){
		this.mLifeSpan = lifeSpan;
		this.mDecayStartTime = lifeSpan*(1-decayPercent);
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		if(this.mDecayStartTime > 0){
			this.update(this.getPosition(), this.mScale);	
		} else {
			float decayLength = this.mLifeSpan - this.mDecayStartTime;
			this.update(this.getPosition(), this.mScale*(decayLength + this.mDecayStartTime)/decayLength);
		}
		mLifeSpan -= deltaTime;
		mDecayStartTime -= deltaTime;
	}
	
	protected abstract void update(Vector3d position, float scale);

	@Override
	public boolean isAlive() {
		return this.mLifeSpan > 0;
	}
	
	public void setScale(float scale){
		this.mScale = scale;
	}
}
