package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;

public abstract class BaseMatrixController2d extends BaseController2d implements MatrixController2d{
	protected Matrix3d mControlled;

	public void setControlled(Matrix3d matrix) {
		this.mControlled = matrix;
	}

	public void setControlled(Node node) {
		this.setControlled(node.getTransform());
	}

	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.setControlled(n);
		}
	}
}
