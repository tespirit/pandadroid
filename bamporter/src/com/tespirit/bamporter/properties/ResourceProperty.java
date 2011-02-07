package com.tespirit.bamporter.properties;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.app.Preferences;
import com.tespirit.bamporter.io.FileFilterManager;

public abstract class ResourceProperty extends Property<File>{
	
	private JFileChooser mResourceSelect;
	private JTextField mFile;

	public ResourceProperty(String name) {
		super(name);
		this.mResourceSelect = new JFileChooser();
	}
	
	public ResourceProperty(String name, FileFilterManager fileFilterManager) {
		this(name);
		fileFilterManager.setFilters(this.mResourceSelect);
	}

	@Override
	public JComponent getEditor() {
		JButton button = new JButton("Browse");
		this.mFile = new JTextField();
		Box panel = Box.createHorizontalBox();
		this.mResourceSelect.setCurrentDirectory(Preferences.getSaveDirectory());
		panel.add(this.mFile);
		panel.add(button);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(mResourceSelect.showOpenDialog(BamporterFrame.getInstance()) == JFileChooser.APPROVE_OPTION){
					File file = mResourceSelect.getSelectedFile();
					setValue(file);
					mFile.setText(file.getAbsolutePath());
				}
			}
		});
		
		this.mFile.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(mFile.getText());
				if(file.exists()){
					setValue(file);
				}
			}
		});
		return panel;
	}

}
