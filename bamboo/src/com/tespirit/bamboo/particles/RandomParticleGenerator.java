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
	private float mLength;
	private float mWidth;
	
	private float mBirthRemainder;
	
	public RandomParticleGenerator(){
		this.mAngle = new RandomRange(0,0);
		this.mBirthRate = new RandomRange(1,1);
		this.mSpeed = new RandomRange(0,0);
		this.mLifeSpan = new RandomRange(1,1);
		this.mDecayPercent = new RandomRange(0,0);
		this.mMass = new RandomRange(1,1);
		this.mScale = new RandomRange(1,1);
	}
	
	public RandomRange getAngleRange(){
		return this.mAngle;
	}
	
	public RandomRange getBirthRateRange(){
		return this.mBirthRate;
	}
	
	public RandomRange getSpeedRange(){
		return this.mSpeed;
	}
	
	public RandomRange getLifeSpanRange(){
		return this.mLifeSpan;
	}
	
	public RandomRange getDecayPercentRange(){
		return this.mDecayPercent;
	}
	
	public RandomRange getMassRange(){
		return this.mMass;
	}
	
	public RandomRange getScaleRange(){
		return this.mScale;
	}
	
	public void setWidth(float width){
		this.mWidth = width;
	}
	
	public float getWidth(){
		return this.mWidth;
	}
	
	public void setLength(float length){
		this.mLength = length;
	}
	
	public float getLength(){
		return this.mLength;
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
		
		Vector3d velocity = new Vector3d(y, x, z); //flip the values so that the default orientation is up
		
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

	@Override
	public float getWidthOffset() {
		return mWidth*((float)Math.random()-0.5f);
	}

	@Override
	public float getLengthOffset() {
		return mLength*((float)Math.random()-0.5f);
	}
}
