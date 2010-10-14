package com.tespirit.bamporter.editor;

import java.awt.Component;

import com.tespirit.bamboo.particles.ConstantGravityForce;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.properties.SimplePanel;

public class ConstantParticleGravity extends ParticleForceEditor{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((ConstantGravityForce)object);
	}

	@Override
	public Class<?> getDataClass() {
		return ConstantGravityForce.class;
	}
	
	public class Editor extends ParticleForceEditor.Editor{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7122021845713621470L;
		ConstantGravityForce mForce;
		
		protected Editor(ConstantGravityForce force){
			super(force, false);
			this.mForce = force;
		}

		@Override
		protected Component generatePanel() {
			SimplePanel panel = new SimplePanel();
			panel.addProperty("Acceleration", this.mForce.getAcceleration(), 0.1f);
			return panel;
		}
	}

	@Override
	public ParticleForce createForce() {
		return new ConstantGravityForce();
	}
}
