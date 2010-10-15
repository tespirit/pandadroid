package com.tespirit.bamboo.vectors;

public class Range<T extends Number> {
	protected T mMin;
	protected T mMax;

	public Range(T min, T max){
		this.mMin = min;
		this.mMax = max;
	}
	public void setMin(T min){
		this.mMin = min;
	}
	
	public void setMax(T max){
		this.mMax = max;
	}
	
	public T getMin(){
		return this.mMin;
	}
	
	public T getMax(){
		return this.mMax;
	}
}
