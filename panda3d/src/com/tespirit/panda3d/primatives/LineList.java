package com.tespirit.panda3d.primatives;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.tespirit.panda3d.vectors.AxisAlignedBox;

public class Lines extends Primative{
	private FloatBuffer points;
	private int count;
	
	private static int stride = 3;
	
	public Lines(int pointCount){
		this.count = pointCount;
		ByteBuffer temp = ByteBuffer.allocateDirect(this.count*Lines.stride*4);
		temp.order(ByteOrder.nativeOrder());
		this.points = temp.asFloatBuffer();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FloatBuffer getPoints(){
		return this.points;
	}
	
	public int getPointCount(){
		return this.count;
	}
	
	public int getPointStride(){
		return Lines.stride;
	}
}
