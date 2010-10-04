package com.tespirit.bamboo.controllers;

/**
 * this class is intended for hooking up to
 * @author Todd Espiritu Santo
 *
 */
public class EulerController3d extends MatrixController3d{
	private float xScale;
	private float yScale;
	private float zScale;
	private Euler type;
	
	public static enum Euler{
		XYZ,
		XZY,
		YXZ,
		YZX,
		ZXY,
		ZYX
	}
	
	public EulerController3d(Euler type){
		this(type, 1, 1, 1);
	}
	
	public EulerController3d(Euler type, float xScale, float yScale, float zScale){
		this.xScale = xScale;
		this.yScale = yScale;
		this.zScale = zScale;
		this.type = type;
	}

	@Override
	protected void update(float[] values, long time, long deltaTime) {
		float x = values[BaseController3d.DELTA_X] * this.xScale;
		float y = values[BaseController3d.DELTA_Y] * this.yScale;
		float z = values[BaseController3d.DELTA_Z] * this.zScale;
		switch(this.type){
		case XYZ:
			this.mControlled.rotateX(x);
			this.mControlled.rotateY(y);
			this.mControlled.rotateZ(z);
			break;
		case XZY:
			this.mControlled.rotateX(x);
			this.mControlled.rotateZ(z);
			this.mControlled.rotateY(y);
			break;
		case YXZ:
			this.mControlled.rotateY(y);
			this.mControlled.rotateX(x);
			this.mControlled.rotateZ(z);
			break;
		case YZX:
			this.mControlled.rotateY(y);
			this.mControlled.rotateZ(z);
			this.mControlled.rotateX(x);
			break;
		case ZXY:
			this.mControlled.rotateZ(z);
			this.mControlled.rotateX(x);
			this.mControlled.rotateY(y);
			break;
		case ZYX:
			this.mControlled.rotateZ(z);
			this.mControlled.rotateY(y);
			this.mControlled.rotateX(x);
			break;
		}
	}

}
