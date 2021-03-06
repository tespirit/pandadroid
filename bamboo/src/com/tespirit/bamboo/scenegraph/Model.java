package com.tespirit.bamboo.scenegraph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.render.RenderableNode;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.*;

public class Model extends RenderableNode implements Externalizable{
	private Primitive mPrimitive;
	private Surface mSurface;
	
	private AxisAlignedBox mBoundingBox;
	
	private Matrix3d mTransform;
	private Matrix3d mWorldTransform;
	
	public Model(){
		this(null);
	}
	
	public Model(String name){
		super(name);
		this.mBoundingBox = new AxisAlignedBox();
		float[] m = Matrix3d.createBuffer(2);
		this.mTransform = new Matrix3d(m);
		this.mWorldTransform = new Matrix3d(m, Matrix3d.SIZE);
		this.mSurface = Surface.getDefaultSurface();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.mBoundingBox;
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
	public void update(Matrix3d transform) {
		super.update(transform);
		this.mWorldTransform.multiply(transform,this.mTransform);
		this.mPrimitive.update();
	}

	@Override
	public Matrix3d getTransform() {
		return this.mTransform;
	}

	@Override
	public Matrix3d getWorldTransform() {
		return this.mWorldTransform;
	}
	
	public Primitive getPrimitive(){
		return this.mPrimitive;
	}
	
	public void setPrimitive(Primitive primitive){
		this.mPrimitive = primitive;
		this.mPrimitive.computeBoundingBox(this.mBoundingBox);
		this.registerResource(this.mSurface);
	}
	
	public Surface getSurface(){
		return this.mSurface;
	}
	
	public void setSurface(Surface surface){
		if(surface != null)
			this.mSurface = surface;
		else
			this.mSurface = Surface.getDefaultSurface();
		this.registerResource(this.mSurface);
	}
	
	@Override
	public boolean alphaSort() {
		return this.mSurface.hasAlpha();
	}

	@Override
	public void render() {
		this.mSurface.renderStart();
		this.mPrimitive.render();
		this.mSurface.renderEnd();
	}
	
	@Override
	public void setRenderManager(RenderManager renderManager){
		super.setRenderManager(renderManager);
		this.registerResource(this.mSurface);
		this.registerResource(this.mPrimitive);
	}
	
	@Override
	protected void recycleInternal(){
		this.unregisterResource(this.mSurface);
		this.unregisterResource(this.mPrimitive);
		this.mBoundingBox = null;
		this.mTransform = null;
		this.mWorldTransform = null;
		this.mPrimitive = null;
		this.mSurface = null;
	}
	
	//IO
	private static final long serialVersionUID = 385991631261527460L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.read(in);
    	this.mBoundingBox.getMin().set(in.readFloat(), in.readFloat(), in.readFloat());
    	this.mBoundingBox.getMax().set(in.readFloat(), in.readFloat(), in.readFloat());
    	for(int i = 0; i < Matrix3d.SIZE; i++){
    		this.mTransform.setValue(in.readFloat(), i);
    	}
    	this.mPrimitive = (Primitive)in.readObject();
    	this.mSurface = (Surface)in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.write(out);
		out.writeFloat(this.mBoundingBox.getMin().getX());
		out.writeFloat(this.mBoundingBox.getMin().getY());
		out.writeFloat(this.mBoundingBox.getMin().getZ());
		out.writeFloat(this.mBoundingBox.getMax().getX());
		out.writeFloat(this.mBoundingBox.getMax().getY());
		out.writeFloat(this.mBoundingBox.getMax().getZ());
		for(int i = 0; i < Matrix3d.SIZE; i++){
			out.writeFloat(this.mTransform.getValue(i));
		}
		out.writeObject(this.mPrimitive);
		out.writeObject(this.mSurface);
	}
}
