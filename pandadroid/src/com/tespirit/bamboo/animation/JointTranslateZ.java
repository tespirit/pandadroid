package com.tespirit.bamboo.animation;

public class JointTranslateZ  extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3347011540281203701L;

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
