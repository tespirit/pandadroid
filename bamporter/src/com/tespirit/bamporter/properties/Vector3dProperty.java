package com.tespirit.bamporter.properties;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.tespirit.bamboo.vectors.Vector3d;

public abstract class Vector3dProperty extends Property<Vector3d>{
	
	public static class Bind extends Vector3dProperty{
		Vector3d mVector;

		public Bind(String name, Vector3d v) {
			this(name, v, 1f);
		}
		
		public Bind(String name, Vector3d v, Float step){
			super(name, step);
			this.mVector = v;
		}

		@Override
		public void setValue(Vector3d value) {
			//VOID
		}

		@Override
		public Vector3d getValue() {
			return this.mVector;
		}
		
	}
	
	private FloatProperty mX;
	private FloatProperty mY;
	private FloatProperty mZ;
	
	public Vector3dProperty(String name){
		this(name, 1f);
	}

	public Vector3dProperty(String name, Float step) {
		super(name);
		this.mX = new FloatProperty("x", step){
			@Override
			public void setValue(Float value) {
				Vector3d v = Vector3dProperty.this.getValue();
				v.setX(value);
				Vector3dProperty.this.setValue(v);
			}
			@Override
			public Float getValue() {
				return Vector3dProperty.this.getValue().getX();
			}
			
		};
		
		this.mY = new FloatProperty("y", step){
			@Override
			public void setValue(Float value) {
				Vector3d v = Vector3dProperty.this.getValue();
				v.setY(value);
				Vector3dProperty.this.setValue(v);
			}
			@Override
			public Float getValue() {
				return Vector3dProperty.this.getValue().getY();
			}
			
		};
		
		this.mZ = new FloatProperty("z", step){
			@Override
			public void setValue(Float value) {
				Vector3d v = Vector3dProperty.this.getValue();
				v.setZ(value);
				Vector3dProperty.this.setValue(v);
			}
			@Override
			public Float getValue() {
				return Vector3dProperty.this.getValue().getZ();
			}
			
		};
	}

	@Override
	public JComponent getEditor() {
		JPanel panel = new JPanel(new GridLayout(1,3));
		panel.add(this.mX.getEditor());
		panel.add(this.mY.getEditor());
		panel.add(this.mZ.getEditor());
		
		return panel;
	}
	
}