package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.Ground;
import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.properties.ButtonProperty;
import com.tespirit.bamporter.properties.FloatProperty;

public class ParticleGroundEditor extends ParticleForceEditor{

	@Override
	public Editor createEditor(Object object) {
		return new Editor((Ground)object);
	}

	@Override
	public Class<?> getDataClass() {
		return Ground.class;
	}
	
	public class Editor extends ParticleForceEditor.Editor{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4071087075565823735L;
		Ground mForce;
		
		protected Editor(Ground force){
			super(force, false);
			this.mForce = force;
			this.addProperty(new FloatProperty("Height", 0.1f){
				@Override
				public void setValue(Float value) {
					mForce.setHeight(value);
				}
				@Override
				public Float getValue() {
					return 	mForce.getHeight();
				}
			});
			this.addProperty(new FloatProperty("Friction", 0.1f){
				@Override
				public void setValue(Float value) {
					mForce.setFriction(value);
				}
				@Override
				public Float getValue() {
					return 	mForce.getFriction();
				}
			});
			this.addProperty(new FloatProperty("Elasticity", 0.1f){
				@Override
				public void setValue(Float value) {
					mForce.setElasticity(value);
				}
				@Override
				public Float getValue() {
					return 	mForce.getElasticity();
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
		return new Ground();
	}

}
