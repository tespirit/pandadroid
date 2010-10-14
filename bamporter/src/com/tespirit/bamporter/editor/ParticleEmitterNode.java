package com.tespirit.bamporter.editor;

import com.tespirit.bamboo.particles.SpriteParticleEmitter;
import com.tespirit.bamporter.app.BamporterFrame;

public class ParticleEmitterNode extends NodeEditor{
	@Override
	public Editor createEditor(Object object) {
		BamporterFrame.getInstance().registerParticles((SpriteParticleEmitter)object);
		return super.createEditor(object);
	}

	@Override
	public Class<?> getDataClass() {
		return SpriteParticleEmitter.class;
	}
}
