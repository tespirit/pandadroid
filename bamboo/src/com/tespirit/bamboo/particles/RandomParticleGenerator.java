package com.tespirit.bamboo.particles;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.RandomRange;
import com.tespirit.bamboo.vectors.Vector3d;

public class RandomParticleGenerator extends ParticleGenerator<StandardParticle> implements Externalizable{
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
	protected void initParticle(StandardParticle p){
		p.setScale(this.getScale());
		p.setLifeSpan(this.getLifeSpan(), this.getDecayPercent());
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

	//IO
	private static final long serialVersionUID = -3813429521787814637L;
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		this.writeRange(out, this.mSpeed);
		this.writeRange(out, this.mAngle);
		this.writeRange(out, this.mBirthRate);
		this.writeRange(out, this.mLifeSpan);
		this.writeRange(out, this.mScale);
		this.writeRange(out, this.mDecayPercent);
		this.writeRange(out, this.mMass);
		out.writeFloat(this.mLength);
		out.writeFloat(this.mWidth);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.readRange(in, this.mSpeed);
		this.readRange(in, this.mAngle);
		this.readRange(in, this.mBirthRate);
		this.readRange(in, this.mLifeSpan);
		this.readRange(in, this.mScale);
		this.readRange(in, this.mDecayPercent);
		this.readRange(in, this.mMass);
		this.mLength = in.readFloat();
		this.mWidth = in.readFloat();
	}
	
	public void writeRange(ObjectOutput out, RandomRange range) throws IOException{
		out.writeFloat(range.getMin());
		out.writeFloat(range.getMax());
	}
	
	public void readRange(ObjectInput in, RandomRange range) throws IOException{
		range.setMin(in.readFloat());
		range.setMax(in.readFloat());
	}
}
