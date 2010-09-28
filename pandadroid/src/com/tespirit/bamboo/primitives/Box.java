package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Vector3d;

public class Box extends TriangleIndices {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6007609877682419505L;

	public Box(AxisAlignedBox box){
		this(box.getMin(), box.getMax());
	}
	
	public Box(Vector3d min, Vector3d max){
		super(12*3, 24, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD});
	
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		this.vertexBuffer.addNormal(0, -1, 0);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
		
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		this.vertexBuffer.addNormal(0, 1, 0);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
		
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		this.vertexBuffer.addNormal(0, 0, 1);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
	
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		this.vertexBuffer.addNormal(0, 0, -1);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);

		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		this.vertexBuffer.addNormal(1, 0, 0);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
		
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		this.vertexBuffer.addNormal(-1, 0, 0);
		
		this.vertexBuffer.addTexcoord(0.0f, 0.0f);
		this.vertexBuffer.addTexcoord(0.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 1.0f);
		this.vertexBuffer.addTexcoord(1.0f, 0.0f);
		
		this.setBox(min, max);

		this.indexBuffer.addTriangle(0, 1, 2);
		this.indexBuffer.addTriangle(0, 2, 3);
		
		this.indexBuffer.addTriangle(4, 5, 6);
		this.indexBuffer.addTriangle(4, 6, 7);
		
		this.indexBuffer.addTriangle(8, 9, 10);
		this.indexBuffer.addTriangle(8, 10, 11);
		
		this.indexBuffer.addTriangle(12, 13, 14);
		this.indexBuffer.addTriangle(12, 14, 15);
		
		this.indexBuffer.addTriangle(16, 18, 17);
		this.indexBuffer.addTriangle(16, 19, 18);
		
		this.indexBuffer.addTriangle(20, 22, 21);
		this.indexBuffer.addTriangle(20, 23, 22);
		
		this.indexBuffer.resetBufferPosition();
	}
	
	public Box(){
		this(new AxisAlignedBox(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f));
	}
	
	public void setBox(Vector3d min, Vector3d max){
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		
		this.vertexBuffer.resetBufferPosition();
	}
	
	public void setBox(AxisAlignedBox box){
		this.setBox(box.getMin(), box.getMax());
	}
}
