package com.tespirit.bamporter.properties;

import javax.swing.JSpinner;

public class LongRangeProperty {
	public static interface Property{
		public void setMin(long min);
		public void setMax(long max);
		public long getMin();
		public long getMax();
	}

	LongProperty mMin;
	LongProperty mMax;
	Property mProperty;
	
	LongRangeProperty(JSpinner min, JSpinner max, Property property){
		this.mProperty = property;
		this.mMin = new LongProperty(min, new LongProperty.Property() {
			@Override
			public void setValue(long value) {
				mProperty.setMin(value);
				mMax.setMinValue(value);
				if(value > mMax.getValue()){
					mMax.setValue(value);
				}
			}
			@Override
			public long getValue() {
				return mProperty.getMin();
			}
		});
		
		this.mMax = new LongProperty(max, new LongProperty.Property() {
			@Override
			public void setValue(long value) {
				mProperty.setMax(value);
			}
			@Override
			public long getValue() {
				return mProperty.getMax();
			}
		});
	}
}
