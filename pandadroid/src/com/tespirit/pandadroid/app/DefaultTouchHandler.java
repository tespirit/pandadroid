package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.controllers.ControllerDummy;
import com.tespirit.bamboo.controllers.MatrixController2d;
import com.tespirit.bamboo.controllers.MoveController2d;
import com.tespirit.bamboo.controllers.MoveFlingController2d;
import com.tespirit.bamboo.controllers.StandardCameraController2d;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * This is a basic OnTouchListener that switches between two controllers based on if an object in
 * the screen is selected or not.
 * @author DennisMcAutoknight
 *
 */
public class DefaultTouchHandler implements OnTouchListener{
	private Controller2d mCurrent;
	private Controller2d mDefault;
	private MatrixController2d mSelected;
	private Node mNode;

	protected RenderManager mRenderManager;
	
	/**
	 * This will automatically register this listener to the pandadroid view.
	 * @param view
	 */
	public DefaultTouchHandler(PandadroidView view){
		this(view.getRenderer());
		view.setOnTouchListener(this);
	}
	
	public DefaultTouchHandler(RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mCurrent = ControllerDummy.getInstance();
		this.mDefault = ControllerDummy.getInstance();
		this.mSelected = null;
	}
	
	public void setDefaultController(Controller2d unselectedController){
		this.mDefault = unselectedController;
		this.mDefault.setUpdateManager(this.mRenderManager);
	}
	
	public void setSelectedController(MatrixController2d selectedController){
		this.mSelected = selectedController;
		this.mSelected.setUpdateManager(this.mRenderManager);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		long time = event.getEventTime();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			this.mCurrent.end();
			this.mCurrent = this.mDefault;
			break;
		case MotionEvent.ACTION_DOWN:
			this.mNode = this.mRenderManager.selectModel(x, y);
			if(this.mNode != null){
				if(this.mSelected != null){
					this.mSelected.setControlled(this.mNode);
					this.mCurrent = this.mSelected;
				}
			} else {
				this.mCurrent = this.mDefault;
			}
			this.mCurrent.begin(x, y, time);
			break;
		case MotionEvent.ACTION_MOVE:
			this.mCurrent.applyChange(x, y, time);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		default:
			break;
		}
		
		return true;
	}
	
	public StandardCameraController2d makeCameraControllable(){
		Camera camera = this.mRenderManager.getCamera();
		this.mRenderManager.setCamera(camera);
		StandardCameraController2d ctrl = new StandardCameraController2d();
		ctrl.setControlled(camera);
		ctrl.setScale(0.25f);
		this.setDefaultController(ctrl);
		return ctrl;
	}

	/**
	 * This conveniently sets a move controller as the select controller.
	 * this will no longer work if you set a new select controller.
	 * @param drag
	 */
	public MoveController2d makeNodesDraggable(){
		MoveController2d mc = new MoveController2d(this.mRenderManager);
		this.setSelectedController(mc);
		return mc;
	}
	
	public MoveFlingController2d makeNodesFlingable(){
		MoveFlingController2d mfc = new MoveFlingController2d(this.mRenderManager);
		this.setSelectedController(mfc);
		return mfc;
	}
	
}
