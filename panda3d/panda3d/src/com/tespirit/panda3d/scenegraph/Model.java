package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;

public class Model extends BaseNode{
	
	public Model(){
		
	}
	
	public Model(String name){
		super(name);
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	public BaseNode getChild(int i) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return null;
	}

}
