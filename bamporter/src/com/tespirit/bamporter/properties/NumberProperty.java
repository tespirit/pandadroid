package com.tespirit.bamporter.properties;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class NumberProperty<T extends Number> extends Property<T>{
	private T mMin;
	private T mMax;
	private T mStep;
	
	public NumberProperty(String name, T step, T min, T max){
		super(name);
		this.mMin = min;
		this.mMax = max;
		this.mStep = step;
	}
	
	protected abstract SpinnerNumberModel getNumberModel(T start, T min, T max, T step);
	protected abstract void setValue(SpinnerNumberModel model);

	@Override
	public JComponent getEditor(){
		SpinnerNumberModel model = getNumberModel(this.getValue(), this.mMin, this.mMax, this.mStep);
		JSpinner spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				SpinnerNumberModel model = (SpinnerNumberModel)((JSpinner)e.getSource()).getModel();
				setValue(model);
			}
		});
		return spinner;
	}
}