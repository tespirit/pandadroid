package com.tespirit.bamporter.standardEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.tespirit.bamboo.particles.ParticleForce;
import com.tespirit.bamboo.particles.StandardParticleSystem;
import com.tespirit.bamporter.editor.EditorFactory;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.ParticleForceEditor;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.SimplePanel;

public class ParticleSystemEditor implements Factory{
	@Override
	public Editor createEditor(Object object) {
		return new ParticleSystemEditor.Editor((StandardParticleSystem)object);
	}

	@Override
	public Class<?> getDataClass() {
		return StandardParticleSystem.class;
	}
	
	public class Editor extends TreeNodeEditor{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4329218305403186171L;
		private StandardParticleSystem mParticleSystem;
		
		private class OnNewForce implements ActionListener{
			ParticleForceEditor mEditor;
			private OnNewForce(Factory factory){
				this.mEditor = (ParticleForceEditor)factory;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				addForce(this.mEditor.createForce());
			}
		}
		
		public Editor(StandardParticleSystem particleSystem){
			super(particleSystem);
			this.mParticleSystem = particleSystem;
		}
	
		@Override
		protected Component generatePanel() {
			SimplePanel panel = new SimplePanel();
			
			List<Factory> factories = EditorFactory.getEditorFactoriesOf(ParticleForceEditor.class);
			for(Factory ef : factories){
				panel.createButton("New " + Util.getClassName(ef.getDataClass())).addActionListener(new OnNewForce(ef));
			}
			return panel;
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
