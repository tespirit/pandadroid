package com.tespirit.panda3d.opengl1x;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.primatives.LineIndices;
import com.tespirit.panda3d.primatives.LineList;
import com.tespirit.panda3d.primatives.Primative;
import com.tespirit.panda3d.primatives.TriangleFan;
import com.tespirit.panda3d.primatives.TriangleIndices;
import com.tespirit.panda3d.primatives.TriangleList;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.render.Light;
import com.tespirit.panda3d.surfaces.Material;
import com.tespirit.panda3d.surfaces.Surface;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.vectors.Matrix3d;

import android.content.Context;

public class Renderer extends com.tespirit.panda3d.render.Renderer implements android.opengl.GLSurfaceView.Renderer{

	public Renderer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableLights() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableTextures() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popMatrix() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushMatrix(Matrix3d transform) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Light light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Camera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Material material) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Texture texture) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Surface surface) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(TriangleIndices triangles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(TriangleFan triangles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(TriangleList triangles) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(LineIndices lines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(LineList lines) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Primative primative) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup(Light light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup(Camera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setup(Texture texture) {
		// TODO Auto-generated method stub
		
	}

}
