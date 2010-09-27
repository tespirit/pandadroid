package com.tespirit.bamboo.primitives;

import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Vector3d;

public class WireBox extends LineList{
	
	public WireBox(){
		this(new Vector3d(-0.5f, -0.5f, -0.5f), new Vector3d(0.5f, 0.5f, 0.5f));
	}
	
	public WireBox(AxisAlignedBox box){
		this(box.getMin(), box.getMax());
	}
	
	public WireBox(Vector3d min, Vector3d max) {
		super(24, new int[]{VertexBuffer.POSITION});
		this.setBox(min, max);
		this.renderAsLines();
	}
	
	public void setBox(AxisAlignedBox box){
		this.setBox(box.getMin(), box.getMax());
	}
	
	public void setBox(Vector3d min, Vector3d max){
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), max.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), min.getY(), max.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		
		this.vertexBuffer.addPosition(max.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		
		
		this.vertexBuffer.addPosition(max.getX(), min.getY(), min.getZ());
		this.vertexBuffer.addPosition(max.getX(), min.getY(), max.getZ());
		
		this.vertexBuffer.addPosition(min.getX(), max.getY(), min.getZ());
		this.vertexBuffer.addPosition(min.getX(), max.getY(), max.getZ());
		
		
		this.vertexBuffer.resetBufferPosition();
	}
}
