package com.tespirit.bamporter.properties;

import javax.swing.JComponent;

public abstract class Property<T> {
	private String mName;
	
	public Property(String name){
		this.mName = name;
	}

	public String getName(){
		return this.mName;
	}
	
	public abstract void setValue(T value);
	public abstract T getValue();
	public abstract JComponent getEditor();
	
}
