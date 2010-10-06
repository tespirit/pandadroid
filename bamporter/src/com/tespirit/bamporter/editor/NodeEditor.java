package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JTextField;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;

public class NodeEditor extends TreeNodeEditor{
	Node mNode;
	RenderManager mRenderManager;
	SimplePanel mPropertyPanel;
	
	public NodeEditor(Node node, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mNode = node;
		for(int i = 0; i < node.getChildCount(); i++){
			this.add(new NodeEditor(node.getChild(i), renderManager));
		}
	}
	
	private void generatePanel(){
		this.mPropertyPanel = new SimplePanel();
		JTextField name = this.mPropertyPanel.createTextField("Name");
		if(this.mNode.getName() != null){
			name.setText(this.mNode.getName());
		}
		
		name.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mNode.setName(((JTextField)e.getSource()).getText());
			}
		});
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mNode)+" "+this.mNode.getName();
	}

	@Override
	public Component getPropertyPanel() {
		if(this.mPropertyPanel == null){
			this.generatePanel();
		}
		return this.mPropertyPanel;
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
