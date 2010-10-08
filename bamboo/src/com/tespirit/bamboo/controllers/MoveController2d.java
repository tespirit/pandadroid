package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Plane;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class MoveController2d extends MatrixController2d{
	private RenderManager mRenderManager;
	private Plane mPlane;
	private Matrix3d mInitialWorldMatrix;
	private Matrix3d mInverter;
	private Matrix3d mLocal;
	
	public MoveController2d(RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mPlane = new Plane();
		this.mInverter = new Matrix3d();
		this.mLocal = new Matrix3d();
	}

	@Override
	public void setControlled(Node node) {
		this.setControlled(node.getTransform());
		this.setInitialWorldMatrix(node.getWorldTransform());
	}
	
	public void setInitialWorldMatrix(Matrix3d initialMatrix){
		this.mInitialWorldMatrix = initialMatrix;
	}

	@Override
	public void init(float x, float y, long time){
		super.init(x, y, time);
		this.mInverter.invert(this.mInitialWorldMatrix);
		this.mLocal.copy(this.mControlled);
	}
	
	@Override
	public void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime) {
		Ray ray = this.mRenderManager.getCamera().createRay(x, y);
		ray.transformBy(this.mInverter);
		this.mPlane.setNormal(ray.getPosition());
		Vector3d intersect = mPlane.rayIntersectsAt(ray);
		if(intersect != null){
			//push the position as a translation onto the node.
			this.mControlled.identity().translate(intersect).multiply(mLocal);
		}
	}

}
