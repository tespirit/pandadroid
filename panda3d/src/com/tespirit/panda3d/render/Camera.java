package com.tespirit.panda3d.render;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;

public class Camera {
	private Matrix3d camera;
	private Matrix3d pivot;
	
	private float fov;
	private float near;
	private float far;
	
	public Camera(){
		this(45.0f,0.1f,100.0f);
	}
	
	public Camera(float fov){
		this(fov,0.1f,100.0f);
	}
	
	public Camera(float fov, float near, float far){
		this.fov = fov;
		this.near = near;
		this.far = far;
		this.camera = new Matrix3d();
		this.pivot = new Matrix3d();
	}
	
	public Matrix3d getTransform(){
		return this.camera;
	}
	
	public Matrix3d getPivot(){
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
