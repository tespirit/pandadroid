package com.tespirit.pandadroid.app;

import java.util.ArrayList;
import java.util.List;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.controllers.AccelerationController3d;
import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.controllers.ControllerDummy;
import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.EulerController3d;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.creation.Lights;
import com.tespirit.bamboo.io.BambooAsset;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.pandadroid.R;
import com.tespirit.pandadroid.debug.Debug;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class PandadroidView extends GLSurfaceView {
	private com.tespirit.bamboo.render.RenderManager renderer;
	private Controller2d touchUpController;
	private Controller2d touchMoveController;
	private Controller2d touchDownController;
	private boolean debug;
	private Context context;

	public PandadroidView(Context context){
		super(context);
		this.debug = false;
		this.init(context);
	}
	
	public PandadroidView(Context context, boolean debug){
		super(context);
		this.debug = debug;
		this.init(context);
	}
	
	public PandadroidView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		//setup custom attributes
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PandadroidView);
		this.debug = a.getBoolean(R.styleable.PandadroidView_debug, false);
		int color = a.getColor(R.styleable.PandadroidView_background_color, 0x000000FF);
		
		Color4 bgColor = new Color4();
		bgColor.set(Color.red(color), 
					Color.green(color), 
					Color.blue(color),
					Color.alpha(color));
		a.recycle();
		
		this.init(context);
		this.renderer.setBackgroundColor(bgColor);
	}
	
	private void init(Context context){
		Assets.init(context);
		this.context = context;
		
		//TODO:smartly create a renderer based on the availible graphics api.
		this.initOpenGl1x();
		
		this.touchUpController = ControllerDummy.getInstance();
		this.touchMoveController = ControllerDummy.getInstance();
		this.touchDownController = ControllerDummy.getInstance();
	}
	
	private void initOpenGl1x(){
		com.tespirit.pandadroid.opengl1x.Renderer gl1x;
		if(this.debug){
			gl1x = Debug.init(this);
		} else {
			gl1x = new com.tespirit.pandadroid.opengl1x.Renderer();
		}
		this.renderer = gl1x;
		this.setRenderer(gl1x);
	}
	
	public com.tespirit.bamboo.render.RenderManager getRenderer(){
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
		this.requestFocus();
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
	
	/**
	 * 
	 * @return null if the device does not support motion sensors.
	 */
	public Camera createMotionSensorCamera(){
		return this.createMotionSensorCamera(0.0f);
	}
	
	/**
	 * 
	 * @param zoomDistance
	 * @return null if the device does not support motion sensors.
	 */
	public Camera createMotionSensorCamera(float zoomDistance){
		SensorManager s = (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometer = s.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
		Sensor geoMagnetic = s.getDefaultSensor(SensorManager.SENSOR_MAGNETIC_FIELD);
		if(accelerometer == null || geoMagnetic == null){
			return null;
		}
		
		Camera camera = new Camera();
		camera.zoom(zoomDistance);
		
		EulerController3d e = new EulerController3d(EulerController3d.Euler.ZXY);
		e.setControlled(camera);
		
		AccelerationController3d a = new AccelerationController3d();
		a.setControlled(camera);
		
		MotionSensor3d motionSensor = new MotionSensor3d(e, a);
		
		//register the motion sensor!		
		if(!s.registerListener(motionSensor, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)){
			return null;
		}
		if(!s.registerListener(motionSensor, geoMagnetic, SensorManager.SENSOR_DELAY_GAME)){
			return null;
		}
		this.renderer.setCamera(camera);
		return camera;
	}
	
	public Camera getCamera(){
		return this.renderer.getCamera();
	}
	
	public void setCamera(Camera camera){
		this.renderer.setCamera(camera);
	}
	
	public Player addAnimation(Animation animation){
		Player player = new Player();
		player.setAnimation(animation);
		this.renderer.addTimeUpdate(player);
		return player;
	}
	
	public void addSceneNode(Node node){
		this.renderer.addNode(node);
	}
	
	/**
	 * This assumes there is 1 animation to load.
	 * @param bamboo
	 * @return
	 */
	public Player addBambooSingleAnimation(BambooAsset bamboo){
		this.renderer.addNode(bamboo.getRootSceneNodes());
		return this.addAnimation(bamboo.getAnimations().get(0));
	}
	
	/**
	 * This will conveniently return an array of players linked up to any animations
	 * that were imported.
	 * @param bamboo
	 * @return
	 */
	public List<Player> addBamboo(BambooAsset bamboo){
		ArrayList<Player> players = new ArrayList<Player>(bamboo.getAnimations().size());
		for(Animation a : bamboo.getAnimations()){
			players.add(this.addAnimation(a));
		}
		return players;
	}
	
	public void createDefaultLight(){
		Lights.addDefaultLight(this.renderer);
	}
}
