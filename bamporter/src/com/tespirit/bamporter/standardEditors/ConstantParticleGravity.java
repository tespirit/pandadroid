package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.ConstantGravity;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.Vector3dProperty;

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
			this.addProperty(new Vector3dProperty.Bind("Acceleration", this.mForce.getAcceleration(), 0.1f));
			this.addProperty(new ButtonProperty("Delete"){
				@Override
				public void onClick() {
					removeEditorFromParent();
				}
			});
		}
	}

	@Override
	public ParticleForce createForce() {
		return new ConstantGravity();
	}
}
