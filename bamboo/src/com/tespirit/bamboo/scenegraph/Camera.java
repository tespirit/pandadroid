package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.render.Updater;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class Camera extends Node implements Updater{
	private Matrix3d camera;
	private Matrix3d worldTransform;
	
	private float fov;
	private float near;
	private float far;
	private int width;
	private int height;
	
	/* computed values */
	private float nearHeight;
	private float aspectRatio;
	
	public Camera(){
		this(45.0f);
	}
	
	public Camera(float fov){
		this(fov,0.1f,100.0f);
	}
	
	public Camera(float fov, float near, float far){
		this.fov = fov;
		this.near = near;
		this.far = far;
		
		float[] buffer = Matrix3d.createBuffer(2);
		this.camera = new Matrix3d(buffer);
		this.worldTransform = new Matrix3d(buffer, Matrix3d.SIZE);
	}
	
	public void fit(Node node){
		if(node.getBoundingBox() != null){
			this.lookAt(node.getBoundingBox().getCenter());
			
			//compute the distance
			float distance = node.getBoundingBox().getRadius()*this.near/this.nearHeight;
			this.camera.getTranslation().setZ(-distance);
			
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
		this.camera.lookAt(point);
	}
	
	
	
	@Override
	public Matrix3d getWorldTransform(){
		return this.worldTransform;
	}
	
	@Override
	public void update(Matrix3d transform){
		this.worldTransform.multiply(transform, this.camera).invert();
	}
	
	@Override
	public Matrix3d getTransform(){
		return this.camera;
	}
	
	public float getFov(){
		return this.fov;
	}
	
	public void setFov(float fov){
		this.fov = fov;
		this.markDirty();
	}
	
	public float getNear(){
		return this.near;
	}
	
	public void setNear(float near){
		this.near = near;
		this.markDirty();
	}
	
	public float getFar(){
		return this.far;
	}
	
	public void setFar(float far){
		this.far = far;
		this.markDirty();
	}
	
	/**
	 * This is the height from the center of the camera view
	 * @return
	 */
	public float getNearHeight(){
		return this.nearHeight;
	}
	
	/**
	 * This is the width from the center of the camera view
	 * @return
	 */
	public float getNearWidth(){
		return this.nearHeight * this.aspectRatio;
	}
	
	public float getAspectRatio(){
		return this.aspectRatio;
	}
	
	public void render(){
		Camera.renderer.render(this);
	}
	
	private void markDirty(){
		if(this.getRenderManager() != null){
			this.getRenderManager().addSingleUpdater(this);
		}
	}
	
	@Override
	public void update() {
		//compute near and far heights
		this.nearHeight = (float)(this.near * Math.tan(this.fov/2.0));
		this.aspectRatio = (float)width/(float)height;
		Camera.renderer.setDisplay(this, width, height);
		Camera.renderer.setDisplay(this, this.width, this.height);
	}

	public void setDisplay(int width, int height) {
		this.width = width;
		this.height = height;
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
		this.camera = null;
		this.worldTransform = null;
	}
	
	public Ray createRay(float x, float y){
		float halfHeight = (float)this.height/2;
		float halfWidth = (float)this.width/2;
			
		float xUnit = (halfWidth - x)/halfWidth;
		float yUnit = (y - halfHeight)/halfHeight;
		
		Ray ray = new Ray();
		Matrix3d inverse = new Matrix3d(); 
		ray.setDirection(xUnit*this.nearHeight*this.aspectRatio, 
						 yUnit*this.nearHeight, 
						 this.near);
		ray.setPostion(0.0f, 0.0f, 0.0f);
		inverse.invert(this.worldTransform);
		ray.transformBy(inverse);
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
