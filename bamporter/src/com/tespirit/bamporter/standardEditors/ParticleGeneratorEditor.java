package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.RandomParticleGenerator;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.FloatProperty;
import com.tespirit.bamporter.properties.FloatRangeProperty;

public class ParticleGeneratorEditor implements Factory{
	
	@Override
	public Editor createEditor(Object object) {
		return new ParticleGeneratorEditor.Editor((RandomParticleGenerator)object);
	}

	@Override
	public Class<?> getDataClass() {
		return RandomParticleGenerator.class;
	}

	public class Editor extends PropertyTreeNodeEditor{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8568898146286088966L;
		
		private RandomParticleGenerator mParticles;
		
	
		public Editor(RandomParticleGenerator particleGenerator){
			super(particleGenerator, false);
			this.mParticles = particleGenerator;
			
			this.addProperty(new FloatProperty("Width", 0.1f, 0f, Float.MAX_VALUE){
				@Override
				public void setValue(Float value) {
					mParticles.setWidth(value);
				}
				@Override
				public Float getValue() {
					return mParticles.getWidth();
				}
			});
			
			this.addProperty(new FloatProperty("Length", 0.1f, 0f, Float.MAX_VALUE){
				@Override
				public void setValue(Float value) {
					mParticles.setLength(value);
				}
				@Override
				public Float getValue() {
					return mParticles.getLength();
				}
			});
			
			this.addProperty(new FloatRangeProperty.Bind("Birth Rate", this.mParticles.getBirthRateRange(), 1f, 0.0f, Float.MAX_VALUE));
			this.addProperty(new FloatRangeProperty.Bind("Life Span", this.mParticles.getLifeSpanRange(), 0.1f, 0.0f, Float.MAX_VALUE));
			this.addProperty(new FloatRangeProperty.Bind("Speed", this.mParticles.getSpeedRange(), 0.5f, 0.0f, Float.MAX_VALUE));
			this.addProperty(new FloatRangeProperty.Bind("Angle", this.mParticles.getAngleRange(), 5.0f, 0.0f, Float.MAX_VALUE));
			this.addProperty(new FloatRangeProperty.Bind("Scale", this.mParticles.getScaleRange(), 0.1f, 0.0f, Float.MAX_VALUE));
			this.addProperty(new FloatRangeProperty.Bind("Decay", this.mParticles.getDecayPercentRange(), 0.1f, 0.0f, 1.0f));
			this.addProperty(new FloatRangeProperty.Bind("Mass", this.mParticles.getMassRange(), 0.05f, 0.0f, Float.MAX_VALUE));
		}
		
		@Override
		public void recycle() {
			this.mParticles = null;
			super.recycle();
		}
		
		@Override
		public String toString(){
			return Util.getClassName(this.mParticles);
		}
	}
}
