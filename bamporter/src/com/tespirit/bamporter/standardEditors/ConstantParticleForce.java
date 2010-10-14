package com.tespirit.bamporter.standardEditors;

import java.awt.Component;

import com.tespirit.bamboo.particles.ConstantForce;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.SimplePanel;

public class ConstantParticleForce extends ParticleForceEditor{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((ConstantForce)object);
	}

	@Override
	public Class<?> getDataClass() {
		return ConstantForce.class;
	}
	
	public class Editor extends ParticleForceEditor.Editor{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6061636281807289190L;
		
		ConstantForce mForce;
		
		protected Editor(ConstantForce force){
			super(force, false);
			this.mForce = force;
		}

		@Override
		protected Component generatePanel() {
			SimplePanel panel = new SimplePanel();
			panel.addProperty("Force",this.mForce.getForce(), 0.1f);
			return panel;
		}
		
	}

	@Override
	public ParticleForce createForce() {
		return new ConstantForce();
	}
}
