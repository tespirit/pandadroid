package com.tespirit.bamboo.animation;

public class DofStream {
	float[] dofs;
	int position;
	
	public DofStream(int count){
		this.dofs = new float[count];
		this.position = 0;		
	}
	
	public float getNext(){
		this.position++;
		return this.dofs[this.position-1];
	}
	
	public void setNext(float value){
		this.dofs[this.position] = value;
		this.position++;
	}
	
	public void reset(){
		this.position = 0;
	}
}
