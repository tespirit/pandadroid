package com.tespirit.bamboo.physics;

import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.TimeUpdater;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public abstract class ForceGenerator{

	protected abstract class ForceUpdater implements TimeUpdater{
		private Matrix3d mTarget;
		private Vector3d mForce;
		private Vector3d mCurrentForce;
		private Vector3d mFrictionForce;
		private Vector3d mFrictionDirection;
		private float mCurrentTime;
		private float mMassInv;
		private Clock mClock;
		
		public void init(Matrix3d target, Vector3d force, float friction, float mass){
			this.mTarget = target;
			this.mForce = force.clone();
			this.mFrictionDirection = this.mForce.clone();
			this.mFrictionDirection.normalize();
			this.mFrictionDirection.scale(-friction);
			this.mCurrentForce = new Vector3d();
			this.mFrictionForce = new Vector3d();
			this.mCurrentTime = 0;
			this.mMassInv = 1/mass;
			this.initStartConditions(target, force);
			mUpdateManager.addUpdater(this);
		}
		
		protected abstract void initStartConditions(Matrix3d target, Vector3d force);

		@Override
		public void update() {
			this.mCurrentTime += this.mClock.getDeltaTime()/1000f;
			this.mFrictionForce.copy(this.mFrictionDirection);
			this.mFrictionForce.scale(this.mCurrentTime);
			if(this.mFrictionForce.magnitude2() >= this.mForce.magnitude2()){
				mUpdateManager.removeUpdater(this);
				this.recycle();
			} else {
				this.mCurrentForce.add(this.mForce, this.mFrictionForce);
				this.update(this.mTarget, this.mCurrentForce.scale(this.mMassInv), this.mCurrentTime);
			}
		}
		
		public void recycle(){
			this.mClock = null;
			this.mForce = null;
			this.mFrictionDirection = null;
			this.mFrictionForce = null;
			this.mCurrentForce = null;
			this.mTarget = null;
		}
		
		public abstract void update(Matrix3d target, Vector3d acceleration, float time);
		
		@Override
		public void setUpdateManager(UpdateManager updateManager) {
			//VOID
		}

		@Override
		public void setClock(Clock clock) {
			this.mClock = clock;
		}

	}
	
	private UpdateManager mUpdateManager;
	private float mFriction;
	
	public ForceGenerator(UpdateManager updateManager){
		this.mUpdateManager = updateManager;
		this.mFriction = 0.5f;
	}
	
	public void apply(Matrix3d target, Vector3d force, float mass){
		ForceUpdater forceUpdater = this.createForceUpdater();
		forceUpdater.init(target, force, this.mFriction, mass);
	}
	
	public void setFriction(float friction){
		this.mFriction = friction;
	}
	
	protected abstract ForceUpdater createForceUpdater();
	
}