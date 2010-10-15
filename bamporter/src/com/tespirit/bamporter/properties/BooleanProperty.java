package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

public abstract class BooleanProperty extends Property<Boolean>{
	private boolean mUseButton;
	private String mName;

	public BooleanProperty(String name) {
		super("");
		this.mName = name;
	}
	
	public BooleanProperty(String name, boolean useButton){
		this(name);
		this.mUseButton = useButton;
	}

	@Override
	public JComponent getEditor() {
		if(this.mUseButton){
			JToggleButton button = new JToggleButton(this.mName);
			button.getModel().setSelected(this.getValue());
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					JToggleButton button = (JToggleButton)e.getSource();
					setValue(button.getModel().isSelected());
				}
			});
			return button;
		} else {
			JCheckBox button = new JCheckBox();
			button.getModel().setSelected(this.getValue());
			button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox button = (JCheckBox)e.getSource();
					setValue(button.getModel().isSelected());
				}
			});
			return null;
		}
	}

}
