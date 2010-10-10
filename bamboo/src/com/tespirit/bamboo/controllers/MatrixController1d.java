package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;

public interface MatrixController1d extends Controller1d{

	public void setControlled(Matrix3d matrix);
	public void setControlled(Node node);
	public void setControlled(String nodeName);
}
