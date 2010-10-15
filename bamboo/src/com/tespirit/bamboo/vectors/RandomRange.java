package com.tespirit.bamboo.vectors;

public class RandomRange extends Range<Float> {
	
	public RandomRange(float min, float max){
		super(min, max);
	}
	
	public float generateValue(){
		return (float)Math.random()*(this.mMax - this.mMin) + this.mMin;
	}
}
