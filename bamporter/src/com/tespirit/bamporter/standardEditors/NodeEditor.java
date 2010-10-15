package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.properties.StringProperty;

public class NodeEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new NodeEditor.Editor((Node)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Node.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		Node mNode;
		
		protected Editor(Node node){
			super(node);
			this.mNode = node;
			
			this.addProperty(new StringProperty("Name"){
				@Override
				public void setValue(String value) {
					mNode.setName(value);
					updateEditor();
				}
				@Override
				public String getValue() {
					return mNode.getName();
				}
				
			});
			
			for(int i = 0; i < node.getChildCount(); i++){
				this.addNewEditor(node.getChild(i));
			}
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
