package com.tespirit.bamporter.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;


import com.tespirit.bamboo.particles.ParticleEmitter;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.properties.SimplePanel;

public class NodeEditor extends TreeNodeEditor{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Node mNode;
	RenderManager mRenderManager;
	
	public NodeEditor(Node node, RenderManager renderManager){
		this.mRenderManager = renderManager;
		this.mNode = node;
		for(int i = 0; i < node.getChildCount(); i++){
			this.addNewEditor(new NodeEditor(node.getChild(i), renderManager));
		}
		if(node instanceof ParticleEmitter){
			BamporterFrame.getInstance().registerParticles((ParticleEmitter)node);
		}
	}
	
	@Override
	protected Component generatePanel(){
		SimplePanel panel = new SimplePanel();
		JTextField name = panel.createTextField("Name");
		if(this.mNode.getName() != null){
			name.setText(this.mNode.getName());
		}
		
		name.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mNode.setName(((JTextField)e.getSource()).getText());
				updateEditor();
			}
		});
		return panel;
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mNode)+" "+this.mNode.getName();
	}

	@Override
	public void recycle() {
		this.mNode.recycle();
		this.mNode = null;
		super.recycle();
	}
}
