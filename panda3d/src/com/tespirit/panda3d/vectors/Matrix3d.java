package com.tespirit.panda3d.vectors;

import android.opengl.Matrix;

public class Matrix3d {
	
	private float[] m;
	private int offset;
	
	public static final int SIZE44 = 16;
	
	public static final int SIZEROW = 4;
	
	public static final int SIZE34 = 12;
	
	
	public static final Matrix3d IDENTITY = new Matrix3d();
	
	/**
	 * creates a matrix with its own float buffer.
	 */
	public Matrix3d(){
		this.m = new float[Matrix3d.SIZE44];

		this.offset = 0;

		Matrix.setIdentityM(this.m, this.offset);
	}
	
	/**
	 * Use this to set values with an offset of 0
	 * @param values
	 */
	public Matrix3d(float[] values){
		this.m = values;
		this.offset = 0;
	}
		
	/**
	 * This constructor does not clone the float array.
	 * @param values
	 */
	public Matrix3d(float[] values, int offset){
		this.m = values;
		this.offset = offset;
	}
	
	public Matrix3d(float a1, float a2, float a3, float a4, 
					float b1, float b2, float b3, float b4, 
					float c1, float c2, float c3, float c4){
		this.m = new float[Matrix3d.SIZE44];
		this.offset = 0;
		
		this.m[0] = a1;
		this.m[1] = b1;
		this.m[2] = c1;
		this.m[3] = 0;
		
		this.m[4] = a2;
		this.m[5] = b2;
		this.m[6] = c2;
		this.m[7] = 0;
		
		this.m[8] = a3;
		this.m[9] = b3;
		this.m[10] = c3;
		this.m[11] = 0;
		
		this.m[12] = a4;
		this.m[13] = b4;
		this.m[14] = c4;
		this.m[15] = 1;
	}
	
	public Matrix3d(float a1, float a2, float a3, float a4, 
					float b1, float b2, float b3, float b4, 
					float c1, float c2, float c3, float c4, 
					float d1, float d2, float d3, float d4){
		this.m = new float[Matrix3d.SIZE44];
		this.offset = 0;
		
		this.m[0] = a1;
		this.m[1] = b1;
		this.m[2] = c1;
		this.m[3] = d1;
		
		this.m[4] = a2;
		this.m[5] = b2;
		this.m[6] = c2;
		this.m[7] = d2;
		
		this.m[8] = a3;
		this.m[9] = b3;
		this.m[10] = c3;
		this.m[11] = d3;
		
		this.m[12] = a4;
		this.m[13] = b4;
		this.m[14] = c4;
		this.m[15] = d4;
	}
			
	/**
	 * if this is attached to a matrix buffer, this is a way to slide to the
	 * next set of values.
	 * @return
	 */
	public boolean next(){
		if(this.offset+Matrix3d.SIZE44 < this.m.length){
			this.offset += Matrix3d.SIZE44;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * if this is attached to a matrix buffer, this is a way to slide to the
	 * previous set of values.
	 * @return
	 */
	public boolean prev(){
		if(this.offset-Matrix3d.SIZE44 >= 0){
			this.offset -= Matrix3d.SIZE44;
			return true;
		} else {
			return false;
		}
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
		for(int i = 0; i < Matrix3d.SIZE44; i++){
			this.m[i + this.offset] = m.m[i+m.offset];
		}
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
		this.m[1] = this.m[4];
		this.m[4] = temp;
		
		temp = this.m[2];
		this.m[2] = this.m[12];
		this.m[12] = temp;
		
		temp = this.m[6];
		this.m[6] = this.m[13];
		this.m[13] = temp;
		
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
		Matrix.rotateM(this.m, this.offset, t, 0, 1, 0);
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
		Matrix3d m2 = this.clone();
		Matrix.multiplyMM(this.m, this.offset, m.m, m.offset, m2.m, m2.offset);
		return this;
	}
	
	/**
	 * use this to multiply two matrices together and store the results in this matrix.
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Matrix3d multiply(Matrix3d m1, Matrix3d m2){
		Matrix.multiplyMM(this.m, this.offset, m1.m, m2.offset, m2.m, m2.offset);
		return this;
	}
	
	public Matrix3d identity(){
		Matrix.setIdentityM(this.m, this.offset);
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
