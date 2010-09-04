package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;
import java.util.Hashtable;

public abstract class Node {
	
	private String name;
	
	static private Hashtable<String, Node> LOOKUP;
	
	public Node(){
		this.name = null;
	}
	
	public Node(String name){
		this.name = name;
		if(this.name != null){
			Node.LOOKUP.put(this.name, this);
		}
	}
	
	public abstract Node getChild(int i);
	
	public abstract int getChildCount();
	
	public abstract Matrix3d getTransform();
	
	public abstract AxisAlignedBox getBoundingBox();
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String n){
		if(this.name != null){
			Node.LOOKUP.remove(this.name);
		}
		this.name = n;
		Node.LOOKUP.put(this.name, this);
	}
	
	public static Node getNode(String name){
		return Node.LOOKUP.get(name);
	}
}
