package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller2d;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Plane;
import com.tespirit.bamboo.vectors.Ray;
import com.tespirit.bamboo.vectors.Vector3d;

public class TranslateAbsolute extends SelectController2d{
	Controller2d previousController;
	MoveController moveController;
	
	private PandadroidView view;
	private Node node;
	private Plane plane;
	private Matrix3d inverter;
	private Matrix3d local;
	
	float totalX;
	float totalY;
	
	public TranslateAbsolute(PandadroidView view){
		super(view);
		this.view = view;
		this.plane = new Plane();
		this.inverter = new Matrix3d();
		this.moveController = new MoveController();
		this.local = new Matrix3d();
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
			this.node = model;
			this.inverter.invert(model.getWorldTransform());
			this.local.copy(model.getTransform());
			this.totalX = this.x;
			this.totalY = this.y;
			if(this.previousController == null){
				this.previousController = this.view.getTouchMoveController();
				this.view.setTouchMoveController(this.moveController);
			}
		}
	}
	
	
	private class MoveController implements Controller2d{
		@Override
		public void update(float x, float y) {
			totalX += x;
			totalY += y;
			Ray ray = view.getRenderer().getCamera().createRay(totalX, totalY);
			ray.transformBy(inverter);
			plane.setNormal(ray.getPosition());
			Vector3d intersect = plane.rayIntersectsAt(ray);
			if(intersect != null){
				//push the position as a translation onto the node.
				node.getTransform().identity().translate(intersect).multiply(local);
			}
		}

		@Override
		public void update(float x, float y, long time) {
			this.update(x, y);
			
		}
	}
	
}
