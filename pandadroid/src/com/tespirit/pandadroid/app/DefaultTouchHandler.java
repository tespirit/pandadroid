package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.controllers.ControllerDummy;
import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.MatrixController2d;
import com.tespirit.bamboo.controllers.MoveController2d;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.physics.ForceController2d;
import com.tespirit.bamboo.physics.MoveForce;
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
	private Controller2d mRelease;
	private Controller2d mDefault;
	private MatrixController2d mSelected;
	private MatrixController2d mReleaseSelected;

	RenderManager mRenderManager;
	
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
		this.mRelease = ControllerDummy.getInstance();
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
	
	public void setReleaseSelectedController(MatrixController2d releaseSelectedController){
		this.mReleaseSelected = releaseSelectedController;
		this.mReleaseSelected.setUpdateManager(this.mRenderManager);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		long time = event.getEventTime();
		
		switch(event.getAction()){
		case MotionEvent.ACTION_UP:
			this.mCurrent.set(x, y, time);
			this.mCurrent = this.mDefault;
			this.mRelease.set(x, y, time);
			this.mRelease = ControllerDummy.getInstance();
			break;
		case MotionEvent.ACTION_DOWN:
			Node selected = this.mRenderManager.selectModel(x, y);
			if(selected != null){
				if(this.mSelected != null){
					this.mSelected.setControlled(selected);
					this.mCurrent = this.mSelected;
				}
				if(this.mReleaseSelected != null){
					this.mReleaseSelected.setControlled(selected);
					this.mRelease = this.mReleaseSelected;
				}
			} else {
				this.mCurrent = this.mDefault;
			}
			this.mCurrent.init(x, y, time);
			break;
		case MotionEvent.ACTION_MOVE:
			this.mCurrent.set(x, y, time);
			this.mRelease.init(x, y, time);
			break;
		default:
			break;
		}
		
		return true;
	}
	
	public void makeCameraPannable(){
		Camera camera = this.mRenderManager.getCamera();
		this.mRenderManager.setCamera(camera);

		TranslateController2d ctrl = new TranslateController2d(Dof3.negativeX, Dof3.Y);
		ctrl.setControlled(camera.getPivotTransform());
		ctrl.setScale(0.01f);
		this.setDefaultController(ctrl);
	}
	
	public void makeCameraRotatable(){
		Camera camera = this.mRenderManager.getCamera();
		this.mRenderManager.setCamera(camera);
		
		RotateController2d ctrl = new RotateController2d(Dof3.Y, Dof3.X);
		ctrl.setControlled(camera.getPivotTransform());
		ctrl.setScale(0.25f);
		this.setDefaultController(ctrl);
	}

	/**
	 * This conveniently sets a move controller as the select controller.
	 * this will no longer work if you set a new select controller.
	 * @param drag
	 */
	public void makeNodesDraggable(){
		this.setSelectedController(new MoveController2d(this.mRenderManager));
	}
	
	public void makeNodesFlingable(){
		this.makeNodesDraggable();
		this.setReleaseSelectedController(new ForceController2d(new MoveForce(this.mRenderManager)));
	}
	
}
