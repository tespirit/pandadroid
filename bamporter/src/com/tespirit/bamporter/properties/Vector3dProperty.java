package com.tespirit.bamporter.properties;

import javax.swing.JSpinner;

import com.tespirit.bamboo.vectors.Vector3d;

public class Vector3dProperty{
	public static interface Property{
		public void setX(float x);
		public void setY(float y);
		public void setZ(float z);
		
		public float getX();
		public float getY();
		public float getZ();
	}
	
	private static class VectorProperty implements Property{
		Vector3d mVector;
		VectorProperty(Vector3d vector){
			this.mVector = vector;
		}

		@Override
		public void setX(float x) {
			this.mVector.setX(x);
		}

		@Override
		public void setY(float y) {
			this.mVector.setY(y);
		}

		@Override
		public void setZ(float z) {
			this.mVector.setZ(z);
		}

		@Override
		public float getX() {
			return this.mVector.getX();
		}

		@Override
		public float getY() {
			return this.mVector.getY();
		}

		@Override
		public float getZ() {
			return this.mVector.getZ();
		}
		
	}
	
	FloatProperty mX;
	FloatProperty mY;
	FloatProperty mZ;
	Property mProperty;
	
	Vector3dProperty(JSpinner x, JSpinner y, JSpinner z, Vector3d vector){
		this(x, y, z, new VectorProperty(vector));
	}
	
	Vector3dProperty(JSpinner x, JSpinner y, JSpinner z, Property property){
		this.mProperty = property;
		this.mX = new FloatProperty(x, new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mProperty.setX(value);
			}
			@Override
			public float getValue() {
				return mProperty.getX();
			}
		});
		
		this.mY = new FloatProperty(y, new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mProperty.setY(value);
			}
			@Override
			public float getValue() {
				return mProperty.getY();
			}
		});
		
		this.mZ = new FloatProperty(z, new FloatProperty.Property() {
			@Override
			public void setValue(float value) {
				mProperty.setZ(value);
			}
			@Override
			public float getValue() {
				return mProperty.getZ();
			}
		});
	}
	
}
