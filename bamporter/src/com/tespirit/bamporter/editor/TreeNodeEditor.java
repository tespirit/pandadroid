package com.tespirit.bamporter.editor;

import javax.swing.tree.DefaultMutableTreeNode;

import com.tespirit.bamporter.app.BamporterFrame;

public abstract class TreeNodeEditor extends DefaultMutableTreeNode implements Editor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2041667420816532942L;

	
	/**
	 * Default constructor allows children.
	 */
	protected TreeNodeEditor(){
		this(true);
	}
	
	protected TreeNodeEditor(boolean allowChildren){
		this.allowsChildren = allowChildren;
	}
	
	/**
	 * use this for initialization as it does not call insertEditor.
	 * @param node
	 */
	public void addNewEditor(TreeNodeEditor node){
		super.insert(node, this.getChildCount());
	}
	
	public void addEditor(TreeNodeEditor node){
		this.insertEditor(node, this.getChildCount());
		
	}
	
	public void insertEditor(TreeNodeEditor newChild, int childIndex){
		BamporterFrame.getInstance().insertNodeTo(newChild, this, childIndex);
	}
	
	public void removeEditor(TreeNodeEditor node){
		BamporterFrame.getInstance().removeNode(node);
	}
	
	public void removeEditorFromParent(){
		if(this.parent instanceof TreeNodeEditor){
			((TreeNodeEditor)this.parent).removeEditor(this);
		}
	}
	
	public void updateEditor(){
		BamporterFrame.getInstance().refreshNode(this);
	}
	
	@Override
	public void recycle(){
		for(Object child : this.children){
			if(child instanceof TreeNodeEditor){
				((TreeNodeEditor)child).recycle();
			}
		}
	}
}
