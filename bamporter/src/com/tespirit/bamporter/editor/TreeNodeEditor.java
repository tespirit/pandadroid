package com.tespirit.bamporter.editor;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public abstract class TreeNodeEditor implements Editor, MutableTreeNode, Enumeration<TreeNodeEditor> {

	private ArrayList<TreeNodeEditor> mChildren;
	private MutableTreeNode mParentNode;
	
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
		return this.getChildCount() == 0;
	}

	int mCurrentIndex = 0;
	@Override
	public boolean hasMoreElements() {
		boolean retVal = this.mChildren.size() > this.mCurrentIndex;
		if(retVal == false){
			mCurrentIndex = 0;
		}
		return retVal;
	}

	@Override
	public TreeNodeEditor nextElement() {
		TreeNodeEditor node = this.mChildren.get(this.mCurrentIndex);
		this.mCurrentIndex++;
		return node;
	}
	
	@Override
	public void insert(MutableTreeNode child, int index) {
		if(child instanceof TreeNodeEditor){
			TreeNodeEditor node = (TreeNodeEditor)child;
			node.setParent(this);
			this.mChildren.add(index, node);
		}
	}

	@Override
	public void remove(int index) {
		this.mChildren.remove(index);
	}

	@Override
	public void remove(MutableTreeNode node) {
		this.mChildren.remove(node);
	}

	@Override
	public void removeFromParent() {
		if(this.mParentNode != null){
			this.mParentNode.remove(this);
		}
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		this.removeFromParent();
		this.mParentNode = newParent;
	}

	@Override
	public void setUserObject(Object object) {
		//VOID these nodes are the userobjects!
	}

}
