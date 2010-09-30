package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.vectors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public abstract class Node {

	private String mName;
	private int mUid;
		
	static private Map<String, Node> nameLookup = new HashMap<String, Node>();
	static private Vector<Node> nodes = new Vector<Node>(); 
	
	public Node(){
		this(null);
	}
	
	public Node(String name){
		this.mName = name;
		this.init();
	}
	
	protected void init(){
		if(this.mName != null){
			Node.nameLookup.put(this.mName, this);
		}
		this.mUid = Node.nodes.size();
		Node.nodes.add(this);
	}
	
	public int getUid(){
		return this.mUid;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public void setName(String n){
		if(this.mName != null){
			Node.nameLookup.remove(this.mName);
		}
		this.mName = n;
		Node.nameLookup.put(this.mName, this);
	}
	
	public abstract Node getChild(int i);
	
	public abstract int getChildCount();
	
	public abstract Matrix3d getTransform();
	
	public abstract Matrix3d getWorldTransform();
	
	public abstract AxisAlignedBox getBoundingBox();
	
	/**
	 * this updates the nodes with the new tranformation
	 * @param m
	 */
	public abstract void update(Matrix3d transform);
	
	
	public static Node getNode(String name){
		return Node.nameLookup.get(name);
	}
	
	public static Node getNode(int uid){
		return Node.nodes.get(uid);
	}
	
	//for debugging
	public String toString(){
	public String getNodeInfo(){
		String className = super.toString();
		int i = className.lastIndexOf('.');
		if(i != -1){
			className = className.substring(i+1);
		}
		i = className.indexOf('@');
		if(i != -1){
			className = className.substring(0, i);
		}
		String name = this.mName;
		if(name == null){
			name = "<no name>";
		}
		return className+": " + name; 
	}
}
