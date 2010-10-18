package com.tespirit.bamporter.standardEditors;

import java.util.ArrayList;
import java.util.List;

import com.tespirit.bamboo.particles.SpriteParticleEmitter;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamporter.app.BamporterFrame;
import com.tespirit.bamporter.editor.EditorFactory;
import com.tespirit.bamporter.editor.Factory;
import com.tespirit.bamporter.editor.TreeNodeEditor;
import com.tespirit.bamporter.editor.Util;
import com.tespirit.bamporter.properties.ButtonProperty;

public class ParticleEmitterNode extends NodeEditor{
	@Override
	public Editor createEditor(Object object) {
		BamporterFrame.getInstance().registerParticles((SpriteParticleEmitter)object);
		return new Editor((SpriteParticleEmitter)object);
	}

	@Override
	public Class<?> getDataClass() {
		return SpriteParticleEmitter.class;
	}
	
	public class Editor extends NodeEditor.Editor{

		/**
		 * 
		 */
		private static final long serialVersionUID = -2776741377357784920L;
		
		private class FactoryNode{
			Factory mFactory;
			private FactoryNode(Factory factory){
				this.mFactory = factory;
			}
			
			@Override
			public String toString(){
				return Util.getClassName(this.mFactory.getDataClass());
			}
		}
		
		private List<FactoryNode> mSurfaceFactories;
		private TreeNodeEditor mSurfaceEditor;
		private SpriteParticleEmitter mEmitter;
		
		protected Editor(SpriteParticleEmitter emitter) {
			super(emitter);
			this.mEmitter = emitter;
			this.mSurfaceEditor = (TreeNodeEditor)this.addEditor(emitter.getSurface());
			List<Factory> factories = EditorFactory.getFactoriesOf(Surface.class, false);
			this.mSurfaceFactories = new ArrayList<FactoryNode>(factories.size());
			for(Factory ef : factories){
				this.mSurfaceFactories.add(new FactoryNode(ef));
			}
			this.addProperty(new ButtonProperty("Change Surface"){
				@Override
				public void onClick() {
					FactoryNode nodey = Util.promptChoice("What surface would you like?", mSurfaceFactories, mSurfaceFactories.get(0));
					if(mSurfaceEditor != null){
						mSurfaceEditor.removeEditorFromParent();
					}
					try {
						Surface surface = (Surface)nodey.mFactory.getDataClass().newInstance();
						mEmitter.setSurface(surface);
						mSurfaceEditor = (TreeNodeEditor)addEditor(surface);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}
}
