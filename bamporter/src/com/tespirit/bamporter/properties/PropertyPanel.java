package com.tespirit.bamporter.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PropertyPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4519237203166366514L;
	private Box mLabelPanel;
	private Box mInputPanel;
	
	private static final Dimension mInputSizeMin = new Dimension(50,25);
	private static final Dimension mInputSizePref = new Dimension(100,25);
	private static final Dimension mInputSizeMax = new Dimension(1000, 25);
	private static final Dimension mLabelSizeMin = new Dimension(50,25);
	private static final Dimension mLabelSizePref = new Dimension(75,25);
	private static final Dimension mLabelSizeMax = new Dimension(1000, 25);
	
	public PropertyPanel() {
		this(false);
	}
	
	public PropertyPanel(boolean noLabels){
		if(noLabels){
			this.mInputPanel = Box.createVerticalBox();
		
			this.setLayout(new GridLayout());
			this.add(this.mInputPanel);
		} else {
			this.mLabelPanel = Box.createVerticalBox();
			this.mInputPanel = Box.createVerticalBox();
			
		    this.setLayout(new BorderLayout());
		    this.add(this.mLabelPanel, BorderLayout.WEST);
		    this.add(this.mInputPanel, BorderLayout.CENTER);
		}
	}
	
	public void addComponent(String label, JComponent input){
		if(this.mLabelPanel != null){
			JLabel labelField = new JLabel(label);
			labelField.setMinimumSize(mLabelSizeMin);
			labelField.setPreferredSize(mLabelSizePref);
			labelField.setMaximumSize(mLabelSizeMax);
			labelField.setAlignmentX(RIGHT_ALIGNMENT);
			this.mLabelPanel.add(labelField);
			this.mLabelPanel.add(Box.createVerticalStrut(5));
		}
		input.setAlignmentX(LEFT_ALIGNMENT);
		input.setMinimumSize(mInputSizeMin);
		input.setPreferredSize(mInputSizePref);
		input.setMaximumSize(mInputSizeMax);
		this.mInputPanel.add(input);
		this.mInputPanel.add(Box.createVerticalStrut(5));
	}
	
	public void addProperty(Property<?> property){
		this.addComponent(property.getName(), property.getEditor());
	}
/*
	public JLabel createLabel(String label, String value){
		JLabel valueLabel = new JLabel(value);
		this.addComponent(label, valueLabel);
		return valueLabel;
	}
	
	public JButton createButton(String text){
		JButton button = new JButton(text);
		this.addComponent("", button);
		return button;
	}
	
	public JToggleButton createToggleButton(String text){
		JToggleButton button = new JToggleButton(text);
		this.addComponent("", button);
		return button;
	}
	
	public JTextField createTextField(String label){
		JTextField text = new JTextField();
		this.addComponent(label, text);
		return text;
	}
	
	public JComboBox createComboBox(String label){
		JComboBox combo = new JComboBox();
		this.addComponent(label, combo);
		return combo;
	}
	
	public JSpinner createLongSpinner(String label, long min, long max, long step){
		JSpinner spinner = this.newLongSpinner(min, max, step);
		this.addComponent(label, spinner);
		return spinner;
	}
	
	public JSpinner createFloatSpinner(String label, float min, float max, float step){
		JSpinner spinner = this.newFloatSpinner(min, max, step);
		this.addComponent(label, spinner);
		return spinner;
	}
	
	private JSpinner newLongSpinner(long min, long max, long step){
		JSpinner spinner = new JSpinner();
		SpinnerModel model = new SpinnerNumberModel(0, min, max, step);
		spinner.setModel(model);
		return spinner;
	}
	
	private JSpinner newFloatSpinner(float min, float max, float step){
		JSpinner spinner = new JSpinner();
		SpinnerModel model = new SpinnerNumberModel(0, min, max, step);
		spinner.setModel(model);
		return spinner;
	}
	
	public void addProperty(String label, FloatProperty.Property property, float min, float max, float step){
		new FloatProperty(this.createFloatSpinner(label, min, max, step), property);
	}
	
	public void addProperty(String label, FloatRangeProperty.Property property, float min, float max, float step){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JSpinner minSpinner = this.newFloatSpinner(min, max, step);
		JSpinner maxSpinner = this.newFloatSpinner(min, max, step);
		panel.add(minSpinner);
		JLabel to = new JLabel("to");
		to.setAlignmentX(0.5f);
		panel.add(maxSpinner);
		new FloatRangeProperty(minSpinner, maxSpinner, property);
		this.addComponent(label, panel);
	}
	
	public void addProperty(String label, RandomRange range, float min, float max, float step){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JSpinner minSpinner = this.newFloatSpinner(min, max, step);
		JSpinner maxSpinner = this.newFloatSpinner(min, max, step);
		panel.add(minSpinner);
		JLabel to = new JLabel("to");
		to.setAlignmentX(0.5f);
		panel.add(to);
		panel.add(maxSpinner);
		new FloatRangeProperty(minSpinner, maxSpinner, range);
		this.addComponent(label, panel);
	}
	
	public void addProperty(String label, LongProperty.Property property, long min, long max, long step){
		new LongProperty(this.createLongSpinner(label, min, max, step), property);
	}
	
	public void addProperty(String label, LongRangeProperty.Property property, long min, long max, long step){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JSpinner minSpinner = this.newLongSpinner(min, max, step);
		JSpinner maxSpinner = this.newLongSpinner(min, max, step);
		panel.add(minSpinner);
		JLabel to = new JLabel("to");
		to.setAlignmentX(0.5f);
		panel.add(maxSpinner);
		new LongRangeProperty(minSpinner, maxSpinner, property);
		this.addComponent(label, panel);
	}
	
	public void addProperty(String label, StringProperty.Property property){
		
	}
	
	public void addProperty(String label, Vector3dProperty.Property property, float step){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JSpinner x = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		JSpinner y = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		JSpinner z = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		panel.add(x);
		panel.add(y);
		panel.add(z);
		new Vector3dProperty(x,y,z,property);
		this.addComponent(label, panel);
	}
	
	public void addProperty(String label, Vector3d vector, float step){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,3));
		JSpinner x = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		JSpinner y = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		JSpinner z = this.newFloatSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, step);
		panel.add(x);
		panel.add(y);
		panel.add(z);
		new Vector3dProperty(x,y,z,vector);
		this.addComponent(label, panel);
	}*/
}
