package com.tespirit.bamboo.animation;

public class JointRotateY  extends Joint{

	/**
	 * 
	 */
	private static final long serialVersionUID = -861036497708679065L;

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
