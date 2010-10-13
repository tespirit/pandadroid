package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.particles.RandomParticleGenerator;
import com.tespirit.bamporter.properties.FloatProperty;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleGeneratorEditor extends TreeNodeEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8568898146286088966L;
	
	private RandomParticleGenerator mParticles;
	

	public ParticleGeneratorEditor(RandomParticleGenerator particleProperties){
		this.mParticles = particleProperties;
	}
	
	@Override
	protected Component generatePanel() {
		SimplePanel panel = new SimplePanel();
		
		panel.addProperty("Width", new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mParticles.setWidth(value);
			}
			
			@Override
			public float getValue() {
				return mParticles.getWidth();
			}
		}, 0, Float.MAX_VALUE, 0.1f);
		
		panel.addProperty("Length", new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mParticles.setLength(value);
			}
			
			@Override
			public float getValue() {
				return mParticles.getLength();
			}
		}, 0, Float.MAX_VALUE, 0.1f);
		
		panel.addProperty("Birth Rate", this.mParticles.getBirthRateRange(), 0.0f, Float.MAX_VALUE, 1.0f);
		panel.addProperty("Life Span", this.mParticles.getLifeSpanRange(), 0.0f, Float.MAX_VALUE, 0.1f);
		panel.addProperty("Speed", this.mParticles.getSpeedRange(), 0.0f, Float.MAX_VALUE, 0.5f);
		panel.addProperty("Angle", this.mParticles.getAngleRange(), 0.0f, Float.MAX_VALUE, 5.0f);
		panel.addProperty("Scale", this.mParticles.getScaleRange(), 0.0f, Float.MAX_VALUE, 0.1f);
		panel.addProperty("Decay", this.mParticles.getDecayPercentRange(), 0.0f, 1.0f, 0.1f);
		panel.addProperty("Mass", this.mParticles.getMassRange(), 0.0f, Float.MAX_VALUE, 0.05f);
		
		return panel;
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
