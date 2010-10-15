package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamporter.properties.Properties;
import com.tespirit.bamporter.properties.Property;

public class PropertyTreeNodeEditor extends TreeNodeEditor{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8894737398871528936L;
	private Properties mProperties;
	private boolean mNoLabels;

	protected PropertyTreeNodeEditor(Object data) {
		this(data, false);
	}
	
	protected PropertyTreeNodeEditor(Object data, Boolean noLabels){
		super(data);
		this.mProperties = new Properties();
		this.mNoLabels = noLabels;
	}

	@Override
	protected Component generatePanel() {
		this.initPanel();
		return this.mProperties.getEditor(this.mNoLabels);
	}
	
	protected void initPanel(){
		//VOID
	}
	
	protected <T extends Property<?>> T addProperty(T property){
		return this.mProperties.addProperty(property);
	}

}
