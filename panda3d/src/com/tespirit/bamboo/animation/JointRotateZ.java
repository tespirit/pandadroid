package com.tespirit.bamboo.animation;

public class JointRotateZ  extends Joint{
	
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
