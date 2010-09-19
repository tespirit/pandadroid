package com.tespirit.panda3d.app;

import com.tespirit.panda3d.controllers.Controller2d;
import com.tespirit.panda3d.scenegraph.Model;
import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Plane;
import com.tespirit.panda3d.vectors.Ray;
import com.tespirit.panda3d.vectors.Vector3d;

public class TranslateAbsolute implements Controller2d{
	private Panda3dView view;
	private Node node;
	private Plane plane;
	private Matrix3d inverter;
	
	private class Select extends SelectController2d{
		Controller2d previousController;

		public Select() {
			super(TranslateAbsolute.this.view);
		}

		@Override
		public void deselect() {
			if(this.previousController != null){
				view.setTouchMoveController(this.previousController);
				this.previousController = null;
			}
		}

		@Override
		public void select(Model model) {
			if(model == null){
				this.deselect();
			} else {
				node = model;
				if(this.previousController == null){
					this.previousController = this.view.getTouchMoveController();
					this.view.setTouchMoveController(TranslateAbsolute.this);
				}
			}
		}
		
	}
	
	public TranslateAbsolute(Panda3dView view){
		super();
		this.view = view;
		this.plane = new Plane();
		this.inverter = new Matrix3d();
	}

	@Override
	public void update(float x, float y) {
		Ray ray = this.view.getRenderer().getCamera().createRay(x, y);
		this.inverter.invert(this.node.getWorldTransform());
		ray.transformBy(this.inverter);
		Vector3d intersect = this.plane.rayIntersectsAt(ray);
		if(intersect != null){
			node.getTransform().translate(intersect);
		}
	}

	@Override
	public void update(float x, float y, long time) {
		this.update(x, y);
		
	}
	
	public void setController(Node node){
		this.node = node;
	}
	
	public Select createSelectController(){
		return new Select();
	}

}
