package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.vectors.Matrix3d;

public interface ParticleEmitter {
	public Particle createParticle();
	public ParticleSystem getParticleSysetm();
	public Matrix3d getWorldTransform();
}
