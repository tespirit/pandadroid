package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.*;
import java.util.Hashtable;
import java.util.Vector;

public abstract class Node {
	
	private String name;
	private int uid;
		
	static private Hashtable<String, Node> nameLookup = new Hashtable<String, Node>();
	static private Vector<Node> nodes = new Vector<Node>(); 
	
	public Node(){
		this(null);
	}
	
	public Node(String name){
		this.name = name;
		if(this.name != null){
			Node.nameLookup.put(this.name, this);
		}
		this.uid = Node.nodes.size();
		Node.nodes.add(this);
	}
	
	public int getUid(){
		return this.uid;
	}
	
	public abstract Node getChild(int i);
	
	public abstract int getChildCount();
	
	public abstract Matrix3d getTransform();
	
	public abstract Matrix3d getWorldTransform();
	
	public abstract AxisAlignedBox getBoundingBox();
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * this updates the nodes with the new tranformation
	 * @param m
	 */
	public abstract void update(Matrix3d transform);
	
	public void setName(String n){
		if(this.name != null){
			Node.nameLookup.remove(this.name);
		}
		this.name = n;
		Node.nameLookup.put(this.name, this);
	}
	
	public static Node getNode(String name){
		return Node.nameLookup.get(name);
	}
	
	public static Node getNode(int uid){
		return Node.nodes.get(uid);
	}
}
