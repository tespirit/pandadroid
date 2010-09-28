package com.tespirit.bamboo.primitives;

import java.io.Serializable;

import com.tespirit.bamboo.vectors.AxisAlignedBox;

public abstract class Primitive implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7735764148071638696L;

	protected int type;
	
	private static int[] enumTypes = {0,1,2,3,4, 5};
	private static final int TRIANGLES = 0;
	private static final int TRIANGLE_STRIP = 1;
	private static final int TRIANGLE_FAN = 2;
	private static final int LINES = 3;
	private static final int LINE_STRIP = 4;
	private static final int POINTS = 5;
	
	public static void setTypeEnums(int triangles, 
									int triangleStrip, 
									int triangleFan, 
									int lines,
									int lineStrip,
									int points){
		Primitive.enumTypes[Primitive.TRIANGLES] = triangles;
		Primitive.enumTypes[Primitive.TRIANGLE_STRIP] = triangleStrip;
		Primitive.enumTypes[Primitive.TRIANGLE_FAN] = triangleFan;
		Primitive.enumTypes[Primitive.LINES] = lines;
		Primitive.enumTypes[Primitive.LINE_STRIP] = lineStrip;
		Primitive.enumTypes[Primitive.POINTS] = points;
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
	
	protected void renderAsLineStrip(){
		this.type = Primitive.LINE_STRIP;
	}
	
	protected void renderAsPoints(){
		this.type = Primitive.POINTS;
	}
	
	abstract public void computeBoundingBox(AxisAlignedBox boundingBox);
	
	abstract public void render();
}
