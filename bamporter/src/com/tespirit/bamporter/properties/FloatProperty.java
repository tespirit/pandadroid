package com.tespirit.bamporter.properties;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FloatProperty implements ChangeListener{
	public static interface Property{
		public void setValue(float value);
		public float getValue();
	}
	private JSpinner mValue;
	private Property mProperty;
	
	FloatProperty(JSpinner value, Property property){
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
		this.mProperty.setValue(value.getNumber().floatValue());
	}
	
	public void setMinValue(float min){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setMinimum(new Double(min));
	}
	
	public void setMaxValue(float max){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setMinimum(new Double(max));
	}
	
	public float getValue(){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		return value.getNumber().floatValue();
	}
	
	public void setValue(float val){
		SpinnerNumberModel value = (SpinnerNumberModel)this.mValue.getModel();
		value.setValue(new Double(val));
	}
}
