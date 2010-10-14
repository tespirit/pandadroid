package com.tespirit.bamporter.standardEditors;

import java.awt.Component;

import com.tespirit.bamboo.particles.ConstantGravity;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.SimplePanel;

public class ConstantParticleGravity extends ParticleForceEditor{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((ConstantGravity)object);
	}

	@Override
	public Class<?> getDataClass() {
		return ConstantGravity.class;
	}
	
	public class Editor extends ParticleForceEditor.Editor{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7122021845713621470L;
		ConstantGravity mForce;
		
		protected Editor(ConstantGravity force){
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
		return new ConstantGravity();
	}
}
