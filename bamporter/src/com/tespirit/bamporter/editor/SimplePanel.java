package com.tespirit.bamporter.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class SimplePanel extends JPanel{
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
	
	public SimplePanel() {
		this.mLabelPanel = Box.createVerticalBox();
		this.mInputPanel = Box.createVerticalBox();
		
	    this.setLayout(new BorderLayout());
	    this.add(this.mLabelPanel, BorderLayout.WEST);
	    this.add(this.mInputPanel, BorderLayout.CENTER);
	}
	
	public void addComponent(String label, JComponent input){
		JLabel labelField = new JLabel(label);
		labelField.setMinimumSize(mLabelSizeMin);
		labelField.setPreferredSize(mLabelSizePref);
		labelField.setMaximumSize(mLabelSizeMax);
		input.setMinimumSize(mInputSizeMin);
		input.setPreferredSize(mInputSizePref);
		input.setMaximumSize(mInputSizeMax);
		
		labelField.setAlignmentX(RIGHT_ALIGNMENT);
		input.setAlignmentX(LEFT_ALIGNMENT);
		
		this.mLabelPanel.add(labelField);
		this.mInputPanel.add(input);
		this.mLabelPanel.add(Box.createVerticalStrut(5));
		this.mInputPanel.add(Box.createVerticalStrut(5));
	}

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
		JSpinner spinner = new JSpinner();
		this.addComponent(label, spinner);
		SpinnerModel model = new SpinnerNumberModel(0, min, max, step);
		spinner.setModel(model);
		return spinner;
	}
}
