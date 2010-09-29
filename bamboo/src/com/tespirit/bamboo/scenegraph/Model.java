package com.tespirit.bamboo.scenegraph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.vectors.*;

public class Model extends Node implements RenderableNode, Externalizable {
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
		this.mSurface = Surface.getDefaultSurface();
	}
	
	@Override
	protected void init(){
		super.init();
		this.mBoundingBox = new AxisAlignedBox();
		float[] m = new float[Matrix3d.SIZE*2];
		this.mTransform = new Matrix3d(m);
		this.mTransform.identity();
		this.mWorldTransform = new Matrix3d(m, Matrix3d.SIZE);
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
		this.mWorldTransform.multiply(transform,this.mTransform);
		this.mPrimitive.updateModifiers();
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
	
	public void setPrimative(Primitive primitive){
		this.mPrimitive = primitive;
		this.mPrimitive.computeBoundingBox(this.mBoundingBox);
	}
	
	public Surface getSurface(){
		return this.mSurface;
	}
	
	public void setSurface(Surface surface){
		if(surface != null)
			this.mSurface = surface;
		else
			this.mSurface = Surface.getDefaultSurface();
	}

	@Override
	public void render() {
		this.mSurface.render();
		this.mPrimitive.render();
	}

	@Override
	public void setDisplay(int width, int height) {
		//VOID for now
	}

	@Override
	public void setup() {
		//VOID for now
	}
	
	//IO
	private void writeObject(ObjectOutputStream out) throws IOException{
		
	}
	
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
    	
    }

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.init();
    	this.setName((String)in.readObject());
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
		out.writeObject(this.getName());
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
