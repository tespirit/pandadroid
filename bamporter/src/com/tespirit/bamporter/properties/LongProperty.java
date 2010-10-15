package com.tespirit.bamporter.properties;

import javax.swing.SpinnerNumberModel;


public abstract class LongProperty extends NumberProperty<Long> {
	
	public LongProperty(String name){
		this(name, 1L);
	}
	
	public LongProperty(String name, Long step){
		this(name, step, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public LongProperty(String name, Long step, Long min, Long max) {
		super(name, step, min, max);
	}

	@Override
	protected SpinnerNumberModel getNumberModel(Long start, Long min, Long max, Long step) {
		return new SpinnerNumberModel(start, min, max, step);
	}

	@Override
	protected void setValue(SpinnerNumberModel model) {
		this.setValue(model.getNumber().longValue());
	}
}