package com.tespirit.bamporter.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController1d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;

public class CameraControl implements MouseListener, MouseMotionListener{
	
	RotateController2d mRotate;
	TranslateController2d mTranslate;
	TranslateController1d mZoom;
	
	public CameraControl(RenderManager renderManager){
		Camera camera = renderManager.getCamera();
		this.mRotate = new RotateController2d(Dof3.Y, Dof3.X);
		this.mRotate.setScale(0.25f);
		this.mRotate.setControlled(camera.getPivotTransform());
		
		this.mTranslate = new TranslateController2d(Dof3.negativeX, Dof3.Y);
		this.mTranslate.setScale(0.0025f);
		this.mTranslate.setControlled(camera.getPivotTransform());
		
		this.mZoom = new TranslateController1d(Dof3.Z);
		this.mZoom.setScale(0.0025f);
		this.mZoom.setControlled(camera.getPivotTransform());
		
		renderManager.registerUpdater(this.mRotate);
		renderManager.registerUpdater(this.mTranslate);
		renderManager.registerUpdater(this.mZoom);
		
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
				this.mTranslate.init(event.getX(), event.getY(), event.getWhen());
				this.mRotate.init(event.getX(), event.getY(), event.getWhen());
				this.mZoom.init((float)event.getY(), event.getWhen());
				this.mIsDrag = true;
			}
			this.mButton = event.getButton();
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
			this.mRotate.set((float)event.getX(), (float)event.getY(), event.getWhen());
			break;
		case MouseEvent.BUTTON2:
			this.mZoom.set((float)event.getY(), event.getWhen());
			break;
		case MouseEvent.BUTTON3:
			this.mTranslate.set((float)event.getX(), (float)event.getY(), event.getWhen());
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
