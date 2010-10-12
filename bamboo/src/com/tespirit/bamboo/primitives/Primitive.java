package com.tespirit.bamboo.primitives;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.Resource;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public abstract class Primitive implements Resource{
	public static final int TRIANGLES = 0;
	public static final int TRIANGLE_STRIP = 1;
	public static final int TRIANGLE_FAN = 2;
	public static final int LINES = 3;
	public static final int LINE_STRIP = 4;
	public static final int POINTS = 5;
	
	public static final int TYPE_COUNT = 6;
	
	private int mType;
	
	public int getType(){
		return this.mType;
	}
	
	public void renderAsTriangles(){
		this.mType = Primitive.TRIANGLES;
	}
	
	public void renderAsTriangleStrip(){
		this.mType = Primitive.TRIANGLE_STRIP;
	}
	
	public void renderAsTriangleFan(){
		this.mType = Primitive.TRIANGLE_FAN;
	}
	
	public void renderAsLines(){
		this.mType = Primitive.LINES;
	}
	
	public void renderAsLineStrip(){
		this.mType = Primitive.LINE_STRIP;
	}
	
	public void renderAsPoints(){
		this.mType = Primitive.POINTS;
	}
	
	protected void write(ObjectOutput out) throws IOException{
		out.writeInt(this.mType);
	}
	
	protected void read(ObjectInput input) throws IOException{
		this.mType = input.readInt();
	}
	
	abstract public void computeBoundingBox(AxisAlignedBox boundingBox);
	
	abstract public void render();
	
	abstract public void update();
}
