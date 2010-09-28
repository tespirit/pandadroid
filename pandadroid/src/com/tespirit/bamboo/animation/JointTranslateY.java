package com.tespirit.bamboo.animation;

public class JointTranslateY  extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2721944481737041697L;

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
