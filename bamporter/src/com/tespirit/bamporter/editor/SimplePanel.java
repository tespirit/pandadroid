package com.tespirit.bamporter.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
	private JPanel mLabelPanel;
	private JPanel mInputPanel;
	
	private static final Dimension mInputSizeMin = new Dimension(50,25);
	private static final Dimension mInputSizePref = new Dimension(150,25);
	private static final Dimension mInputSizeMax = new Dimension(1000, 25);
	private static final Dimension mLabelSizeMin = new Dimension(50,25);
	private static final Dimension mLabelSizePref = new Dimension(75,25);
	private static final Dimension mLabelSizeMax = new Dimension(1000, 25);
	
	public SimplePanel() {
		this.setLayout(new BorderLayout());
		this.mLabelPanel = new JPanel(new GridLayout(0,1));
		this.mInputPanel = new JPanel(new GridLayout(0,1));
		this.add(this.mLabelPanel, BorderLayout.WEST);
	    this.add(this.mInputPanel, BorderLayout.CENTER);
	}
	
	private void addRow(String label, Component input){
		JLabel labelField = new JLabel(label);
		labelField.setMinimumSize(mLabelSizeMin);
		labelField.setPreferredSize(mLabelSizePref);
		labelField.setMaximumSize(mLabelSizeMax);
		input.setMinimumSize(mInputSizeMin);
		input.setPreferredSize(mInputSizePref);
		input.setMaximumSize(mInputSizeMax);
		this.mLabelPanel.add(labelField);
		this.mInputPanel.add(input);
	}

	public JButton addButton(String text){
		JButton button = new JButton(text);
		this.addRow("", button);
		return button;
	}
	
	public JToggleButton addToggleButton(String text){
		JToggleButton button = new JToggleButton(text);
		this.addRow("", button);
		return button;
	}
	
	public JTextField addTextField(String label){
		JTextField text = new JTextField();
		this.addRow(label, text);
		return text;
	}
	
	public JComboBox addComboBox(String label){
		JComboBox combo = new JComboBox();
		this.addRow(label, combo);
		return combo;
	}
	
	public JSpinner addLongSpinner(String label, long min, long max, long step){
		JSpinner spinner = new JSpinner();
		this.addRow(label, spinner);
		SpinnerModel model = new SpinnerNumberModel(0, min, max, step);
		spinner.setModel(model);
		return spinner;
	}
}
