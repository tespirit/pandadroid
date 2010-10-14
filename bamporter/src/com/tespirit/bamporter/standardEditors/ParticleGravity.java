package com.tespirit.bamporter.standardEditors;

import java.awt.Component;

import com.tespirit.bamboo.particles.Gravity;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.FloatProperty;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleGravity extends ParticleForceEditor{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((Gravity)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Gravity.class;
	}
	
	public class Editor extends ParticleForceEditor.Editor{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4071087075565823735L;
		Gravity mForce;
		
		protected Editor(Gravity force){
			super(force, false);
			this.mForce = force;
		}

		@Override
		protected Component generatePanel() {
			SimplePanel panel = new SimplePanel();
			panel.addProperty("Position", this.mForce.getPosition(), 1f);
			panel.addProperty("Strength", new FloatProperty.Property() {
				@Override
				public void setValue(float value) {
					mForce.setStrength(value);
				}
				@Override
				public float getValue() {
					return 	mForce.getStrength();
				}
			}, -Float.MAX_VALUE, Float.MAX_VALUE, 0.1f);
			return panel;
		}
	}

	@Override
	public ParticleForce createForce() {
		return new Gravity();
	}
}
