package com.tespirit.bamboo.physics;

import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;

public class MoveForce extends ForceGenerator{
	
	public MoveForce(UpdateManager updateManager) {
		super(updateManager);
	}

	private class MoveForceUpdater extends ForceUpdater{
		Vector3d mInitialPosition;
		
		@Override
		public void update(Matrix3d target, Vector3d acceleration, float time) {
			target.getTranslation().add(this.mInitialPosition, acceleration.scale(time * time));
		}

		@Override
		protected void initStartConditions(Matrix3d target, Vector3d force) {
			this.mInitialPosition = target.getTranslation().clone();
		}
	}

	@Override
	protected ForceUpdater createForceUpdater() {
		return new MoveForceUpdater();
	}

}
