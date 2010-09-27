package com.tespirit.bamboo.animation;

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
