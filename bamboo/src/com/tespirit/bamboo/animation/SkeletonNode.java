package com.tespirit.bamboo.animation;

import com.tespirit.bamboo.vectors.Matrix3d;

public class SkeletonNode extends Joint{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -4794852768832999851L;
	Matrix3d translateTransform;
	
	public SkeletonNode(){
		this(null);
	}
	
	public SkeletonNode(String name){
		super(name);
		this.translateTransform = new Matrix3d();
	}
	
	@Override
	public Matrix3d getTransform() {
		return this.translateTransform;
	}
	
	public Matrix3d getRotateTransform(){
		return this.localTransform;
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.translateTransform.getTranslation().set(dofs.getNext(), dofs.getNext(), dofs.getNext());
		this.localTransform.identity3x3();
		this.localTransform.rotateX(dofs.getNext());
		this.localTransform.rotateY(dofs.getNext());
		this.localTransform.rotateZ(dofs.getNext());
		this.localTransform.multiply(translateTransform, this.localTransform);
	}

}
