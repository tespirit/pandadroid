package com.tespirit.panda3d.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.scenegraph.*;

import android.opengl.GLU;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public class Renderer implements android.opengl.GLSurfaceView.Renderer{
	private Node root;
	
	public Renderer(){
		super();
		this.root = null;
	}
	
	public Renderer(Node root){
		super();
		this.root = root;
	}
	
	public void setSceneGraph(Node root){
		this.root = root;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -10);
		if(this.root != null){
			this.traverseSG(this.root, gl);
		}
	}
	
	/**
	 * This is for testing. i'm not sure how i want to handle this.
	 * @param node
	 */
	public void traverseSG(Node node, GL10 gl){
		if(node instanceof Model){
			((Model)node).getGeometry().render(gl);
		} else {
			//push matrix on the stack
			for(int i = 0; i < node.getChildCount(); i++){
				this.traverseSG(node.getChild(i), gl);
			}
			//pop matrix from the stack
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glCullFace(GL10.GL_BACK);
	}

}
