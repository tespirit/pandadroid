package com.tespirit.bamporter.properties;

import javax.swing.SpinnerNumberModel;

public abstract class FloatProperty extends NumberProperty<Float> {
	
	public FloatProperty(String name){
		this(name, 1f);
	}
	
	public FloatProperty(String name, Float step){
		this(name, step, -Float.MAX_VALUE, Float.MAX_VALUE);
	}

	public FloatProperty(String name, Float step, Float min, Float max) {
		super(name, step, min, max);
	}

	@Override
	protected SpinnerNumberModel getNumberModel(Float start, Float min, Float max, Float step) {
		return new SpinnerNumberModel(start, min, max, step);
	}

	@Override
	protected void setValue(SpinnerNumberModel model) {
		this.setValue(model.getNumber().floatValue());
	}
}
