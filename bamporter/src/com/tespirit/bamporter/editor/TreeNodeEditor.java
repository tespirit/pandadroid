package com.tespirit.bamporter.editor;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

public abstract class TreeNodeEditor implements Editor, TreeNode, Enumeration<TreeNodeEditor> {

	private ArrayList<TreeNodeEditor> mChildren;
	private TreeNodeEditor mParentNode;
	
	/**
	 * Default constructor allows children.
	 */
	protected TreeNodeEditor(){
		this(true);
	}
	
	protected TreeNodeEditor(boolean allowChildren){
		if(allowChildren){
			this.mChildren = new ArrayList<TreeNodeEditor>();
		} else {
			this.mChildren = null;
		}
	}
	
	public void add(TreeNodeEditor node){
		node.setParent(this);
		this.mChildren.add(node);
	}
	
	public void remove(TreeNodeEditor node){
		node.setParent(null);
		this.mChildren.remove(node);
	}
	
	private void setParent(TreeNodeEditor node){
		mParentNode = node;
	}
	
	@Override
	public Enumeration<TreeNodeEditor> children() {
		return this;
	}

	@Override
	public boolean getAllowsChildren() {
		return this.mChildren != null;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return this.mChildren.get(childIndex);
	}

	@Override
	public int getChildCount() {
		if(this.mChildren != null){
			return this.mChildren.size();
		} else {
			return 0;
		}
	}

	@Override
	public int getIndex(TreeNode node) {
		if(this.mChildren != null){
			return this.mChildren.indexOf(node);
		} else {
			return 0;
		}
	}

	@Override
	public TreeNode getParent() {
		return this.mParentNode;
	}

	@Override
	public boolean isLeaf() {
		return this.getChildCount() > 0;
	}

	@Override
	public boolean hasMoreElements() {
		return this.mChildren.iterator().hasNext();
	}

	@Override
	public TreeNodeEditor nextElement() {
		return this.mChildren.iterator().next();
	}

}
