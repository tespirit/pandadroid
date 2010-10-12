package com.tespirit.bamboo.physics;

import com.tespirit.bamboo.controllers.MatrixController2d;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public class ForceController2d implements MatrixController2d{
	private Vector3d mForce;
	private float mMass;
	private float mInputMass = 1;
	private ForceGenerator mForceGenerator;
	
	public ForceController2d(ForceGenerator forceGenerator){
		this.mMass = 1;
		this.mForce = new Vector3d();
		this.mForceGenerator = forceGenerator;
	}
	
	private float mX;
	private float mY;
	private long mTime;
	private float mDeltaX;
	private float mDeltaY;
	private long mDeltaTime;
	
	Matrix3d mControlled;
	
	@Override
	public void init(float x, float y, long time){
		this.mDeltaX = x - this.mX;
		this.mDeltaY = y - this.mY;
		this.mDeltaTime = time - this.mTime;
		this.mX = x;
		this.mY = y;
		this.mTime = time;
	}

	@Override 
	public void set(float x, float y, long time){
		this.mDeltaX += x-this.mX;
		this.mDeltaY += y-this.mY;
		this.mDeltaTime += time-this.mTime;
		this.mForce.set(this.mDeltaX, this.mDeltaY, 0);
		this.mForce.computeForce(this.mInputMass, this.mDeltaTime/1000f);
		this.mForceGenerator.apply(this.mControlled, this.mForce, this.mMass);
	}
	
	@Override
	public void update() {
		//VOID
	}
	
	@Override
	public void setControlled(Node node){
		/*if(node.getBoundingBox() != null){
			this.mMass = 100*node.getBoundingBox().getVolumn();
		} else {
			this.mMass = 100;
		}*/
		this.setControlled(node.getTransform());
	}

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		// TODO Auto-generated method stub
		
	}

	public void setControlled(Matrix3d matrix) {
		this.mControlled = matrix;
	}

	@Override
	public void setControlled(String nodeName) {
		Node n = Node.getNode(nodeName);
		if(n != null){
			this.setControlled(n);
		}
	}
}
