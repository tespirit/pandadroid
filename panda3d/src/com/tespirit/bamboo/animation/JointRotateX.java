package com.tespirit.bamboo.animation;

public class JointRotateX  extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 265567534825069483L;

	public JointRotateX(){
		super();
	}
	
	public JointRotateX(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.localTransform.identity3x3().rotateX(dofs.getNext());
	}

}
