package com.tespirit.bamboo.particles;

public interface ParticleSystem {
	public void update(float deltaTime);
	public void render();
	public void add(Particle p);
	public void remove(Particle p);
	public void recycle();
}
