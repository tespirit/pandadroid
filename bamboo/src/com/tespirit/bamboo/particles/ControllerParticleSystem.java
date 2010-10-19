package com.tespirit.bamboo.particles;

import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.TimeUpdater;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public class ControllerParticleSystem extends StandardParticleSystem implements TimeUpdater{
	public class ControllerParticle extends Particle{
		private Matrix3d mControlled;

		@Override
		public void render() {
			//VOID
		}

		@Override
		public boolean isAlive() {
			return this.getVelocity().magnitude2() > 0.01f;
		}

		@Override
		public void update(float deltaTime) {
			super.update(deltaTime);
			this.mControlled.getTranslation().copy(this.getPosition());
		}
	}
	
	private Clock mClock;
	
	public ControllerParticleSystem(){
		this(100);
	}
	
	public ControllerParticleSystem(int maxAmount){
		super(maxAmount);
		this.mPatricles = new Particle[this.mMaxCount];
		for(int i = 0; i < this.mPatricles.length; i++){
			this.mPatricles[i] = new ControllerParticle();
		}
	}
	
	public void create(Matrix3d controlled, Vector3d velocity){
		this.create(controlled, velocity, 1.0f);
	}
	
	public void create(Matrix3d controlled, Vector3d velocity, float mass){
		ControllerParticle p = (ControllerParticle)this.add();
		p.mControlled = controlled;
		p.setInitialPosition(controlled.getTranslation());
		p.setInitialVelocity(velocity);
		p.setMass(mass);
	}

	@Override
	public void update() {
		this.update(this.mClock.getDeltaTime()/1000.0f);
	}

	@Override
	public void setUpdateManager(UpdateManager updateManager) {
		//VOID
	}

	@Override
	public void setClock(Clock clock) {
		this.mClock = clock;
	}
}
