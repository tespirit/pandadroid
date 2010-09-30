package com.tespirit.bamboo.scenegraph;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

import com.tespirit.bamboo.vectors.*;

public class Group extends Node implements Externalizable{
	
	private Matrix3d mTransform;
	private Matrix3d mWorldTransform;

	private List<Node> mChildren;
	
	public Group(){
		this(null, new ArrayList<Node>());
	}
	
	public Group(String n){
		this(n, new ArrayList<Node>());
	}
	
	public Group(List<Node> childern){
		this(null, childern);
	}
	
	public Group(String n, List<Node> children){
		super(n);
		this.mChildren = children;
	}
	
	@Override
	protected void init(){
		float[] m = new float[Matrix3d.SIZE*2];
		this.mTransform = new Matrix3d(m);
		this.mTransform.identity();
		this.mWorldTransform = new Matrix3d(m, Matrix3d.SIZE);
	}
	
	public void appendChild(Node node){
		if(node != null){
			this.mChildren.add(node);
		}
	}
	
	@Override
	public Node getChild(int i) {
		return this.mChildren.get(i);
	}
	
	@Override
	public int getChildCount(){
		return this.mChildren.size();
	}
	
	@Override
	public AxisAlignedBox getBoundingBox(){
		return null;
	}
	
	@Override
	public Matrix3d getTransform() {
		return this.mTransform;
	}
	
	@Override
	public Matrix3d getWorldTransform() {
		return this.mWorldTransform;
	}

	@Override
	public void update(Matrix3d transform) {
		this.mWorldTransform.multiply(transform,this.mTransform);
		for(int i = 0; i < this.mChildren.size(); i++){
			this.mChildren.get(i).update(this.mWorldTransform);
		}
	}

	//IO
	private static final long serialVersionUID = -7994133683481188642L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.init();
    	this.setName(in.readUTF());
    	for(int i = 0; i < Matrix3d.SIZE; i++){
    		this.mTransform.setValue(in.readFloat(), i);
    	}
    	int childCount = in.readInt();
    	this.mChildren = new ArrayList<Node>(childCount);
    	for(int i = 0; i < childCount; i++){
    		this.mChildren.add((Node)in.readObject());
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.getName());
		for(int i = 0; i < Matrix3d.SIZE; i++){
			out.writeFloat(this.mTransform.getValue(i));
		}
		out.writeInt(this.mChildren.size());
		for(Node child : this.mChildren){
			out.writeObject(child);
		}
	}
}