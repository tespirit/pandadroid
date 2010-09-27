package com.tespirit.bamboo.animation;

public class JointTranslate  extends Joint{
	
	public JointTranslate(){
		super();
	}
	
	public JointTranslate(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.getTranslation().set(dofs.getNext(), dofs.getNext(), dofs.getNext());
	}

}
