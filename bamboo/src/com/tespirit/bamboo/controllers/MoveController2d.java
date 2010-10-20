package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Plane;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class MoveController2d extends BaseMatrixController2d{
	private RenderManager mRenderManager;
	private Plane mPlane;
	private Matrix3d mInitialWorldMatrix;
	private Matrix3d mInverter;
	private Matrix3d mLocal;
	protected Vector3d mVelocity;
	private Vector3d mPrevPosition;
	
	public MoveController2d(RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mPlane = new Plane();
		this.mInverter = new Matrix3d();
		this.mLocal = new Matrix3d();
		this.mVelocity = new Vector3d();
		this.mPrevPosition = new Vector3d();
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
	public void begin(float x, float y, long time){
		super.begin(x, y, time);
		this.mInverter.invert(this.mInitialWorldMatrix);
		this.mLocal.copy(this.mControlled);
	}
	
	public Vector3d getVelocity(){
		return this.mVelocity;
	}
	
	public Matrix3d getControlled(){
		return this.mControlled;
	}
	
	@Override
	public void update(float x, float y, float deltaX, float deltaY, long time, long deltaTime) {
		Camera camera = this.mRenderManager.getCamera();
		Ray ray = camera.createRay(x, y);
		//Fast invert!
		Vector3d planeNormal = new Vector3d(camera.getWorldTransform().getXAxis().getZ(),
										    camera.getWorldTransform().getYAxis().getZ(),
										    camera.getWorldTransform().getZAxis().getZ());
		ray.transformBy(this.mInverter);
		this.mPlane.setNormal(planeNormal);
		Vector3d intersect = mPlane.rayIntersectsAt(ray);
		if(intersect != null){
			//push the position as a translation onto the node.
			this.mPrevPosition.copy(this.mControlled.getTranslation());
			this.mControlled.identity().translate(intersect).multiply(mLocal);
			this.mVelocity.sub(this.mControlled.getTranslation(), this.mPrevPosition);
			this.mVelocity.scale(1000f/deltaTime);
		}
	}

}
