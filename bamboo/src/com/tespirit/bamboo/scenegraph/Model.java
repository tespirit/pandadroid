package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.*;

public class Model extends Transform implements RenderableNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8513346997009224558L;
	private Primitive primitive;
	private Surface surface;
	
	private AxisAlignedBox boundingBox;
	
	public Model(){
		super();
		this.surface = Surface.getDefaultSurface();
		this.boundingBox = new AxisAlignedBox();
	}
	
	public Model(String name){
		super(name);
		this.surface = Surface.getDefaultSurface();
		this.boundingBox = new AxisAlignedBox();
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
		if(surface != null)
			this.surface = surface;
		else
			this.surface = Surface.getDefaultSurface();
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
