package com.tespirit.panda3d.vectors;

import android.opengl.Matrix;

public class Matrix3d {
	
	private float[] m;
	private int offset;
	
	private Vector3d xAxis;
	private Vector3d yAxis;
	private Vector3d zAxis;
	private Vector3d translation;
	
	public static float[] createBuffer(int matrixCount){
		float[] buffer = new float[Matrix3d.SIZE*matrixCount];
		for(int i = 0; i < matrixCount; i++){
			int index = Matrix3d.SIZE*i;
			buffer[index] = 1.0f;
			index+=Matrix3d.SIZEROW;
			buffer[index+1] = 1.0f;
			index+=Matrix3d.SIZEROW;
			buffer[index+2] = 1.0f;
		}
		return buffer;
	}
	
	public static final int SIZE = 16;
	
	private static final int SIZE44 = 16;
	
	public static final int SIZEROW = 4;
	
	public static final Matrix3d IDENTITY = new Matrix3d();
	
	/**
	 * creates a matrix with its own float buffer.
	 */
	public Matrix3d(){
		this(Matrix3d.createBuffer(1));
	}
	
	/**
	 * Use this to set values with an offset of 0
	 * @param values
	 */
	public Matrix3d(float[] values){
		this(values, 0);
	}
		
	/**
	 * This constructor does not clone the float array.
	 * @param values
	 */
	public Matrix3d(float[] values, int offset){
		this.m = values;
		this.offset = offset;
		this.xAxis = new Vector3d(this.m, offset);
		this.xAxis.makeDirectional();
		this.yAxis = new Vector3d(this.m, offset+Vector3d.SIZE);
		this.yAxis.makeDirectional();
		this.zAxis = new Vector3d(this.m, offset+Vector3d.SIZE*2);
		this.zAxis.makeDirectional();
		this.translation = new Vector3d(this.m, offset+Vector3d.SIZE*3);
		this.translation.makePositional();
	}
	
	public Matrix3d(float a1, float a2, float a3, 
					float b1, float b2, float b3, 
					float c1, float c2, float c3,
					float d1, float d2, float d3){
		this(new float[Matrix3d.SIZE44]);
		this.xAxis.set(a1, a2, a3);
		this.yAxis.set(b1, b2, b3);
		this.zAxis.set(c1, c2, c3);
		this.translation.set(d1, d2, d3);
	}
	
	public Vector3d getXAxis(){
		return this.xAxis;
	}
	
	public Vector3d getYAxis(){
		return this.yAxis;
	}
	
	public Vector3d getZAxis(){
		return this.zAxis;
	}
	
	public Vector3d getTranslation(){
		return this.translation;
	}
	
	public float[] getBuffer(){
		return this.m;
	}
	
	public int getBufferOffset(){
		return this.offset;
	}
	
	public float getValue(int row, int col){
		return this.m[this.offset+col+row*Matrix3d.SIZEROW];
	}
	
	public float getValue(int i){
		return this.m[this.offset+i];
	}
	
	public void setValue(float val, int row, int col){
		this.m[this.offset+col+row*Matrix3d.SIZEROW] = val;
	}
	
	public void setValue(float val, int i){
		this.m[this.offset+i] = val;
	}
	
	/**
	 * This will clone the current matrix and the float buffer.
	 * This is an easy way to make a matrix that is not part of a shared
	 * buffer.
	 */
	public Matrix3d clone(){
		float[] vals = new float[16];
		for(int i = 0; i < Matrix3d.SIZE44; i++){
			vals[i] = this.m[i+this.offset];
		}
		return new Matrix3d(vals);
	}
	
	/**
	 * This copies a matrix into this buffer.
	 * @param m
	 */
	public void copy(Matrix3d m){
		this.xAxis.copy(m.xAxis);
		this.yAxis.copy(m.yAxis);
		this.zAxis.copy(m.zAxis);
		this.translation.copy(m.translation);
	}
	
	/**
	 * Inverts a matrix and stores the result to this matrix.
	 * @return
	 */
	public Matrix3d invert(){
		float[] temp = new float[Matrix3d.SIZE44];
		if(Matrix.invertM(temp, 0, this.m, 0)){
			this.m = temp;
			this.offset = 0;
			return this;
		} else {
			return null;
		}
	}
	
