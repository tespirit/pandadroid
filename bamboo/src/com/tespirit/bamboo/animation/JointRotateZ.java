package com.tespirit.bamboo.animation;

public class JointRotateZ  extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2917384407880777411L;

	public JointRotateZ(){
		super();
	}
	
	public JointRotateZ(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.identity3x3().rotateZ(dofs.getNext());
	}

}
