package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class JointRotate extends Joint implements Externalizable{

	public JointRotate(){
		super();
	}
	
	public JointRotate(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.mLocalTransform.identity3x3();
		this.mLocalTransform.rotateX(dofs.getNext());
		this.mLocalTransform.rotateY(dofs.getNext());
		this.mLocalTransform.rotateZ(dofs.getNext());
	}

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
