package com.tespirit.bamporter.properties;

import com.tespirit.bamboo.vectors.Range;

public abstract class LongRangeProperty extends RangeProperty<Long>{
	
	public LongRangeProperty(String name){
		this(name, 1L);
	}
	
	public LongRangeProperty(String name, Long step){
		this(name, step, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public LongRangeProperty(String name, Long step, Long min, Long max) {
		super(name, step, min, max);
	}

	@Override
	protected NumberProperty<Long> createMinProperty(Long min, Long max, Long step) {
		return new LongProperty("min", step, min, max){
			@Override
			public void setValue(Long value) {
				Range<Long> r = LongRangeProperty.this.getValue();
				r.setMin(value);
				LongRangeProperty.this.setValue(r);
			}

			@Override
			public Long getValue() {
				return LongRangeProperty.this.getValue().getMin();
			}
		};
	}
	
	@Override
	protected NumberProperty<Long> createMaxProperty(Long min, Long max, Long step) {
		return new LongProperty("max", step, min, max){
			@Override
			public void setValue(Long value) {
				Range<Long> r = LongRangeProperty.this.getValue();
				r.setMax(value);
				LongRangeProperty.this.setValue(r);
			}

			@Override
			public Long getValue() {
				return LongRangeProperty.this.getValue().getMax();
			}
		};
	}
}