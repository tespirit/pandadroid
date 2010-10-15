package com.tespirit.bamporter.properties;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tespirit.bamboo.vectors.Range;

public abstract class RangeProperty<T extends Number> extends Property<Range<T>>{
	
	private NumberProperty<T> mMin;
	private NumberProperty<T> mMax;
	
	public RangeProperty(String name, T step, T min, T max){
		super(name);
		this.mMin = createMinProperty(min, max, step);
		this.mMax = createMaxProperty(min, max, step);
	}
	
	protected abstract NumberProperty<T> createMinProperty(T min, T max, T step);
	protected abstract NumberProperty<T> createMaxProperty(T min, T max, T step);

	@Override
	public JComponent getEditor() {
		JPanel panel = new JPanel(new GridLayout(1,3));
		panel.add(this.mMin.getEditor());
		panel.add(new JLabel("to"));
		panel.add(this.mMax.getEditor());
		return panel;
	}

}
