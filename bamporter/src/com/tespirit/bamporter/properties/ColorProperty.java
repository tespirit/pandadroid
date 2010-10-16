package com.tespirit.bamporter.properties;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;

import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamporter.app.BamporterFrame;

public abstract class ColorProperty extends Property<Color4>{
	private String mName;
	
	public static class Bind extends ColorProperty{

		private Color4 mColor;
		
		public Bind(String name, Color4 color) {
			super(name);
			this.mColor = color;
		}

		@Override
		public void setValue(Color4 value) {
			//VOID
		}

		@Override
		public Color4 getValue() {
			return mColor;
		}
	}

	public ColorProperty(String name) {
		super("");
		this.mName = name;
	}

	@Override
	public JComponent getEditor() {
		JButton button = new JButton(this.mName);
		
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Color4 source = getValue();
				Color color = new Color(source.getRed(), source.getGreen(), source.getBlue(), source.getAlpha());
				color = JColorChooser.showDialog(BamporterFrame.getInstance(), "Pick a color", color);
				if(color != null){
					source.set(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
					setValue(source);
				}
			}
		});
		
		return button;
	}
}