	public Matrix3d invert(Matrix3d m){
		if(Matrix.invertM(this.m, this.offset, m.m, m.offset)){
			return this;
		} else {
			return null;
		}
	}
	
	/**
	 * This transposes the matrix in place.
	 * @return
	 */
	public Matrix3d transpose(){
		float temp;
		
		temp = this.m[1];
		this.m[this.offset+1] = this.m[this.offset+4];
		this.m[this.offset+4] = temp;
		
		temp = this.m[2];
		this.m[this.offset+2] = this.m[this.offset+12];
		this.m[this.offset+12] = temp;
		
		temp = this.m[6];
		this.m[this.offset+6] = this.m[this.offset+13];
		this.m[this.offset+13] = temp;
		
		return this;
	}
	
	/**
	 * This scales a matrix in place uniformly.
	 * @param s
	 * @return
	 */
	public Matrix3d scale(float s){
		Matrix.scaleM(this.m, this.offset, s, s, s);
		
		return this;
	}
	
	/**
	 * This scales a matrix in place per axis.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Matrix3d scale(float x, float y, float z){
		Matrix.scaleM(this.m, this.offset, x, y, z);
		
		return this;
	}
	
	public Matrix3d scale(Vector3d v){
		Matrix.scaleM(this.m, this.offset, v.getX(), v.getY(), v.getZ());
		
		return this;
	}
	
	public Matrix3d rotateEuler(float x, float y, float z){
		Matrix.setRotateEulerM (this.m, this.offset, x, y, z);
		return this;
	}
	
	public Matrix3d rotateEuler(Vector3d v){
		Matrix.setRotateEulerM (this.m, this.offset, v.getX(), v.getY(), v.getZ());
		return this;
	}
	
	public Matrix3d rotateX(float t){
		Matrix.rotateM(this.m, this.offset, t, 1, 0, 0);
		return this;
	}
	
	public Matrix3d rotateY(float t){
		Matrix.rotateM(this.m, this.offset, t, 0, 1, 0);
		return this;
	}
	
	public Matrix3d rotateZ(float t){
		Matrix.rotateM(this.m, this.offset, t, 0, 0, 1);
		return this;
	}
	
	public Matrix3d rotateAxis(float t, float x, float y, float z){
		Matrix.rotateM(this.m, this.offset, t, x, y, z);
		return this;
	}
	
	public Matrix3d rotateAxis(float t, Vector3d v){
		Matrix.rotateM(this.m, this.offset, t, v.getX(), v.getY(), v.getZ());
		return this;
	}
	
	public Matrix3d translate(float x, float y, float z){
		Matrix.translateM(this.m, this.offset, x, y, z);
		return this;
	}
	
	public Matrix3d translate(Vector3d v){
		Matrix.translateM(this.m, this.offset, v.getX(), v.getY(), v.getZ());
		return this;
	}
	
	/**
	 * This transforms the point and stores it back to that point, so that
	 * point is modified.
	 * @param point
	 * @return
	 */
	public Vector3d transform(Vector3d v){
		Vector3d vin = v.clone();
		Matrix.multiplyMV(v.getBuffer(), v.getBufferOffset(), this.m, this.offset, vin.getBuffer(), vin.getBufferOffset());
		return v;
	}
	
	/**
	 * Use this to copy from one vector to another.
	 * @param vin
	 * @param vout
	 * @return
	 */
	public Vector3d transform(Vector3d vin, Vector3d vout){
		Matrix.multiplyMV(vout.getBuffer(), vout.getBufferOffset(), this.m, this.offset, vin.getBuffer(), vin.getBufferOffset());
		return vout;
	}
	
	/**
	 * use this to multiply the incoming matrix with this matrix and store the results to this matrix.
	 * @param m
	 * @return
	 */
	public Matrix3d multiply(Matrix3d m){
		return this.multiply(m, this);
	}
	
