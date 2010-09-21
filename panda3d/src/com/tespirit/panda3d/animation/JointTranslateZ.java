package com.tespirit.panda3d.animation;

public class JointTranslateZ  extends Joint{
	
	public JointTranslateZ(){
		super();
	}
	
	public JointTranslateZ(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.getTranslation().setZ(dofs.getNext());
	}

}
