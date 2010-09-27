package com.tespirit.bamboo.scenegraph;

import java.util.*;

import com.tespirit.bamboo.vectors.*;

public class Group extends Transform{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8461771187366964690L;

	private ArrayList<Node> children;
	
	private AxisAlignedBox boundingBox;
	
	public Group(){
		super();
		this.children = new ArrayList<Node>();
		this.boundingBox = new AxisAlignedBox();
	}
	
	public Group(String n){
		super(n);
		this.children = new ArrayList<Node>();
		this.boundingBox = new AxisAlignedBox();
	}
	
	public Group(ArrayList<Node> childern){
		super();
		this.children = childern;
		this.boundingBox = new AxisAlignedBox();
	}
	
	public Group(String n, ArrayList<Node> children){
		super(n);
		this.children = children;
		this.boundingBox = new AxisAlignedBox();
	}
	
	public void appendChild(Node node){
		if(node != null){
			this.children.add(node);
		}
	}
	
	@Override
	public Node getChild(int i) {
		return this.children.get(i);
	}
	
	@Override
	public int getChildCount(){
		return this.children.size();
	}
	
	/**
	 * This will compute the bounding box
	 */
	@Override
	public AxisAlignedBox getBoundingBox(){
		return boundingBox;
	}
}