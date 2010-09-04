package com.tespirit.panda3d.scenegraph;

import java.util.*;
import com.tespirit.panda3d.vectors.*;

public class Node extends BaseNode {
	
	private ArrayList<BaseNode> children;
	private Matrix3d transform;
	private AxisAlignedBox boundingBox;
	
	public Node(){
		this.children = new ArrayList<BaseNode>();
	}
	
	public Node(String n){
		super(n);
		this.children = new ArrayList<BaseNode>();
	}
	
	@Override
	public BaseNode getChild(int i) {
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
	
	@Override
	public AxisAlignedBox getBoundingBox(){
		return this.boundingBox;
	}
}