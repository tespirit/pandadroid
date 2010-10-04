package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.vectors.Vector3d;

public abstract class AxisController2d extends MatrixController2d{
	protected Vector3d mXAxis;
	protected Vector3d mYAxis;
	protected float mScale;
	
	public AxisController2d(){
		this(Dof3.X,Dof3.Y);
	}
	
	public AxisController2d(Dof3 xMap, Dof3 yMap){
		this.mXAxis = new Vector3d();
		this.mYAxis = new Vector3d();
		this.mScale = 1.0f;
		
		this.setDof3(xMap, this.mXAxis);
		this.setDof3(yMap, this.mYAxis);
	}
	
	public AxisController2d(Vector3d xAxis, Vector3d yAxis){
		this.mXAxis = xAxis.clone().normalize();
		this.mYAxis = yAxis.clone().normalize();
		this.mScale = 1.0f;
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
	
	public void setAxis(Vector3d x, Vector3d y){
		this.mXAxis.copy(x);
		this.mYAxis.copy(y);
	}
	
	public void setScale(float scale){
		this.mScale = scale;
	}
}
