package com.tespirit.bamboo.animation;

import java.io.Serializable;

public class DofStream implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6188834596391739597L;
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
