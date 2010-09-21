package com.tespirit.panda3d.animation;

public class JointTranslateX  extends Joint{

	public JointTranslateX(){
		super();
	}
	
	public JointTranslateX(String name){
		super(name);
	}
	
	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.getTranslation().setX(dofs.getNext());
	}

}
