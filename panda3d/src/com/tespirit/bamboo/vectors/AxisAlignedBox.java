package com.tespirit.bamboo.vectors;

import java.io.Serializable;

public class AxisAlignedBox implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector3d min;
	private Vector3d max;
	
	public AxisAlignedBox(){
		float[] buffer = Vector3d.createBuffer(2);
		this.min = new Vector3d(buffer);
		this.max = new Vector3d(buffer,Vector3d.SIZE);
	}
	
	public AxisAlignedBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		this();
		this.min.set(minX, minY, minZ);
		this.max.set(maxX, maxY, maxZ);
	}
	
	public void setMin(float x, float y, float z){
		this.min.set(x, y, z);
	}
	
	public void setMax(float x, float y, float z){
		this.max.set(x, y, z);
	}
	
	public void setMin(Vector3d v){
		this.min.copy(v);
	}
	
	public void setMax(Vector3d v){
		this.max.copy(v);
	}
	
	public boolean pointInside(Vector3d point){
		return this.min.getX() <= point.getX() && 
			   this.min.getY() <= point.getY() && 
			   this.min.getZ() <= point.getZ() &&
			   this.max.getX() >= point.getX() && 
			   this.max.getY() >= point.getY() && 
			   this.max.getZ() >= point.getZ();
	}
	
	public boolean intersectsRay(Ray ray){
		Vector3d direction = ray.getDirection();
		Vector3d position = ray.getPosition();
		
		float distanceNear = Float.NEGATIVE_INFINITY;
		float distanceFar = Float.POSITIVE_INFINITY;
		
		for(int i = 0; i < 3; i++){
			float boxMin = this.min.get(i);
			float boxMax = this.max.get(i);
			float slope = direction.get(i);
			float offset = position.get(i);
			
			if(!Util.floatEquals(slope,0)){
				float distanceMin;
				float distanceMax;
				
				if(slope > 0){
					distanceMin = (boxMin-offset)/slope;
					distanceMax = (boxMax-offset)/slope;
				} else {
					distanceMin = (boxMax-offset)/slope;
					distanceMax = (boxMin-offset)/slope;
				}
				
				//see if this is closer
				if(distanceMin > distanceNear){
					distanceNear = distanceMin;
				}
				if(distanceMax < distanceFar){
					distanceFar = distanceMax;
				}
				
				if(distanceNear > distanceFar){
					return false;
				}
			} else if(offset < boxMin || offset > boxMax){
				return false;
			}
		}
		
		return true;
	}
	
	public void grow(Vector3d point){
		for(int i = 0; i < Vector3d.SIZE; i++){
			if(point.get(i) < this.min.get(i)){
				this.min.setAt(i, point.get(i));
			} else if (point.get(i) > this.max.get(i)){
				this.max.setAt(i, point.get(i));
			}
		}
	}
	
	public void grow(AxisAlignedBox box){
		this.grow(box.min);
		this.grow(box.max);
	}
		
	public Vector3d getMin(){
		return this.min;
	}
	
	public Vector3d getMax(){
		return this.max;
	}
}
