package com.tespirit.bamporter.editor;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.tespirit.bamboo.particles.RandomParticleGenerator;

public class ParticleEditor extends TreeNodeEditor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8568898146286088966L;
	
	private RandomParticleGenerator mParticles;
	

	public ParticleEditor(RandomParticleGenerator particleProperties){
		this.mParticles = particleProperties;
	}
	
	@Override
	protected Component generatePanel() {
		SimplePanel panel = new SimplePanel();
		
		this.createMinMaxProp(panel, "Birth", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setBirthRateRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Life", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setLifeSpanRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Speed", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setSpeedRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Angle", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setAngleRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Scale", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setScaleRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Decay", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setDecayPercentRange(min, max);
			}
		});
		
		this.createMinMaxProp(panel, "Mass", new PropChange(){
			@Override
			public void set(float min, float max) {
				mParticles.setMassRange(min, max);
			}
		});
		
		return panel;
	}
	
	private interface PropChange{
		void set(float min, float max);
	}
	
	private class UpdateChange implements ChangeListener{
		JSpinner mMin;
		JSpinner mMax;
		PropChange mPropChange;
		
		UpdateChange(JSpinner min, JSpinner max, PropChange propChange){
			this.mMin = min;
			this.mMax = max;
			this.mPropChange = propChange;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			SpinnerNumberModel min = (SpinnerNumberModel)this.mMin.getModel();
			SpinnerNumberModel max = (SpinnerNumberModel)this.mMax.getModel();
			this.mPropChange.set(min.getNumber().floatValue(), max.getNumber().floatValue());
		}
	}
	
	public void createMinMaxProp(SimplePanel panel, String label, PropChange propChange){
		JSpinner min = panel.createFloatSpinner(label+" Min", 0, Float.MAX_VALUE, 0.1f);
		JSpinner max = panel.createFloatSpinner(label+" Max", 0, Float.MAX_VALUE, 0.1f);
		UpdateChange uc = new UpdateChange(min, max, propChange);
		min.addChangeListener(uc);
		max.addChangeListener(uc);
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
