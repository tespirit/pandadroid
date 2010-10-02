package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public abstract class MatrixController1d implements Controller1d{
	
	protected Vector3d mAxis;
	protected float mScale;
	protected Matrix3d mMatrix;
	
	public MatrixController1d(Dof3 axis){
		this.mAxis = new Vector3d();
		this.setDof3(axis);
	}
	
	private void setDof3(Dof3 dof){
		switch(dof){
		case X:
			this.mAxis.set(1, 0, 0);
			break;
		case Y:
			this.mAxis.set(0, 1, 0);
			break;
		case Z:
			this.mAxis.set(0, 0, 1);
			break;
		case negativeX:
			this.mAxis.set(-1, 0, 0);
			break;
		case negativeY:
			this.mAxis.set(0, -1, 0);
			break;
		case negativeZ:
			this.mAxis.set(0, 0, -1);
			break;
		}
	}

	public void setScale(float scale){
		this.mScale = scale;
	}

	public void setControlled(Matrix3d matrix) {
		this.mMatrix = matrix;
	}

	public void setControlled(Node node) {
		this.mMatrix = node.getTransform();
	}

	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.mMatrix = n.getTransform();
		}
	}
}
