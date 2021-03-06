package com.tespirit.pandadroid.app;

import com.tespirit.bamboo.controllers.Controller3d;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionSensor3d implements SensorEventListener{
	private Controller3d onRotateController;
	private Controller3d onAccelerateController;
	
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
		
		switch(event.sensor.getType()){
		case Sensor.TYPE_ACCELEROMETER:
			this.acceleration = event.values.clone();
			this.onAccelerateController.set(this.acceleration[0],
                    this.acceleration[1],
                	   this.acceleration[2],
                	   event.timestamp);
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			this.geoMagnetic = event.values.clone();
			break;
		}
		
		if(SensorManager.getRotationMatrix(this.rotationMatrix, null, this.acceleration, this.geoMagnetic)){
			SensorManager.getOrientation(this.rotationMatrix, this.orientation);
			this.onRotateController.set((float)Math.toDegrees(this.orientation[0]),
										   (float)Math.toDegrees(this.orientation[1]),
										   (float)Math.toDegrees(this.orientation[2]),
										   event.timestamp);
		}
		
	}

}
