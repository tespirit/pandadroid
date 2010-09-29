package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class JointTranslate extends Joint implements Externalizable{

	public JointTranslate(){
		super();
	}
	
	public JointTranslate(String name){
		super(name);
	}

	@Override
	protected void updateLocalMatrix(DofStream dofs) {
		this.mLocalTransform.getTranslation().set(dofs.getNext(), dofs.getNext(), dofs.getNext());
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
