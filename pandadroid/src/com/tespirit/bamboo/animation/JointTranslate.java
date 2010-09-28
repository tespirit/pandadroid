package com.tespirit.bamboo.animation;

public class JointTranslate  extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8504915220866707962L;

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
