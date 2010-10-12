package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class StringProperty implements ActionListener{
	public interface Property{
		public void setValue(String value);
		public String getValue();
	}
	
	JTextField mValue;
	Property mProperty;
	
	StringProperty(JTextField value, Property property){
		this.mValue = value;
		this.mProperty = property;
		this.mValue.setText(this.mProperty.getValue());
		this.mValue.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.mProperty.setValue(this.mValue.getText());
	}
}
