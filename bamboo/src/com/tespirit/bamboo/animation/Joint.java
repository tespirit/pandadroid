package com.tespirit.bamboo.animation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.*;

/**
 * In keeping things simple, the joint class will be a SceneGraphNode,
 * though not necisarily in the renderable scene tree. By being a SGN, 
 * future extensions, ie physics, can also be added to this without having
 * to special case it.
 * This class is optimized towards assumptions about a skeleton, such as a joint
 * does not need to support dynamic addition of children.
 * This class can still have any SceneGraph node as a child, making it easy to
 * have no skinning required skeleton animations.
 * @author Dennis McAutoKnight
 *
 */
public abstract class Joint extends Node{
	protected Matrix3d mLocalTransform;
	protected Matrix3d mWorldTransform;
	protected ArrayList<Joint> mChildren;
	private AxisAlignedBox mBone;
	private int mChannelCount;
	
	public Joint(){
		this(null);
	}
	
	public Joint(String name){
		super(name);
		this.mChildren = new ArrayList<Joint>();
		this.mChannelCount = this.getDofCount();

		float[] buffer = Matrix3d.createBuffer(2);
		this.mLocalTransform = new Matrix3d(buffer);
		this.mWorldTransform = new Matrix3d(buffer, Matrix3d.SIZE);
	}
	
	@Override
	public int updatePriority(){
		return 1000;
	}
	
	/**
	 * A convenience function to set up bones for this node and all
	 * child nodes.
	 * @param radius
	 */
	public void createAllBones(float radius){
		this.createBone(radius);
		for(Joint j : this.mChildren){
			j.createAllBones(radius);
		}
	}

	/**
	 * call this to set up a bone which can be used for selection and collision
	 * detection. It will create a bounding box which is the bone.
	 * since this is based on the children, make sure all child nodes have been
	 * added before calling this.
	 * @param radius
	 */
	public void createBone(float boneSize){
		this.mBone = new AxisAlignedBox();
		
		Vector3d sizeOffset = new Vector3d(boneSize, boneSize, boneSize);
		this.mBone.grow(sizeOffset);
		sizeOffset.set(-boneSize, -boneSize, -boneSize);
		this.mBone.grow(sizeOffset);
		
		//now add children!
		for(Joint j : this.mChildren){
			Vector3d top = j.mLocalTransform.getTranslation();
			sizeOffset.set(boneSize, boneSize, boneSize);
			sizeOffset.add(top);
			this.mBone.grow(sizeOffset);
			sizeOffset.set(-boneSize, -boneSize, -boneSize);
			sizeOffset.add(top);
			this.mBone.grow(sizeOffset);
		}
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.mBone;
	}
	
	public void appendChild(Joint joint){
		this.mChildren.add(joint);
		this.mChannelCount += joint.mChannelCount;
	}
	
	public int getChannelCount(){
		return this.mChannelCount;
	}

	@Override
	public Node getChild(int i) {
		return this.mChildren.get(i);
	}

	@Override
	public int getChildCount() {
		return this.mChildren.size();
	}

	@Override
	public Matrix3d getTransform() {
		return this.mLocalTransform;
	}

	@Override
	public Matrix3d getWorldTransform() {
		return this.mWorldTransform;
	}

	@Override
	public void update(Matrix3d transform){
		this.mWorldTransform.multiply(transform,this.mLocalTransform);
		for(Joint joint : this.mChildren){
			joint.update(this.mWorldTransform);
		}
	}
	
	public void update(DofStream dofs){
		this.updateLocalMatrix(dofs);
		for(Joint j : this.mChildren){
			j.update(dofs);
		}
	}
	
	@Override
	protected void recycleInternal(){
		this.mBone = null;
		this.mLocalTransform = null;
		this.mWorldTransform = null;
		this.mChildren.clear();
	}
	
	//IO
	private static final long serialVersionUID = -6138379270722022703L;
	protected void write(ObjectOutput out) throws IOException{
		super.write(out);
		if(this.mBone != null){
			out.writeBoolean(true);
    		out.writeFloat(this.mBone.getMin().getX());
			out.writeFloat(this.mBone.getMin().getY());
			out.writeFloat(this.mBone.getMin().getZ());
			out.writeFloat(this.mBone.getMax().getX());
			out.writeFloat(this.mBone.getMax().getY());
			out.writeFloat(this.mBone.getMax().getZ());
		} else {
			out.writeBoolean(false);
		}
		for(int i = 0; i < Matrix3d.SIZE; i++){
			out.writeFloat(this.mLocalTransform.getValue(i));
		}
		out.writeInt(this.mChildren.size());
		for(Joint child : this.mChildren){
			out.writeObject(child);
		}
	}
	
	@Override
	protected void read(ObjectInput in) throws IOException, ClassNotFoundException{
		super.read(in);
    	if(in.readBoolean()){
    		this.mBone = new AxisAlignedBox();
    		this.mBone.setMin(in.readFloat(), in.readFloat(), in.readFloat());
    		this.mBone.setMax(in.readFloat(), in.readFloat(), in.readFloat());
    	}
    	for(int i = 0; i < Matrix3d.SIZE; i++){
    		this.mLocalTransform.setValue(in.readFloat(), i);
    	}
    	int childCount = in.readInt();
    	this.mChildren = new ArrayList<Joint>(childCount);
    	for(int i = 0; i < childCount; i++){
    		Joint child = (Joint)in.readObject();
    		this.mChildren.add(child);
    		this.mChannelCount += child.getChannelCount();
    	}
    }
	
	protected abstract void updateLocalMatrix(DofStream dofs);
	public abstract int getDofCount();
}
