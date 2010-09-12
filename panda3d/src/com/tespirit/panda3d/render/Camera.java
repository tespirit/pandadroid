package com.tespirit.panda3d.render;

import com.tespirit.panda3d.core.ComponentRenderer;
import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

public class Camera {
	private Matrix3d camera;
	
	private float fov;
	private float near;
	private float far;
	
	public Camera(){
		this.fov = 45.0f;
		this.near = 0.1f;
		this.far = 100.0f;
		this.camera = new Matrix3d();
	}
	
	public Camera(float fov){
		this.fov = fov;
		this.near = 0.1f;
		this.far = 100.0f;
		this.camera = new Matrix3d();
	}
	
	public Camera(float fov, float near, float far){
		this.fov = fov;
		this.near = near;
		this.far = far;
		this.camera = new Matrix3d();
	}
	
	public Matrix3d getTransform(){
		return this.camera;
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
	
	public void pan(float x, float y){
		this.camera.translate(this.camera.transform(new Vector3d(x, y, 0.0f)));
	}
	
	public void zoom(float z){
		this.camera.translate(this.camera.transform(new Vector3d(0.0f, 0.0f, z)));
	}
	
	public void roll(float a){
		this.camera.rotateAxis(a, 
							   this.camera.getValue(2, 0), 
							   this.camera.getValue(2, 1), 
							   this.camera.getValue(2, 2));
	}
	
	public void pitch(float a){
		this.camera.rotateAxis(a, 
							   this.camera.getValue(0, 0), 
							   this.camera.getValue(0, 1), 
							   this.camera.getValue(0, 2));
	}
	
	public void yawn(float a){
		this.camera.rotateAxis(a, 
							   this.camera.getValue(1, 0), 
							   this.camera.getValue(1, 1), 
							   this.camera.getValue(1, 2));
	}
	
	public void aim(Matrix3d m){
		//TODO: implement
	}
	
	public void aim(Node node){
		//TODO: implement
	}
	
	public void render(){
		Camera.renderer.render(this);
	}
	
	public void setup(int width, int height){
		Camera.renderer.setup(this, width, height);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Camera.renderer = this;
		}
		
		public abstract void render(Camera camera);
		public abstract void setup(Camera camera, int width, int height);
	}
}
