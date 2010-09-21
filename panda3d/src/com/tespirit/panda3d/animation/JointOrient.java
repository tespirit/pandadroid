package com.tespirit.panda3d.animation;

public class JointOrient extends Joint{
	
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
