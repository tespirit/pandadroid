package com.tespirit.bamporter.properties;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LongProperty implements ChangeListener{
	public static interface Property{
		public void setValue(long value);
		public long getValue();
	}
	private JSpinner mValue;
	private Property mProperty;
	
	LongProperty(JSpinner value, Property property){
		this.mProperty = property;
		this.mValue = value;
		SpinnerNumberModel numVal = (SpinnerNumberModel)this.mValue.getModel();
		numVal.setValue(property.getValue());
		this.mValue.addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		this.mProperty.setValue(value.getNumber().longValue());
	}
	
	public void setMinValue(long min){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setMinimum(new Double(min));
	}
	
	public void setMaxValue(long max){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setMinimum(new Double(max));
	}
	
	public long getValue(){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		return value.getNumber().longValue();
	}
	
	public void setValue(long val){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setValue(new Double(val));
	}
}
