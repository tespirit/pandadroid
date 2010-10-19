package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Matrix3d;

public interface ParticleEmitter <P extends Particle>{
	public Particle newParticle();
	public ParticleSystem getParticleSysetm();
	public ParticleGenerator<P> getParticleGenerator();
	public Matrix3d getWorldTransform();
}
