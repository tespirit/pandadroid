package com.tespirit.bamboo.animation;

public class JointRotateY  extends Joint{

	public JointRotateY(){
		super();
	}
	
	public JointRotateY(String name){
		super(name);
	}
	
	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.identity3x3().rotateY(dofs.getNext());
	}

}
