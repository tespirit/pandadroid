package com.tespirit.bamboo.animation;

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
	
	protected Matrix3d localTransform;
	protected Matrix3d worldTransform;
	
	private AxisAlignedBox bone;
	
	protected ArrayList<Joint> children;
	
	private DofStream dofs;
	
	public Joint(){
		this(null);
	}
	
	public Joint(String name){
		super(name);
		this.children = new ArrayList<Joint>();
		float[] buffer = Matrix3d.createBuffer(2);
		this.localTransform = new Matrix3d(buffer);
		this.worldTransform = new Matrix3d(buffer, Matrix3d.SIZE);
	}
	
	/**
	 * A convenience function to set up bones for this node and all
	 * child nodes.
	 * @param radius
	 */
	public void createAllBones(float radius){
		this.createBone(radius);
		for(Joint j : this.children){
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
	public void createBone(float radius){
		this.bone = new AxisAlignedBox();
		
		Vector3d sizeOffset = new Vector3d(radius, radius, radius);
		this.bone.grow(sizeOffset);
		sizeOffset.set(-radius, -radius, -radius);
		this.bone.grow(sizeOffset);
		
		//now add children!
		for(Joint j : this.children){
			Vector3d top = j.localTransform.getTranslation();
			sizeOffset.set(radius, radius, radius);
			sizeOffset.add(top);
			this.bone.grow(sizeOffset);
			sizeOffset.set(-radius, -radius, -radius);
			sizeOffset.add(top);
			this.bone.grow(sizeOffset);
		}
	}
	
	public void setDofs(DofStream dofs){
		this.dofs = dofs;
		for(Joint j : this.children){
			j.setDofs(dofs);
		}
		
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return this.bone;
	}
	
	public void appendChild(Joint joint){
		this.children.add(joint);
		if(this.dofs!= null){
			joint.setDofs(this.dofs);
		}
	}

	@Override
	public Node getChild(int i) {
		return this.children.get(i);
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public Matrix3d getTransform() {
		return this.localTransform;
	}

	@Override
	public Matrix3d getWorldTransform() {
		return this.worldTransform;
	}

	@Override
	public void update(Matrix3d transform){
		if(this.dofs != null){
			this.updateLocalMatrix(this.dofs);
		}
		this.worldTransform.multiply(transform,this.localTransform);
		for(Joint joint : this.children){
			joint.update(this.worldTransform);
		}
	}
	
	protected abstract void updateLocalMatrix(DofStream dofs);

}
