package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;
import java.util.Hashtable;

public abstract class BaseNode {
	
	private String name;
	
	static private Hashtable<String, BaseNode> LOOKUP;
	
	public BaseNode(){
		this.name = null;
	}
	
	public BaseNode(String name){
		this.name = name;
		if(this.name != null){
			BaseNode.LOOKUP.put(this.name, this);
		}
	}
	
	public abstract BaseNode getChild(int i);
	
	public abstract int getChildCount();
	
	public abstract Matrix3d getTransform();
	
	public abstract AxisAlignedBox getBoundingBox();
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String n){
		if(this.name != null){
			BaseNode.LOOKUP.remove(this.name);
		}
		this.name = n;
		BaseNode.LOOKUP.put(this.name, this);
	}
	
	public static BaseNode getNode(String name){
		return BaseNode.LOOKUP.get(name);
	}
}
