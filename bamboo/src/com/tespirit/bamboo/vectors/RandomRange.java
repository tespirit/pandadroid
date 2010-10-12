package com.tespirit.bamboo.vectors;

public class RandomRange {
	private float mMin;
	private float mMax;
	
	public RandomRange(float min, float max){
		this.mMin = min;
		this.mMax = max;
	}
	
	public float generateValue(){
		return (float)Math.random()*(this.mMax - this.mMin) + this.mMin;
	}
}
