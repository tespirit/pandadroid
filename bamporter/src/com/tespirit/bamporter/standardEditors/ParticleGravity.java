package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.Gravity;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.FloatProperty;
import com.tespirit.bamporter.properties.Vector3dProperty;

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
			this.addProperty(new Vector3dProperty.Bind("Position", this.mForce.getPosition()));
			this.addProperty(new FloatProperty("Strength", 0.1f){
				@Override
				public void setValue(Float value) {
					mForce.setStrength(value);
				}
				@Override
				public Float getValue() {
					return 	mForce.getStrength();
				}
			});
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
		return new Gravity();
	}
}
