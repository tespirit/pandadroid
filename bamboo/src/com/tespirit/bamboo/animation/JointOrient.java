package com.tespirit.bamboo.animation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

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
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		this.write(out);
	}
	
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
    	this.read(in);
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
