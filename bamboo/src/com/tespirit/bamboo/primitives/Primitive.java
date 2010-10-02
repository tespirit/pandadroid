package com.tespirit.bamboo.primitives;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.vectors.AxisAlignedBox;

public abstract class Primitive{
	public static final int TRIANGLES = 0;
	public static final int TRIANGLE_STRIP = 1;
	public static final int TRIANGLE_FAN = 2;
	public static final int LINES = 3;
	public static final int LINE_STRIP = 4;
	public static final int POINTS = 5;
	
	public static final int TYPE_COUNT = 6;
	
	private int type;
	
	public int getType(){
		return this.type;
	}
	
	public void renderAsTriangles(){
		this.type = Primitive.TRIANGLES;
	}
	
	public void renderAsTriangleStrip(){
		this.type = Primitive.TRIANGLE_STRIP;
	}
	
	public void renderAsTriangleFan(){
		this.type = Primitive.TRIANGLE_FAN;
	}
	
	public void renderAsLines(){
		this.type = Primitive.LINES;
	}
	
	public void renderAsLineStrip(){
		this.type = Primitive.LINE_STRIP;
	}
	
	public void renderAsPoints(){
		this.type = Primitive.POINTS;
	}
	
	protected void write(ObjectOutput out) throws IOException{
		out.writeInt(this.type);
	}
	
	protected void read(ObjectInput input) throws IOException{
		this.type = input.readInt();
	}
	
	abstract public void computeBoundingBox(AxisAlignedBox boundingBox);
	
	abstract public void render();
	
	abstract public void updateModifiers();
}
