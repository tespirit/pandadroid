package com.tespirit.bamporter.properties;

import com.tespirit.bamboo.vectors.Range;

public abstract class FloatRangeProperty extends RangeProperty<Float>{
	
	public static class Bind extends FloatRangeProperty{
		private Range<Float> mRange;
		public Bind(String name, Range<Float> range){
			this(name, range, 1f);
		}

		public Bind(String name, Range<Float> range, Float step){
			this(name, range, step, -Float.MAX_VALUE, Float.MAX_VALUE);
		}
		
		public Bind(String name, Range<Float> range, Float step, Float min, Float max) {
			super(name, step, min, max);
			this.mRange = range;
		}

		@Override
		public void setValue(Range<Float> value) {
			// VOID
		}

		@Override
		public Range<Float> getValue() {
			return this.mRange;
		}
		
		
	}
	
	public FloatRangeProperty(String name){
		this(name, 1f);
	}
	
	public FloatRangeProperty(String name, Float step){
		this(name, step, -Float.MAX_VALUE, Float.MAX_VALUE);
	}

	public FloatRangeProperty(String name, Float step, Float min, Float max) {
		super(name, step, min, max);
	}

	@Override
	protected NumberProperty<Float> createMinProperty(Float min, Float max, Float step) {
		return new FloatProperty("min", step, min, max){
			@Override
			public void setValue(Float value) {
				Range<Float> r = FloatRangeProperty.this.getValue();
				r.setMin(value);
				FloatRangeProperty.this.setValue(r);
			}

			@Override
			public Float getValue() {
				return FloatRangeProperty.this.getValue().getMin();
			}
		};
	}
	
	@Override
	protected NumberProperty<Float> createMaxProperty(Float min, Float max, Float step) {
		return new FloatProperty("max", step, min, max){
			@Override
			public void setValue(Float value) {
				Range<Float> r = FloatRangeProperty.this.getValue();
				r.setMax(value);
				FloatRangeProperty.this.setValue(r);
			}

			@Override
			public Float getValue() {
				return FloatRangeProperty.this.getValue().getMax();
			}
		};
	}
}