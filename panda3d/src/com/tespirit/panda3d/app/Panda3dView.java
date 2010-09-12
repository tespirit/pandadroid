package com.tespirit.panda3d.app;

import com.tespirit.panda3d.core.Assets;
import com.tespirit.panda3d.render.Camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Panda3dView extends GLSurfaceView {
	private com.tespirit.panda3d.render.Renderer renderer;

	public Panda3dView(Context context) {
		super(context);
		Assets.init(context);
		
		//TODO:smartly create a renderer based on the availible graphics api.
		this.initOpenGl1x();
	}
	
	public void initOpenGl1x(){
		com.tespirit.panda3d.opengl1x.Renderer gl1x = new com.tespirit.panda3d.opengl1x.Renderer();
		this.renderer = gl1x;
		this.setRenderer(gl1x);
	}
	
	public com.tespirit.panda3d.render.Renderer getRenderer(){
		return this.renderer;
	}
	
	float prevX;//figure out a better way to do this...
	float prevY;//figure out a better way to do this...
	
	private static final float ROTATE_SCALE = 0.5f;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Camera camera = this.renderer.getCamera();
		float x = event.getX();
		float y = event.getY();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			camera.getPivot().rotateY((x-this.prevX)*Panda3dView.ROTATE_SCALE);
			camera.getPivot().rotateX((y-this.prevY)*Panda3dView.ROTATE_SCALE);
			break;
		default:
			break;
		}
		
		this.prevX = x;
		this.prevY = y;
		
		return true;
	}
}
