package com.tespirit.panda3d.vectors;

public class AxisAlignedBox {
	private Vector3d min;
	private Vector3d max;
	
	private Vector3d computedMin;
	private Vector3d computedMax;
	
	public AxisAlignedBox(){
		float[] buffer = new float[Vector3d.SIZE*4];
		this.min = new Vector3d(buffer);
		this.max = new Vector3d(buffer,Vector3d.SIZE);
		this.computedMin = new Vector3d(buffer,Vector3d.SIZE*2);
		this.computedMax = new Vector3d(buffer,Vector3d.SIZE*3);
	}
	
	public boolean pointInside(Vector3d point){
		return this.computedMin.getX() <= point.getX() && this.computedMin.getY() <= point.getY() && this.computedMin.getZ() <= point.getZ() &&
			   this.computedMin.getX() >= point.getX() && this.computedMin.getY() >= point.getY() && this.computedMin.getZ() >= point.getZ();
	}
	
	public void grow(Vector3d point){
		for(int i = 0; i < Vector3d.SIZE; i++){
			if(point.get(i) < this.min.get(i)){
				this.min.setAt(point.get(i), i);
			} else if (point.get(i) > this.max.get(i)){
				this.max.setAt(point.get(i), i);
			}
		}
		this.computedMin.copy(this.min);
		this.computedMax.copy(this.max);
	}
	
	public void grow(AxisAlignedBox box){
		this.grow(box.min);
		this.grow(box.max);
	}
	
	public AxisAlignedBox transformBy(Matrix3d m){
		this.computedMin.add(m.getTranslation());
		this.computedMax.add(m.getTranslation());
		return this;
	}
	
	public Vector3d getMin(){
		return this.computedMin;
	}
	
	public Vector3d getMax(){
		return this.computedMax;
	}
}
