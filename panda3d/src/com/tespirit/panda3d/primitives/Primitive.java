package com.tespirit.panda3d.primitives;

import com.tespirit.panda3d.vectors.AxisAlignedBox;

public abstract class Primitive {
	
	protected int type;
	
	private static int[] enumTypes = {0,1,2,3};
	private static final int TRIANGLES = 0;
	private static final int TRIANGLE_STRIP = 1;
	private static final int TRIANGLE_FAN = 2;
	private static final int LINES = 3;
	
	public static void setTypeEnums(int triangles, int triangleStrip, int triangleFan, int lines){
		Primitive.enumTypes[Primitive.TRIANGLES] = triangles;
		Primitive.enumTypes[Primitive.TRIANGLE_STRIP] = triangleStrip;
		Primitive.enumTypes[Primitive.TRIANGLE_FAN] = triangleFan;
		Primitive.enumTypes[Primitive.LINES] = lines;
	}
	
	public int getTypeEnum(){
		return Primitive.enumTypes[this.type];
	}
	
	protected void renderAsTriangles(){
		this.type = Primitive.TRIANGLES;
	}
	
	protected void renderAsTriangleStrip(){
		this.type = Primitive.TRIANGLE_STRIP;
	}
	
	protected void renderAsTriangleFan(){
		this.type = Primitive.TRIANGLE_FAN;
	}
	
	protected void renderAsLines(){
		this.type = Primitive.LINES;
	}
	
	abstract public void computeBoundingBox(AxisAlignedBox boundingBox);
	
	abstract public void render();
}
