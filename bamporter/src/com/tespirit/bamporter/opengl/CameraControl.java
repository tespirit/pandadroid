package com.tespirit.bamporter.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.PolarRotateController2d;
import com.tespirit.bamboo.controllers.TranslateController1d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.render.Updater;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Model;

public class CameraControl implements MouseListener, MouseMotionListener{
	
	private class FitUpdater implements Updater{
		private Model mModel;
		
		public void setModel(Model model){
			this.mModel = model;
			mRenderManager.addSingleUpdater(this);
		}

		@Override
		public void update() {
			mRenderManager.getCamera().fit(this.mModel);
		}

		@Override
		public void setUpdateManager(UpdateManager updateManager) {
			//VOID
		}
	}
	
	
	PolarRotateController2d mRotate;
	TranslateController2d mTranslate;
	TranslateController1d mZoom;
	RenderManager mRenderManager;
	FitUpdater mFitUpdater;
	public CameraControl(RenderManager renderManager){
		Camera camera = renderManager.getCamera();
		this.mRenderManager = renderManager;
		this.mRotate = new PolarRotateController2d(3);
		this.mRotate.setScale(0.25f);
		this.mRotate.setControlled(camera.getTransform());
		camera.getTransform().PolarRotate(3, 0, 0, 0);
		
		this.mTranslate = new TranslateController2d(Dof3.negativeX, Dof3.Y);
		this.mTranslate.setScale(0.0025f);
		this.mTranslate.setControlled(camera.getTransform());
		
		this.mZoom = new TranslateController1d(Dof3.Z);
		this.mZoom.setScale(0.0025f);
		this.mZoom.setControlled(camera.getTransform());
		
		renderManager.registerUpdater(this.mRotate);
		renderManager.registerUpdater(this.mTranslate);
		renderManager.registerUpdater(this.mZoom);
		
		this.mFitUpdater = new FitUpdater();
		
		this.mIsDrag = false;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// VOID
		
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// VOID
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// VOID
		
	}
	
	private boolean mIsDrag;
	private int mButton;

	@Override
	public void mousePressed(MouseEvent event) {
		if(event.isAltDown()){
			if(this.mIsDrag == false){
				this.mTranslate.begin(event.getX(), event.getY(), event.getWhen());
				this.mRotate.begin(event.getX(), event.getY(), event.getWhen());
				this.mZoom.init((float)event.getY(), event.getWhen());
				this.mIsDrag = true;
			}
			this.mButton = event.getButton();
		} else if (event.isShiftDown() && event.getButton() == MouseEvent.BUTTON1){
			Model node = this.mRenderManager.selectModel((float)event.getX(), (float)event.getY());
			if(node != null){
				this.mFitUpdater.setModel(node);
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
		if(this.mIsDrag){
			this.updateControllers(event);
		}
		this.mIsDrag = false;
	}
	
	private void updateControllers(MouseEvent event){
		switch(this.mButton){
		case MouseEvent.BUTTON1:
			this.mRotate.applyChange((float)event.getX(), (float)event.getY(), event.getWhen());
			break;
		case MouseEvent.BUTTON2:
			this.mZoom.set((float)event.getY(), event.getWhen());
			break;
		case MouseEvent.BUTTON3:
			this.mTranslate.applyChange((float)event.getX(), (float)event.getY(), event.getWhen());
			break;
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if(this.mIsDrag == true){
			this.updateControllers(event);
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		//Void
	}
}
