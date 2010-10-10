package com.tespirit.bamporter.editor;

import java.awt.Component;

import javax.swing.tree.DefaultMutableTreeNode;

import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.app.Preferences;

public abstract class TreeNodeEditor extends DefaultMutableTreeNode implements Editor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2041667420816532942L;
	private Object mTheme;
	private Component mPropertyPanel;

	
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
	public Component getPropertyPanel() {
		if(this.mPropertyPanel == null){
			this.mPropertyPanel = this.generatePanel();
		} else if(!this.mTheme.equals(Preferences.getTheme())){
			Preferences.refreshComponent(this.mPropertyPanel);
		}
		this.mTheme = Preferences.getTheme();
		return this.mPropertyPanel;
	}
	
	protected abstract Component generatePanel() ;

	@Override
	public void recycle(){
		if(this.children != null){
			for(Object child : this.children){
				if(child instanceof TreeNodeEditor){
					((TreeNodeEditor)child).recycle();
				}
			}
		}
	}
}
