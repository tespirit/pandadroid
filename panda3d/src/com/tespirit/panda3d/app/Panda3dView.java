package com.tespirit.panda3d.app;

import java.util.ArrayList;

import com.tespirit.panda3d.controllers.Controller2d;
import com.tespirit.panda3d.controllers.Dof3;
import com.tespirit.panda3d.controllers.RotateController2d;
import com.tespirit.panda3d.controllers.TranslateController2d;
import com.tespirit.panda3d.core.Assets;
import com.tespirit.panda3d.render.Camera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Panda3dView extends GLSurfaceView {
	private com.tespirit.panda3d.render.Renderer renderer;
	private ArrayList<Controller2d> touchUpControllers;
	private ArrayList<Controller2d> touchMoveControllers;
	private ArrayList<Controller2d> touchDownControllers;

	public Panda3dView(Context context) {
		super(context);
		Assets.init(context);
		
		//TODO:smartly create a renderer based on the availible graphics api.
		this.initOpenGl1x();
		
		this.touchUpControllers = new ArrayList<Controller2d>();
		this.touchMoveControllers = new ArrayList<Controller2d>();
		this.touchDownControllers = new ArrayList<Controller2d>();
	}
	
	public void initOpenGl1x(){
		com.tespirit.panda3d.opengl1x.Renderer gl1x = new com.tespirit.panda3d.opengl1x.Renderer();
		this.renderer = gl1x;
		this.setRenderer(gl1x);
	}
	
	public com.tespirit.panda3d.render.Renderer getRenderer(){
		return this.renderer;
	}
	
	public void addTouchUpController(Controller2d m){
		this.touchUpControllers.add(m);
	}
	
	public void addTouchDownController(Controller2d m){
		this.touchDownControllers.add(m);
	}
	
	public void addTouchMoveController(Controller2d m){
		this.touchMoveControllers.add(m);
	}
	
	float prevX;//figure out a better way to do this...
	float prevY;//figure out a better way to do this...
	long prevTime;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		long time = event.getEventTime();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			for(Controller2d m : this.touchUpControllers){
				m.update(x-this.prevX, y-this.prevY, time-this.prevTime);
			}
			break;
		case MotionEvent.ACTION_DOWN:
			for(Controller2d m : this.touchDownControllers){
				m.update(x, y);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			for(Controller2d m : this.touchMoveControllers){
				m.update(x-this.prevX, y-this.prevY, time-this.prevTime);
			}
			break;
		default:
			break;
		}
		
		this.prevX = x;
		this.prevY = y;
		this.prevTime = time;
		
		return true;
	}
	
	//some autogeneration functions
	public Camera createTouchRotateCamera(){
		return this.createTouchRotateCamera(0.0f);
	}
	
	public Camera createTouchRotateCamera(float zoomDistance){
		Camera camera = new Camera();
		camera.zoom(zoomDistance);
		this.renderer.setCamera(camera);
		
		RotateController2d m = new RotateController2d(Dof3.Y, Dof3.X);
		m.setControlled(camera.getPivotTransform());
		m.setScale(0.25f);
		this.addTouchMoveController(m);
		
		return camera;
	}
	
	public Camera createTouchPanCamera(){
		return this.createTouchPanCamera(0.0f);
	}
	
	public Camera createTouchPanCamera(float zoomDistance){
		Camera camera = new Camera();
		camera.zoom(zoomDistance);
		this.renderer.setCamera(camera);
		
		TranslateController2d m = new TranslateController2d(Dof3.X, Dof3.negativeY);
		m.setControlled(camera);
		m.setScale(0.01f);
		this.addTouchMoveController(m);
		
		return camera;
	}
}
