package com.tespirit.bamboo.animation;

public class JointTranslateX  extends Joint{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492132252272848349L;

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
