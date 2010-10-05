package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.util.Enumeration;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.AxisAlignedBox;

public class NodeEditor extends TreeNodeEditor{
	Node mNode;
	RenderManager mRenderManager;
	NodeEditorPanel mProperties;
	
	public NodeEditor(Node node, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mNode = node;
		for(int i = 0; i < node.getChildCount(); i++){
			this.add(new NodeEditor(node.getChild(i), renderManager));
		}
		this.mProperties = new NodeEditorPanel();
		
		if(this.mNode.getName() != null){
			this.mProperties.mName.setText(this.mNode.getName());
		}
		AxisAlignedBox bb = this.mNode.getBoundingBox();
		if(bb == null){
			this.mProperties.mMaxX.setEnabled(false);
			this.mProperties.mMaxY.setEnabled(false);
			this.mProperties.mMaxZ.setEnabled(false);
			this.mProperties.mMinX.setEnabled(false);
			this.mProperties.mMinY.setEnabled(false);
			this.mProperties.mMinZ.setEnabled(false);
		} else {
			this.mProperties.mMaxX.getModel().setValue(bb.getMax().getX());
			this.mProperties.mMaxY.getModel().setValue(bb.getMax().getY());
			this.mProperties.mMaxZ.getModel().setValue(bb.getMax().getZ());
			this.mProperties.mMinX.getModel().setValue(bb.getMin().getX());
			this.mProperties.mMinY.getModel().setValue(bb.getMin().getY());
			this.mProperties.mMinZ.getModel().setValue(bb.getMin().getZ());
		}
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mNode)+" "+this.mNode.getName();
	}

	@Override
	public Component getPropertyPanel() {
		return this.mProperties;
	}

	@Override
	public void recycle() {
		this.mNode.recycle();
		this.mNode = null;
		for(Enumeration<TreeNodeEditor> children = this.children(); children.hasMoreElements();){
			children.nextElement().recycle();
		}
	}
}
