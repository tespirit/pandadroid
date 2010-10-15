package com.tespirit.bamporter.properties;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

public class Properties {
	List<Property<?>> mProperties;
	
	public Properties(){
		this.mProperties = new ArrayList<Property<?>>();
	}
	
	public <T extends Property<?>> T addProperty(T property){
		this.mProperties.add(property);
		return property;
	}
	
	public JComponent getEditor(){
		return this.getEditor(false);
	}
	
	public JComponent getEditor(boolean noLabels){
		PropertyPanel panel = new PropertyPanel(noLabels);
		for(Property<?> property : this.mProperties){
			panel.addProperty(property);
		}
		return panel;
	}
}
