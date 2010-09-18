package com.tespirit.panda3d.app;

import com.tespirit.panda3d.controllers.Controller2d;
import com.tespirit.panda3d.controllers.ControllerDummy;
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
	private Controller2d touchUpController;
	private Controller2d touchMoveController;
	private Controller2d touchDownController;

	public Panda3dView(Context context){
		this(context, false);
	}
	
	public Panda3dView(Context context, boolean debugMode) {
		super(context);
		Assets.init(context);
		
		//TODO:smartly create a renderer based on the availible graphics api.
		this.initOpenGl1x(debugMode);
		
		this.touchUpController = ControllerDummy.getInstance();
		this.touchMoveController = ControllerDummy.getInstance();
		this.touchDownController = ControllerDummy.getInstance();
	}
	
	private void initOpenGl1x(boolean debugMode){
		com.tespirit.panda3d.opengl1x.Renderer gl1x;
		if(debugMode){
			gl1x = new com.tespirit.panda3d.opengl1x.Renderer.Debug();
		} else {
			gl1x = new com.tespirit.panda3d.opengl1x.Renderer();
		}
		this.renderer = gl1x;
		this.setRenderer(gl1x);
	}
	
	public com.tespirit.panda3d.render.Renderer getRenderer(){
		return this.renderer;
	}
	
	/**
	 * The controller is passed in the total distance travel and total time from
	 * when the first touchDown event is called.
	 * @param m
	 */
	public void setTouchUpController(Controller2d m){
		this.touchUpController = m;
	}
	
	/**
	 * The controller is passed in the absolute position of the press (typically
	 * you'll have to make a custom controller for this to behave reasonably)
	 * @param m
	 */
	public void setTouchDownController(Controller2d m){
		this.touchDownController = m;
	}
	
	/**
	 * This controller is passed in the change in position and time from the last
	 * touch event.
	 * @param m
	 */
	public void setTouchMoveController(Controller2d m){
		this.touchMoveController = m;
	}
	
	public Controller2d getTouchUpController(){
		return this.touchUpController;
	}
	
	public Controller2d getTouchDownController(){
		return this.touchDownController;
	}
	
	public Controller2d getTouchMoveController(){
		return this.touchMoveController;
	}
	
	float startX;
	float startY;
	long startTime;
	float prevX;
	float prevY;
	long prevTime;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		long time = event.getEventTime();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			this.touchUpController.update(x-this.startX, y-this.startY, time-this.startTime);
			break;
		case MotionEvent.ACTION_DOWN:
			this.startX = x;
			this.startY = y;
			this.startTime = time;
			this.touchDownController.update(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			this.touchMoveController.update(x-this.prevX, y-this.prevY, time-this.prevTime);
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
		this.setTouchMoveController(m);
		
		return camera;
	}
	
	public Camera createTouchPanCamera(){
		return this.createTouchPanCamera(0.0f);
	}
	
	public Camera createTouchPanCamera(float zoomDistance){
		Camera camera = new Camera();
		camera.zoom(zoomDistance);
		this.renderer.setCamera(camera);
		
		TranslateController2d m = new TranslateController2d(Dof3.negativeX, Dof3.Y);
		m.setControlled(camera);
		m.setScale(0.01f);
		this.setTouchMoveController(m);
		
		return camera;
	}
}
