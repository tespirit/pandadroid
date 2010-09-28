package com.tespirit.bamboo.render;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.scenegraph.RenderableNode;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class Camera extends Node implements RenderableNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4808239562403385262L;
	private Matrix3d camera;
	private Matrix3d pivot;
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
		
		float[] buffer = new float[Matrix3d.SIZE*3];
		this.camera = new Matrix3d(buffer);
		this.camera.identity();
		this.pivot = new Matrix3d(buffer, Matrix3d.SIZE);
		this.pivot.identity();
		this.worldTransform = new Matrix3d(buffer, Matrix3d.SIZE *2);
	}
	
	@Override
	public Matrix3d getWorldTransform(){
		return this.worldTransform;
	}
	
	@Override
	public void update(Matrix3d transform){
		this.worldTransform.multiply(transform, this.pivot).multiply(this.camera);
	}
	
	@Override
	public Matrix3d getTransform(){
		return this.camera;
	}
	
	public Matrix3d getPivotTransform(){
		return this.pivot;
	}
	
	public float getFov(){
		return this.fov;
	}
	
	public void setFov(float fov){
		this.fov = fov;
	}
	
	public float getNear(){
		return this.near;
	}
	
	public void setNear(float near){
		this.near = near;
	}
	
	public float getFar(){
		return this.far;
	}
	
	public void setFar(float far){
		this.far = far;
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
	
	public void pan(float x, float y){
		this.camera.translate(this.camera.transform(new Vector3d(x, y, 0.0f)));
	}
	
	public void zoom(float z){
		this.camera.translate(this.camera.transform(new Vector3d(0.0f, 0.0f, z)));
	}
	
	public void roll(float a){
		this.camera.rotateY(a);
	}
	
	public void pitch(float a){
		this.camera.rotateX(a);
	}
	
	public void yawn(float a){
		this.camera.rotateZ(a);
	}
	
	public void aim(Matrix3d m){
		//TODO: implement
	}
	
	public void aim(Node node){
		//TODO: implement
	}
	
	@Override
	public void render(){
		Camera.renderer.render(this);
	}
	
	@Override
	public void setup() {
		//VOID for now		
	}

	@Override
	public void setDisplay(int width, int height) {
		this.width = width;
		this.height = height;
		//compute near and far heights
		this.nearHeight = (float)(this.near * Math.tan(this.fov/2.0));
		this.aspectRatio = (float)width/(float)height;
		Camera.renderer.setDisplay(this, width, height);
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
	
	public Ray createRay(float x, float y){
		float halfHeight = (float)this.height/2;
		float halfWidth = (float)this.width/2;
			
		float xUnit = (halfWidth - x)/halfWidth;
		float yUnit = (y - halfHeight)/halfHeight;
		
		Ray ray = new Ray();
		ray.setDirection(xUnit*this.nearHeight*this.aspectRatio, 
						 yUnit*this.nearHeight, 
						 this.near);
		ray.setPostion(0.0f, 0.0f, 0.0f);
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
}
