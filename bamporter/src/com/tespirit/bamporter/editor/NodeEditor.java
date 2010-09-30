package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.scenegraph.Node;

public class NodeEditor extends TreeNodeEditor{
	Node mNode;
	
	public NodeEditor(Node node){
		this.mNode = node;
		for(int i = 0; i < node.getChildCount(); i++){
			this.add(new NodeEditor(node.getChild(i)));
		}
	}
	
	@Override
	public String toString(){
		return Util.getClassName(this.mNode)+" "+this.mNode.getName();
	}

	@Override
	public Component getEditorPanel() {
		String output = "Bamboo Scene Graph Node Info\n\n";
		output += "Name: " + this.mNode.getName() + "\n";
		output += "Type: " + this.mNode.toString() + "\n";
		
		if(this.mNode.getTransform() != null){
			output += "\nLocalTransform:";
			for(int j = 0; j < 4; j++){
				output+= "\n\t";
				for(int i = 0; i < 4; i++){
					output += this.mNode.getTransform().getValue(j, i) + "\t";
				}
			}
		}
		if(this.mNode.getBoundingBox() != null){
			output += "\nBoundingBox:\n\t";
			for(int i = 0; i < 3; i++){
				output += this.mNode.getBoundingBox().getMin().get(i) + "\t";
			}
			output += "\n\t";
			for(int i = 0; i < 3; i++){
				output += this.mNode.getBoundingBox().getMax().get(i) + "\t";
			}
		}
		EditorPanels.getTextInfo().setText(output);
		return EditorPanels.getTextInfo();
	}

	@Override
	public Component getPropertyPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
