package com.tespirit.panda3d.controllers;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.Matrix3d;

public class AccelerationController3d implements Controller3d{
	Matrix3d matrix;
	
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

	public void setControlled(Matrix3d matrix) {
		this.matrix = matrix;
	}

	public void setControlled(Node node) {
		this.matrix = node.getTransform();
	}

	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.matrix = n.getTransform();
		}
	}
	
	
	/**
	 * values are assumed to be in m/s^2
	 */
	@Override
	public void update(float x, float y, float z) {
		this.update(x, y, z, 0);
	}

	/**
	 * values are assumed to be in m/s^2
	 * time assumed to be in nano seconds
	 */
	@Override
	public void update(float x, float y, float z, long time) {
		//compute position:
		time *= 0.5f*time;
		x = x*time*xScale;
		y = y*time*yScale;
		z = z*time*zScale;
		
		float tScale = this.timeScale*this.timeScale;
		x /= tScale;
		y /= tScale;
		z /= tScale;
		
		
		this.matrix.translate(x, y, z);
	}
	
}
