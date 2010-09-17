package com.tespirit.panda3d.vectors;

public class AxisAlignedBox {
	private Vector3d min;
	private Vector3d max;
	
	public AxisAlignedBox(){
		float[] buffer = new float[Vector3d.SIZE*2];
		this.min = new Vector3d(buffer);
		this.min.setPositional();
		this.max = new Vector3d(buffer,Vector3d.SIZE);
		this.min.setPositional();
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
		
		Vector3d toMin = new Vector3d();
		toMin.sub(this.min, position);
		
		direction.projectOnTo(toMin);
		toMin.add(position);
		
		return this.min.getX() <= toMin.getX() && 
			   this.min.getY() <= toMin.getY() && 
			   this.max.getX() >= toMin.getX() && 
			   this.max.getY() >= toMin.getY();
	}
	
	public void grow(Vector3d point){
		for(int i = 0; i < Vector3d.SIZE; i++){
			if(point.get(i) < this.min.get(i)){
				this.min.setAt(point.get(i), i);
			} else if (point.get(i) > this.max.get(i)){
				this.max.setAt(point.get(i), i);
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
		return this.min;
	}
}
