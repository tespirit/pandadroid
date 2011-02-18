package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.render.Updater;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class Camera extends Node implements Updater{
	private Matrix3d mTransform;
	private Matrix3d mWorldTransform;
	private Matrix3d mWorldTransformInv;
	private Matrix3d mProjection;
	
	private float mFov;
	private float mNear;
	private float mFar;
	private int mWidth;
	private int mHeight;
	
	/* computed values */
	private float mNearHeight;
	private float mAspectRatio;
	
	public Camera(){
		this(45.0f);
	}
	
	public Camera(float fov){
		this(fov,0.1f,100.0f);
	}
	
	public Camera(float fov, float near, float far){
		this.mFov = fov;
		this.mNear = near;
		this.mFar = far;
		
		float[] buffer = Matrix3d.createBuffer(4);
		this.mTransform = new Matrix3d(buffer);
		this.mWorldTransform = new Matrix3d(buffer, Matrix3d.SIZE);
		this.mWorldTransformInv = new Matrix3d(buffer, Matrix3d.SIZE*2);
		this.mProjection = new Matrix3d(buffer, Matrix3d.SIZE*3);
	}
	
	public void fit(Node node){
		if(node.getBoundingBox() != null){
			this.lookAt(node.getBoundingBox().getCenter());
			
			//compute the distance
			float distance = node.getBoundingBox().getRadius()*this.mNear/this.mNearHeight;
			this.mTransform.getTranslation().setZ(-distance);
			
		} else {
			this.lookAt(node); //at least center to the world transform
		}
	}
	
	public void lookAt(Node node){
		if(node.getBoundingBox() != null){
			this.lookAt(node.getBoundingBox().getCenter());
		} else if (node.getWorldTransform() != null){
			this.lookAt(node.getWorldTransform().getTranslation());
		}
	}
	
	public void lookAt(Vector3d point){
		this.mTransform.lookAt(point);
	}
	
	
	
	@Override
	public Matrix3d getWorldTransform(){
		return this.mWorldTransformInv;
	}
	
	public Matrix3d getWorldTransformNoInverse(){
		return this.mWorldTransform;
	}
	
	public Matrix3d getProjection(){
		return this.mProjection;
	}
	
	@Override
	public void update(Matrix3d transform){
		this.mWorldTransform.multiply(transform, this.mTransform);
		this.mWorldTransformInv.invert(this.mWorldTransform);
	}
	
	@Override
	public Matrix3d getTransform(){
		return this.mTransform;
	}
	
	public float getFov(){
		return this.mFov;
	}
	
	public void setFov(float fov){
		this.mFov = fov;
		this.markDirty();
	}
	
	public float getNear(){
		return this.mNear;
	}
	
	public void setNear(float near){
		this.mNear = near;
		this.markDirty();
	}
	
	public float getFar(){
		return this.mFar;
	}
	
	public void setFar(float far){
		this.mFar = far;
		this.markDirty();
	}
	
	/**
	 * This is the height from the center of the camera view
	 * @return
	 */
	public float getNearHeight(){
		return this.mNearHeight;
	}
	
	/**
	 * This is the width from the center of the camera view
	 * @return
	 */
	public float getNearWidth(){
		return this.mNearHeight * this.mAspectRatio;
	}
	
	public float getAspectRatio(){
		return this.mAspectRatio;
	}
	
	public void render(){
		Camera.renderer.render(this);
	}
	
	//depricated
	//TODO: always set the projection matrix each render cycle using the getProjection method.
	private void markDirty(){
		if(this.getRenderManager() != null){
			this.getRenderManager().addSingleUpdater(this);
		}
	}
	
	@Override
	public void update() {
		
		//compute near and far heights
		this.mNearHeight = (float)(this.mNear * Math.tan(this.mFov/2.0));
		this.mAspectRatio = (float)mWidth/(float)mHeight;

		this.mProjection.makeProjection(this.mNearHeight*this.mAspectRatio, this.mNearHeight, this.mNear, this.mFar);
		Camera.renderer.setDisplay(this, this.mWidth, this.mHeight);

	}

	public void setDisplay(int width, int height) {
		this.mWidth = width;
		this.mHeight = height;
		this.update();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	public Node getChild(int i) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}
	
	@Override
	protected void recycleInternal(){
		this.mTransform = null;
		this.mWorldTransform = null;
		this.mWorldTransformInv = null;
	}
	
	public Ray createRay(float x, float y){
		float halfHeight = (float)this.mHeight/2;
		float halfWidth = (float)this.mWidth/2;
			
		float xUnit = (halfWidth - x)/halfWidth;
		float yUnit = (y - halfHeight)/halfHeight;
		
		Ray ray = new Ray();
		ray.setDirection(xUnit*this.mNearHeight*this.mAspectRatio, 
						 yUnit*this.mNearHeight, 
						 this.mNear);
		ray.setPostion(0.0f, 0.0f, 0.0f);
		ray.transformBy(this.mWorldTransform);
		return ray;
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Camera.renderer = this;
		}
		
		public abstract void render(Camera camera);
		public abstract void setDisplay(Camera camera, int width, int height);
	}

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		//VOID this is handled with the fact that a node gets full renderManager access.
	}
}
