package com.tespirit.panda3d.animation;

public class JointTranslateY  extends Joint{
	
	public JointTranslateY(){
		super();
	}
	
	public JointTranslateY(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.getTranslation().setY(dofs.getNext());
	}

}
