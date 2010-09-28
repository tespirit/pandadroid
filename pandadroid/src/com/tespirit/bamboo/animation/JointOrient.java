package com.tespirit.bamboo.animation;

public class JointOrient extends Joint{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8894790342439752292L;

	public JointOrient(){
		super();
	}
	
	public JointOrient(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		//VOID: this joint does not recieve animations.
	}

}
