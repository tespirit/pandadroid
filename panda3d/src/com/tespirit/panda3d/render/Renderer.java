package com.tespirit.panda3d.render;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.scenegraph.*;
import com.tespirit.panda3d.vectors.Matrix3d;

import android.opengl.GLU;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public class Renderer implements android.opengl.GLSurfaceView.Renderer{
	private Node root;
	private ArrayList<Light> lights;
	
	public Renderer(){
		super();
		this.root = null;
		lights = new ArrayList<Light>();
		lights.add(new Light());
	}
	
	public Renderer(Node root){
		super();
		this.root = root;
		lights = new ArrayList<Light>();
	}
	
	public void setSceneGraph(Node root){
		this.root = root;
	}
	
	public void addLight(Light light){
		lights.add(light);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		//render lights!
		this.applyLights(gl);
		
		gl.glTranslatef(0, 0, -10);
		if(this.root != null){
			this.traverseSG(this.root, gl);
		}
	}
	
	public void applyLights(GL10 gl){
		Iterator<Light> itr = this.lights.iterator();
		while(itr.hasNext()){
			itr.next().render(gl);
		}
	}
	
	/**
	 * This is for testing. i'm not sure how i want to handle this.
	 * @param node
	 */
	public void traverseSG(Node node, GL10 gl){
		gl.glPushMatrix();
		//load matrix!
		Matrix3d m = node.getTransform();
		if(m != null){
			gl.glMultMatrixf(m.getBuffer(), m.getBufferOffset());
		}
		if(node instanceof Model){
			((Model)node).getMaterial().apply(gl);
			((Model)node).getGeometry().render(gl);
		} else {
			for(int i = 0; i < node.getChildCount(); i++){
				this.traverseSG(node.getChild(i), gl);
			}
		}
		gl.glPopMatrix();
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
		
		gl.glEnable (GL10.GL_LIGHTING);
		
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		for(int i = 0; i < this.lights.size(); i++){
			this.lights.get(i).init(gl, i);
		}
	}

}
