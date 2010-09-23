package com.tespirit.panda3d.app;

import com.tespirit.panda3d.controllers.Controller3d;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionSensor3d implements SensorEventListener{
	private Controller3d onRotateController;
	private Controller3d onAccelerateController;
	
	private long lastTime;
	
	float[] orientation;
	float[] acceleration;
	float[] geoMagnetic;

	float[] rotationMatrix;
	
	private static final float[] nullArray = new float[]{0,0,0};
	
	public MotionSensor3d(Controller3d onRotateController, Controller3d onAccelerateController){
		this.onRotateController = onRotateController;
		this.onAccelerateController = onAccelerateController;
		this.orientation = new float[3];
		this.rotationMatrix = new float[16];
		this.acceleration = MotionSensor3d.nullArray;
		this.geoMagnetic = MotionSensor3d.nullArray;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		long time;
		if(this.lastTime != -1) { 
			time = event.timestamp - this.lastTime;
		} else {
			time = 0;
		}
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			this.onAccelerateController.update(this.acceleration[0],
					                           this.acceleration[1],
					                       	   this.acceleration[2],
					                       	   time);
			
			this.acceleration = event.values;
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.geoMagnetic = event.values;
			break;
		}
		
		if(SensorManager.getRotationMatrix(this.rotationMatrix, null, this.acceleration, this.geoMagnetic)){
			SensorManager.getOrientation(this.rotationMatrix, this.orientation);
			this.onRotateController.update((float)Math.toDegrees(this.orientation[0]),
										   (float)Math.toDegrees(this.orientation[1]),
										   (float)Math.toDegrees(this.orientation[2]),
										   time);
		}
		
	}

}
