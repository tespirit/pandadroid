package com.tespirit.panda3d.controllers;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

public abstract class MatrixController2d implements Controller2d{
	protected Matrix3d m;
	protected Vector3d xMapAxis;
	protected Vector3d yMapAxis;
	protected float scale;
	
	public MatrixController2d(){
		this(Dof3.X,Dof3.Y);
	}
	
	public MatrixController2d(Dof3 xMap, Dof3 yMap){
		this.xMapAxis = new Vector3d();
		this.yMapAxis = new Vector3d();
		this.scale = 1.0f;
		
		this.setDof3(xMap, this.xMapAxis);
		this.setDof3(yMap, this.yMapAxis);
	}
	
	public MatrixController2d(Vector3d xMapAxis, Vector3d yMapAxis){
		this.xMapAxis = xMapAxis.clone().normalize();
		this.yMapAxis = yMapAxis.clone().normalize();
		this.scale = 1.0f;
	}
	
	private void setDof3(Dof3 dof, Vector3d axis){
		switch(dof){
		case X:
			axis.set(1, 0, 0);
			break;
		case Y:
			axis.set(0, 1, 0);
			break;
		case Z:
			axis.set(0, 0, 1);
			break;
		case negativeX:
			axis.set(-1, 0, 0);
			break;
		case negativeY:
			axis.set(0, -1, 0);
			break;
		case negativeZ:
			axis.set(0, 0, -1);
			break;
		}
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}

	public void setControlled(Matrix3d matrix) {
		this.m = matrix;
	}

	public void setControlled(Node node) {
		this.m = node.getTransform();
	}

	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.m = n.getTransform();
		}
	}
}
