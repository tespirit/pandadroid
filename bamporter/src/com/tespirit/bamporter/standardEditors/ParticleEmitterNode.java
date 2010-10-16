package com.tespirit.bamporter.standardEditors;

import com.tespirit.bamboo.particles.SpriteParticleEmitter;
import com.tespirit.bamporter.app.BamporterFrame;

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

		protected Editor(SpriteParticleEmitter emitter) {
			super(emitter);
			this.addEditor(emitter.getSurface());
		}
		
	}
}
