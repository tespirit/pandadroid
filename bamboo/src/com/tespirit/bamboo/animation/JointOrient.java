package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class JointOrient extends Joint implements Externalizable{

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

	@Override
	public int getDofCount(){
		return 0;
	}
	
    //IO
	private static final long serialVersionUID = -1269762051395304350L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.read(in);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		this.write(out);
	}

}
