package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.vectors.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class Node {

	private String mName;
	private int mUid;
		
	static private Map<String, Node> nameLookup = new HashMap<String, Node>();
	static private List<Node> nodes = new Vector<Node>(); 
	static private List<Node> newNodes = new Vector<Node>();
	
	public Node(){
		this(null);
	}
	
	public Node(String name){
		this.mName = name;
		if(this.mName != null){
			Node.nameLookup.put(this.mName, this);
		}
		this.mUid = Node.nodes.size();
		Node.nodes.add(this);
		Node.newNodes.add(this);
	}
	
	public int getUid(){
		return this.mUid;
	}
	
	public String getName() {
		if(this.mName != null && this.mName.length() > 0)
			return this.mName;
		else 
			return null;
	}
	
	public void setName(String n){
		if(this.getName() != null){
			Node.nameLookup.remove(this.mName);
		}
		if(n != null && n.length() > 0){
			this.mName = n;
			Node.nameLookup.put(this.mName, this);
		} else {
			this.mName = "";
		}
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
	
	public abstract void init();
	
	public static Node getNode(String name){
		return Node.nameLookup.get(name);
	}
	
	public static Node getNode(int uid){
		return Node.nodes.get(uid);
	}
	
	public static void initNewNodes(){
		for(int i = 0; i < Node.newNodes.size(); i++){
			Node.newNodes.get(i).init();
			Node.newNodes.set(i, null);
		}
		Node.newNodes.clear();
	}
	
	//IO
	protected void write(ObjectOutput out) throws IOException{
		out.writeUTF(this.mName);
	}
	
	protected void read(ObjectInput in) throws IOException, ClassNotFoundException{
		this.setName(in.readUTF());
	}
	
}
