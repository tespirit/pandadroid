package com.tespirit.panda3d.vectors;

public class AxisAlignedBox {
	public Vector3d min;
	public Vector3d max;
	
	public AxisAlignedBox(){
		float[] buffer = new float[Vector3d.SIZE*2];
		this.min = new Vector3d(buffer,0);
		this.max = new Vector3d(buffer,Vector3d.SIZE);
	}
	
	public boolean pointInside(Vector3d point){
		return this.min.getX() <= point.getX() && this.min.getY() <= point.getY() && this.min.getZ() <= point.getZ() &&
			   this.max.getX() >= point.getX() && this.max.getY() >= point.getY() && this.max.getZ() >= point.getZ();
	}
	
	public void grow(Vector3d point){
		for(int i = 0; i < Vector3d.SIZE; i++){
			if(point.get(i) < min.get(i)){
				min.setAt(point.get(i), i);
			} else if (point.get(i) > max.get(i)){
				max.setAt(point.get(i), i);
			}
		}
	}
}
