package com.tespirit.bamporter.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.tespirit.bamboo.controllers.StandardCameraController2d;
import com.tespirit.bamboo.controllers.StandardCameraController2d.ControlState;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Model;

public class CameraControl implements MouseListener, MouseMotionListener{
	
	StandardCameraController2d mCameraController;
	RenderManager mRenderManager;
	public CameraControl(RenderManager renderManager){
		Camera camera = renderManager.getCamera();
		this.mRenderManager = renderManager;
		this.mCameraController = new StandardCameraController2d();
		this.mCameraController.setScale(0.25f);
		this.mCameraController.setControlled(camera);
		this.mRenderManager.registerUpdater(this.mCameraController);
		this.mCameraController.set(3,0,45);
		
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
		if(event.isShiftDown() && event.getButton() == MouseEvent.BUTTON1){
			Model node = this.mRenderManager.selectModel((float)event.getX(), (float)event.getY());
			if(node != null && node.getBoundingBox() != null){
				this.mCameraController.setCenter(node.getBoundingBox().getCenter());
				float nearHeight = this.mRenderManager.getCamera().getNearHeight();
				float nearPlane = this.mRenderManager.getCamera().getNear();
				float d = node.getBoundingBox().getRadius()*nearPlane/nearHeight;
				this.mCameraController.setDistance(d);
			}
		} else if (event.isShiftDown() && event.getButton() == MouseEvent.BUTTON3){
			Model node = this.mRenderManager.selectModel((float)event.getX(), (float)event.getY());
			if(node != null && node.getBoundingBox() != null){
				this.mCameraController.setCenter(node.getBoundingBox().getCenter());
			}
		} else if(event.isAltDown()){
			if(this.mIsDrag == false){
				this.mCameraController.begin(event.getX(), event.getY(), event.getWhen());
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
			this.mCameraController.setState(ControlState.rotate);
			break;
		case MouseEvent.BUTTON2:
			this.mCameraController.setState(ControlState.zoom);
			break;
		case MouseEvent.BUTTON3:
			this.mCameraController.setState(ControlState.pan);
			break;
		}
		this.mCameraController.applyChange((float)event.getX(), (float)event.getY(), event.getWhen());
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
