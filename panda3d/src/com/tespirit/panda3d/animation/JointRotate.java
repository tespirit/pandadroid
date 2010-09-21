package com.tespirit.panda3d.animation;

public class JointRotate extends Joint{
	
	public JointRotate(){
		super();
	}
	
	public JointRotate(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.identity3x3().rotateEuler(dofs.getNext(), dofs.getNext(), dofs.getNext());
	}

}
