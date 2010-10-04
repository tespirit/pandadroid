package com.tespirit.bamboo.controllers;

public class AccelerationController3d extends MatrixController3d{
	
	private float xScale;
	private float yScale;
	private float zScale;
	private float timeScale;
	
	public AccelerationController3d(){
		this(1,1,1, 10e-9f);
	}
	
	public AccelerationController3d(float xScale, float yScale, float zScale, float tScale){
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
		this.timeScale = tScale;
	}

	@Override
	protected void update(float[] values, long time, long deltaTime) {
		// TODO Auto-generated method stub
		time *= 0.5f*time;
		float x = values[BaseController3d.DELTA_X]*time*xScale;
		float y = values[BaseController3d.DELTA_Y]*time*yScale;
		float z = values[BaseController3d.DELTA_Z]*time*zScale;
		
		float tScale = this.timeScale*this.timeScale;
		x /= tScale;
		y /= tScale;
		z /= tScale;
		
		this.mControlled.translate(x, y, z);
	}
	
}
