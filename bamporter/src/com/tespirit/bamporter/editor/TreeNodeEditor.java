package com.tespirit.bamporter.editor;

import java.awt.Component;

import javax.swing.tree.DefaultMutableTreeNode;

import com.tespirit.bamboo.render.RenderManager;
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
	protected TreeNodeEditor(Object data){
		this(data, true);
	}
	
	protected TreeNodeEditor(Object data, boolean allowChildren){
		this.allowsChildren = allowChildren;
		this.userObject = data;
	}
	
	protected String getNodeName(){
		return null;
	}
	
	@Override
	public String toString(){
		String nodeName = this.getNodeName();
		if(nodeName != null){
			return Util.getClassName(this.userObject)+" "+nodeName;
		} else {
			return Util.getClassName(this.userObject);
		}
	}
	
	/**
	 * use this for initialization as it does not call insertEditor.
	 * @param node
	 */
	public void addNewEditor(Object object){
		Editor editor = EditorFactory.createEditor(object);
		if(editor instanceof TreeNodeEditor){
			super.insert((TreeNodeEditor)editor, this.getChildCount());
		}
	}
	
	public void addEditor(Object object){
		this.insertEditor(object, this.getChildCount());
		
	}
	
	public void insertEditor(Object object, int childIndex){
		Editor editor = EditorFactory.createEditor(object);
		if(editor instanceof TreeNodeEditor){
			BamporterFrame.getInstance().insertNodeTo((TreeNodeEditor)editor, this, childIndex);
		}
		
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
	
	public RenderManager getRenderManager(){
		return BamporterFrame.getInstance().getRenderManger();
	}
	
	@Override
	public Component getPropertyPanel() {
		if(this.mPropertyPanel == null){
			this.mPropertyPanel = this.generatePanel();
		} else if(!this.mTheme.equals(Preferences.getTheme())){
			Preferences.refreshComponent(this.mPropertyPanel);
		}
		this.mTheme = Preferences.getTheme();
		this.refresh();
		return this.mPropertyPanel;
	}
	
	protected void refresh(){
		//VOID
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
