package com.tespirit.bamporter.standardEditors;

import java.util.List;

import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamboo.particles.StandardParticleSystem;
import com.tespirit.bamporter.editor.EditorFactory;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.editor.PropertyTreeNodeEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.ButtonProperty;

public class ParticleSystemEditor implements Factory{
	@Override
	public Editor createEditor(Object object) {
		return new ParticleSystemEditor.Editor((StandardParticleSystem)object);
	}

	@Override
	public Class<?> getDataClass() {
		return StandardParticleSystem.class;
	}
	
	public class Editor extends PropertyTreeNodeEditor{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4329218305403186171L;
		private StandardParticleSystem mParticleSystem;
		
		private class ForceButton extends ButtonProperty{
			ParticleForceEditor mEditor;
			
			public ForceButton(Factory factory) {
				super(Util.getClassName(factory.getDataClass()));
				this.mEditor = (ParticleForceEditor)factory;
			}

			@Override
			public void onClick() {
				addForce(this.mEditor.createForce());
			}
			
		}
		
		public Editor(StandardParticleSystem particleSystem){
			super(particleSystem, true);
			this.mParticleSystem = particleSystem;
			
			List<Factory> factories = EditorFactory.getFactoriesOf(ParticleForce.class);
			for(Factory ef : factories){
				this.addProperty(new ForceButton(ef));
			}
		}
		
		public void addForce(ParticleForce force){
			this.mParticleSystem.addForce(force);
			this.addEditor(force);
		}
		
		@Override
		public void removeEditor(TreeNodeEditor node){
			super.removeEditor(node);
			if(node instanceof ParticleForceEditor.Editor){
				this.mParticleSystem.removeForce(((ParticleForceEditor.Editor)node).getForce());
			}
		}
	}
}