	/**
	 * use this to multiply two matrices together and store the results in this matrix.
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Matrix3d multiply(Matrix3d m1, Matrix3d m2){
		float a1, a2, a3, b1, b2, b3, c1, c2, c3, d1, d2, d3;
		
		a1 = m2.getValue(0, 0) * m1.getValue(0,0) +
			 m2.getValue(0, 1) * m1.getValue(1,0) +
			 m2.getValue(0, 2) * m1.getValue(2,0);
		
		a2 = m2.getValue(0, 0) * m1.getValue(0,1) +
		 	 m2.getValue(0, 1) * m1.getValue(1,1) +
		 	 m2.getValue(0, 2) * m1.getValue(2,1);
		
		a3 = m2.getValue(0, 0) * m1.getValue(0,2) +
		 	 m2.getValue(0, 1) * m1.getValue(1,2) +
		 	 m2.getValue(0, 2) * m1.getValue(2,2);
		
		b1 = m2.getValue(1, 0) * m1.getValue(0,0) +
		 	 m2.getValue(1, 1) * m1.getValue(1,0) +
		 	 m2.getValue(1, 2) * m1.getValue(2,0);
	
		b2 = m2.getValue(1, 0) * m1.getValue(0,1) +
	 	 	 m2.getValue(1, 1) * m1.getValue(1,1) +
	 	 	 m2.getValue(1, 2) * m1.getValue(2,1);
	
		b3 = m2.getValue(1, 0) * m1.getValue(0,2) +
	 	 	 m2.getValue(1, 1) * m1.getValue(1,2) +
	 	 	 m2.getValue(1, 2) * m1.getValue(2,2);
		
		c1 = m2.getValue(2, 0) * m1.getValue(0,0) +
	 	 	 m2.getValue(2, 1) * m1.getValue(1,0) +
	 	 	 m2.getValue(2, 2) * m1.getValue(2,0);

		c2 = m2.getValue(2, 0) * m1.getValue(0,1) +
		 	 m2.getValue(2, 1) * m1.getValue(1,1) +
		 	 m2.getValue(2, 2) * m1.getValue(2,1);
	
		c3 = m2.getValue(2, 0) * m1.getValue(0,2) +
		 	 m2.getValue(2, 1) * m1.getValue(1,2) +
		 	 m2.getValue(2, 2) * m1.getValue(2,2);
	
		d1 = m2.getValue(3, 0) * m1.getValue(0,0) +
		 	 m2.getValue(3, 1) * m1.getValue(1,0) +
		 	 m2.getValue(3, 2) * m1.getValue(2,0) + m1.getValue(3, 0);
	
		d2 = m2.getValue(3, 0) * m1.getValue(0,1) +
		 	 m2.getValue(3, 1) * m1.getValue(1,1) +
		 	 m2.getValue(3, 2) * m1.getValue(2,1) + m1.getValue(3, 1);
	
		d3 = m2.getValue(3, 0) * m1.getValue(0,2) +
		 	 m2.getValue(3, 1) * m1.getValue(1,2) +
		 	 m2.getValue(3, 2) * m1.getValue(2,2) + m1.getValue(3, 2);
		
		this.xAxis.set(a1, a2, a3);
		this.yAxis.set(b1, b2, b3);
		this.zAxis.set(c1, c2, c3);
		this.translation.set(d1, d2, d3);

		return this;
	}
	
	public Matrix3d identity(){
		Matrix.setIdentityM(this.m, this.offset);
		return this;
	}
	
	public Matrix3d identity3x3(){
		this.m[this.offset+0] = 1;
		this.m[this.offset+1] = 0;
		this.m[this.offset+2] = 0;
		
		this.m[this.offset+4] = 0;
		this.m[this.offset+5] = 1;
		this.m[this.offset+6] = 0;
		
		this.m[this.offset+8] = 0;
		this.m[this.offset+9] = 0;
		this.m[this.offset+10] = 1;
		
		return this;
	}
	
	public boolean isIdentity(){
		return this.equals(Matrix3d.IDENTITY);
	}
	
	public boolean isIdentity3x3(){
		return this.xAxis.equals(Matrix3d.IDENTITY.xAxis) &&
			   this.yAxis.equals(Matrix3d.IDENTITY.yAxis) &&
			   this.zAxis.equals(Matrix3d.IDENTITY.zAxis);
	}
	
	public Matrix3d aim(Matrix3d m){
		//TODO: implement
		return this;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Matrix3d){
			Matrix3d m = (Matrix3d)o;
			for(int i = 0; i < Matrix3d.SIZE44; i++){
				if(!Util.floatEquals(this.m[this.offset+i], m.m[m.offset+i])){
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
