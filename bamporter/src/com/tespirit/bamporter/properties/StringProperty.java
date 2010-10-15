package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public abstract class StringProperty extends Property<String>{
	boolean mReadOnly;

	public StringProperty(String name){
		this(name, false);
	}
	
	public StringProperty(String name, boolean readOnly) {
		super(name);
		this.mReadOnly = readOnly;
	}

	@Override
	public JComponent getEditor() {
		if(this.mReadOnly){
			return new JLabel(this.getValue());
		} else {
			JTextField text = new JTextField();
			text.setText(this.getValue());
			
			text.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					setValue(t.getText());
				}
			});
			
			return text;
		}
	}
	
}