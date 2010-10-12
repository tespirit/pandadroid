package com.tespirit.bamporter.properties;

import javax.swing.JSpinner;

import com.tespirit.bamboo.vectors.RandomRange;

public class FloatRangeProperty {
	public static interface Property{
		public void setMin(float min);
		public void setMax(float max);
		public float getMin();
		public float getMax();
	}
	
	private static class RandomRangeProperty implements Property{
		private RandomRange mRange;
		
		private RandomRangeProperty(RandomRange range){
			this.mRange = range;
		}
		
		@Override
		public void setMin(float min) {
			this.mRange.setMin(min);
		}

		@Override
		public void setMax(float max) {
			this.mRange.setMax(max);
		}

		@Override
		public float getMin() {
			return this.mRange.getMin();
		}

		@Override
		public float getMax() {
			return this.mRange.getMax();
		}
		
	}
	
	FloatProperty mMin;
	FloatProperty mMax;
	Property mProperty;
	
	FloatRangeProperty(JSpinner min, JSpinner max, RandomRange range){
		this(min, max, new RandomRangeProperty(range));
	}
	
	FloatRangeProperty(JSpinner min, JSpinner max, Property property){
		this.mProperty = property;
		this.mMin = new FloatProperty(min, new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mProperty.setMin(value);
				mMax.setMinValue(value);
				if(value > mMax.getValue()){
					mMax.setValue(value);
				}
			}
			@Override
			public float getValue() {
				return mProperty.getMin();
			}
		});
		
		this.mMax = new FloatProperty(max, new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mProperty.setMax(value);
			}
			@Override
			public float getValue() {
				return mProperty.getMax();
			}
		});
	}
}
