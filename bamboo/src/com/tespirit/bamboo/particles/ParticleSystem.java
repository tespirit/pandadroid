package com.tespirit.bamboo.particles;

public interface ParticleSystem {
	public void update(float deltaTime);
	public void render();
	public Particle add();
	public void remove(Particle p);
	public void recycle();
	public void init(ParticleEmitter<?> p);
}
