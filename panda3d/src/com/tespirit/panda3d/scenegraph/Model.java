package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.surfaces.Surface;
import com.tespirit.panda3d.vectors.*;
import com.tespirit.panda3d.primitives.Primitive;

public class Model extends Node implements RenderableNode{
	private Matrix3d transform;
	private Primitive primitive;
	private Surface surface;
	
	private AxisAlignedBox boundingBox;
	
	public Model(){
		this.transform = new Matrix3d(); //optimize later to share a single matrix buffer.
		this.surface = Surface.getDefaultSurface();
		this.boundingBox = new AxisAlignedBox();
	}
	
	public Model(String name){
		super(name);
		this.transform = new Matrix3d(); //optimize later to share a single matrix buffer.
		this.surface = Surface.getDefaultSurface();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.boundingBox;
	}

	/**
	 * Models cannot have children.
	 */
	@Override
	public Node getChild(int i) {
		return null;
	}

	/**
	 * Models cannot have children.
	 */
	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return transform;
	}
	
	public Primitive getPrimitive(){
		return this.primitive;
	}
	
	public void setPrimative(Primitive primitive){
		this.primitive = primitive;
		this.primitive.computeBoundingBox(this.boundingBox);
	}
	
	public Surface getSurface(){
		return this.surface;
	}
	
	public void setSurface(Surface surface){
		this.surface = surface;
	}

	@Override
	public void render() {
		this.surface.render();
		this.primitive.render();
	}

	@Override
	public void setDisplay(int width, int height) {
		//VOID for now
	}

	@Override
	public void setup() {
		//VOID for now
	}
}
