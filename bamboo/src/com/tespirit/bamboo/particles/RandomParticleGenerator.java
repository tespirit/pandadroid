package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.RandomRange;
import com.tespirit.bamboo.vectors.Vector3d;

public class RandomParticleGenerator extends ParticleGenerator{
	private RandomRange mSpeed;
	private RandomRange mAngle;
	private RandomRange mBirthRate;
	private RandomRange mLifeSpan;
	private RandomRange mScale;
	private RandomRange mDecayPercent;
	private RandomRange mMass;
	
	private float mBirthRemainder;
	
	public RandomParticleGenerator(){
		this.setAngleRange(0, 0);
		this.setBirthRateRange(1, 1);
		this.setLifeSpanRange(1, 1);
		this.setSpeedRange(0, 0);
		this.setScaleRange(1, 1);
		this.setDecayPercentRange(0, 0);
		this.setMassRange(1, 1);
	}
	
	public void setSpeedRange(float min, float max){
		this.mSpeed = new RandomRange(min, max);
	}
	
	public void setAngleRange(float min, float max){
		this.mAngle = new RandomRange(min, max);
	}
	
	public void setBirthRateRange(float min, float max){
		this.mBirthRate = new RandomRange(min, max);
	}
	
	public void setLifeSpanRange(float min, float max){
		this.mLifeSpan = new RandomRange(min, max);
	}
	
	public void setScaleRange(float min, float max){
		this.mScale = new RandomRange(min, max);
	}
	
	public void setDecayPercentRange(float min, float max){
		this.mDecayPercent = new RandomRange(min, max);
	}
	
	public void setMassRange(float min, float max){
		this.mMass = new RandomRange(min, max);
	}
	
	@Override
	public int getBirthAmount(float deltaTime){
		float amount = this.mBirthRate.generateValue()*deltaTime+this.mBirthRemainder;
		float baseAmount = (float)Math.floor(amount);
		this.mBirthRemainder = amount - baseAmount;
		return (int)baseAmount;
	}
	
	@Override
	public Vector3d getVelocity(){
		double angle = Math.toRadians(this.mAngle.generateValue());
		float speed = this.mSpeed.generateValue();
		float x = speed*(float)Math.cos(angle);
		float ry = speed*(float)Math.sin(angle);
		angle = Math.random()*Math.PI*2;
		float y = ry*(float)Math.sin(angle);
		float z = ry*(float)Math.cos(angle);
		
		Vector3d velocity = new Vector3d(x, y, z); //flip the values so that the default orientation is up
		
		return velocity;
	}

	@Override
	public float getMass() {
		return this.mMass.generateValue();
	}

	@Override
	public float getDecayPercent() {
		return this.mDecayPercent.generateValue();
	}

	@Override
	public float getLifeSpan() {
		return this.mLifeSpan.generateValue();
	}

	@Override
	public float getScale() {
		return this.mScale.generateValue();
	}
}
