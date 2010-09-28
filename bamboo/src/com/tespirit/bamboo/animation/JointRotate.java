package com.tespirit.bamboo.animation;

public class JointRotate extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8315657350959674882L;

	public JointRotate(){
		super();
	}
	
	public JointRotate(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.identity3x3();
		this.localTransform.rotateX(dofs.getNext());
		this.localTransform.rotateY(dofs.getNext());
		this.localTransform.rotateZ(dofs.getNext());
	}

}
