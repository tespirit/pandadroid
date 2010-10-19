package com.tespirit.bamboo.controllers;

import com.tespirit.bamboo.particles.ControllerParticleSystem;
import com.tespirit.bamboo.render.RenderManager;

public class MoveFlingController2d extends MoveController2d{
	private ControllerParticleSystem mParticles;

	public MoveFlingController2d(RenderManager renderManager) {
		super(renderManager);
		this.mParticles = new ControllerParticleSystem();
		renderManager.addUpdater(this.mParticles);
	}
	
	public void end(){
		this.mParticles.create(this.mControlled, this.mVelocity);
	}
	
	public ControllerParticleSystem getParticles(){
		return this.mParticles;
	}
}
