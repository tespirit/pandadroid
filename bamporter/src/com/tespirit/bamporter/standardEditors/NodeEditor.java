package com.tespirit.bamporter.standardEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.properties.SimplePanel;

public class NodeEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new NodeEditor.Editor((Node)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Node.class;
	}
	
	public class Editor extends TreeNodeEditor{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Node mNode;
		
		protected Editor(Node node){
			super(node);
			this.mNode = node;
			for(int i = 0; i < node.getChildCount(); i++){
				this.addNewEditor(node.getChild(i));
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
		public String getNodeName(){
			return this.mNode.getName();
		}
	
		@Override
		public void recycle() {
			this.mNode.recycle();
			this.mNode = null;
			super.recycle();
		}
	}
}
