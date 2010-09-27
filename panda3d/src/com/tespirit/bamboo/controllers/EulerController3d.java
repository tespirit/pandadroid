package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;

/**
 * this class is intended for hooking up to
 * @author Todd Espiritu Santo
 *
 */
public class EulerController3d implements Controller3d{
	private Matrix3d matrix;
	
	private float xScale;
	private float yScale;
	private float zScale;
	private Euler type;
	
	public static enum Euler{
		XYZ,
		XZY,
		YXZ,
		YZX,
		ZXY,
		ZYX
	}
	
	public EulerController3d(Euler type){
		this(type, 1, 1, 1);
	}
	
	public EulerController3d(Euler type, float xScale, float yScale, float zScale){
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
		this.type = type;
	}
	
	public void setControlled(Matrix3d matrix) {
		this.matrix = matrix;
	}

	public void setControlled(Node node) {
		this.matrix = node.getTransform();
	}

	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.matrix = n.getTransform();
		}
	}
	
	@Override
	public void update(float x, float y, float z) {
		x *= this.xScale;
		y *= this.yScale;
		z *= this.zScale;
		switch(this.type){
		case XYZ:
			this.matrix.rotateX(x);
			this.matrix.rotateY(y);
			this.matrix.rotateZ(z);
			break;
		case XZY:
			this.matrix.rotateX(x);
			this.matrix.rotateZ(z);
			this.matrix.rotateY(y);
			break;
		case YXZ:
			this.matrix.rotateY(y);
			this.matrix.rotateX(x);
			this.matrix.rotateZ(z);
			break;
		case YZX:
			this.matrix.rotateY(y);
			this.matrix.rotateZ(z);
			this.matrix.rotateX(x);
			break;
		case ZXY:
			this.matrix.rotateZ(z);
			this.matrix.rotateX(x);
			this.matrix.rotateY(y);
			break;
		case ZYX:
			this.matrix.rotateZ(z);
			this.matrix.rotateY(y);
			this.matrix.rotateX(x);
			break;
		}
	}

	@Override
	public void update(float x, float y, float z, long time) {
		this.update(x, y, z);
	}

}
