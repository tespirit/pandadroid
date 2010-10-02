package com.tespirit.bamporter.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.tespirit.bamboo.controllers.Dof3;
import com.tespirit.bamboo.controllers.RotateController2d;
import com.tespirit.bamboo.controllers.TranslateController1d;
import com.tespirit.bamboo.controllers.TranslateController2d;
import com.tespirit.bamboo.render.Camera;

public class CameraControl implements MouseListener, MouseMotionListener{
	
	RotateController2d mRotate;
	TranslateController2d mTranslate;
	TranslateController1d mZoom;
	
	public CameraControl(Camera camera){
		this.mRotate = new RotateController2d(Dof3.Y, Dof3.X);
		this.mRotate.setScale(0.25f);
		this.mRotate.setControlled(camera.getPivotTransform());
		
		this.mTranslate = new TranslateController2d(Dof3.negativeX, Dof3.Y);
		this.mTranslate.setScale(0.0025f);
		this.mTranslate.setControlled(camera.getPivotTransform());
		
		this.mZoom = new TranslateController1d(Dof3.Z);
		this.mZoom.setScale(0.0025f);
		this.mZoom.setControlled(camera.getPivotTransform());
		
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
	
	private float mPrevX;
	private float mPrevY;
	private boolean mIsDrag;
	private int mButton;

	@Override
	public void mousePressed(MouseEvent event) {
		if(event.isAltDown()){
			if(this.mIsDrag == false){
				this.mPrevX = event.getX();
				this.mPrevY = event.getY();
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
		float deltaX = event.getX() - this.mPrevX;
		float deltaY = event.getY() - this.mPrevY;
		switch(this.mButton){
		case MouseEvent.BUTTON1:
			this.mTranslate.update(deltaX, deltaY);
			break;
		case MouseEvent.BUTTON2:
			this.mZoom.update(deltaY);
			break;
		case MouseEvent.BUTTON3:
			this.mRotate.update(deltaX, deltaY);
			break;
		}
		this.mPrevX = event.getX();
		this.mPrevY = event.getY();
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
