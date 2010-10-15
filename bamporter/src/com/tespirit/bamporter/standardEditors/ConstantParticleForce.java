package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.ConstantForce;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.Vector3dProperty;

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
			this.addProperty(new Vector3dProperty.Bind("Force", this.mForce.getForce(), 0.1f));
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
		return new ConstantForce();
	}
}
