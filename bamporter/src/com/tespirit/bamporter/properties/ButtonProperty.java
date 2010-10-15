package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

public abstract class ButtonProperty extends Property<Object>{
	private String mButtonName;

	public ButtonProperty(String name) {
		super("");
		this.mButtonName = name;
	}

	@Override
	public void setValue(Object value) {
		//VOID
	}
	
	public abstract void onClick();

	@Override
	public Object getValue() {
		return null;
	}

	@Override
	public JComponent getEditor() {
		JButton button = new JButton(this.mButtonName);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				onClick();
			}
		});
		return button;
	}

}
