package com.tespirit.panda3d.scenegraph;

import java.util.*;
import com.tespirit.panda3d.vectors.*;

public class Group extends Node {
	
	private ArrayList<Node> children;
	private Matrix3d transform;
	
	public Group(){
		this.children = new ArrayList<Node>();
		this.transform = new Matrix3d();//optimize later to share a single matrix buffer.
	}
	
	public Group(String n){
		super(n);
		this.children = new ArrayList<Node>();
		this.transform = new Matrix3d();//optimize later to share a single matrix buffer.
	}
	
	public void appendChild(Node node){
		this.children.add(node);
	}
	
	@Override
	public Node getChild(int i) {
		return this.children.get(i);
	}
	
	@Override
	public int getChildCount(){
		return this.children.size();
	}
	
	@Override
	public  Matrix3d getTransform(){
		return this.transform;
	}
	
	/**
	 * This will compute the bounding box
	 */
	@Override
	public AxisAlignedBox getBoundingBox(){
		return null;
	}
}