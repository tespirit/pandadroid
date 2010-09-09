package com.tespirit.panda3d.render;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

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
	
	public void render(GL10 gl){
		gl.glMultMatrixf(this.camera.getBuffer(),this.camera.getBufferOffset());
	}
	
	public void setupView(GL10 gl, int width, int height){
		if(height == 0){
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, this.fov, (float) width / (float) height, this.near, this.far);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public Matrix3d getTransform(){
		return this.camera;
	}
	
	public void pan(float x, float y){
		//TODO: implement
		this.camera.translate(x, y, 0.0f);
	}
	
	public void zoom(float z){
		//TODO: implement
		this.camera.translate(0.0f, 0.0f, z);
	}
	
	public void roll(float a){
		//TODO: implement
	}
	
	public void pitch(float a){
		//TODO: implement
	}
	
	public void aim(Matrix3d m){
		//TODO: implement
	}
	
	public void aim(Node node){
		//TODO: implement
	}
}
