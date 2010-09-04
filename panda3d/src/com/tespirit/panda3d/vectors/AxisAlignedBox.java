package com.tespirit.panda3d.vectors;

public class AxisAlignedBox {
	public Vector3d min;
	public Vector3d max;
	
	public boolean pointInside(Vector3d point){
		return this.min.getX() <= point.getX() && this.min.getY() <= point.getY() && this.min.getZ() <= point.getZ() &&
			   this.max.getX() >= point.getX() && this.max.getY() >= point.getY() && this.max.getZ() >= point.getZ();
	}
}
