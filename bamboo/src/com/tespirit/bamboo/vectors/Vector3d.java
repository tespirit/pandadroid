package com.tespirit.bamboo.vectors;

import java.lang.Math;

public class Vector3d {
	float[] v;
	int offset;
		
	public final static int SIZE = 4; //generic size info, incase this changes for some reason
	
	public static float[] createBuffer(int vectorCount){
		float[] buffer = new float[Vector3d.SIZE*vectorCount];
		for(int i = 0; i < vectorCount; i++){
			buffer[3+Vector3d.SIZE*i] = 1.0f;
		}
		return buffer;
	}
	
	public Vector3d(){
		this.v = new float[Vector3d.SIZE];
		this.v[3] = 1;
		this.offset = 0;
	}
	
	public Vector3d(float x, float y, float z){
		this.v = new float[Vector3d.SIZE];
		this.v[0] = x;
		this.v[1] = y;
		this.v[2] = z;
		this.v[3] = 1;
		this.offset = 0;
	}
	
	/**
	 * Use this when you want to set a new single vector buffer.
	 * @param v
	 */
	public Vector3d(float[] v){
		this.v = v;
		this.offset = 0;
	}
	
	/**
	 * This will share a common buffer.
	 * @param v
	 * @param offset
	 */
	public Vector3d(float[] v, int offset){
		this.v = v;
		this.offset = offset;
	}
	
	public float[] getBuffer(){
		return this.v;
	}
	
	public int getBufferOffset(){
		return this.offset;
	}
	
	public float get(int i){
		return this.v[this.offset+i];
	}
	
	public float getX(){
		return this.v[this.offset];
	}
	
	public float getY(){
		return this.v[this.offset+1];
	}
	
	public float getZ(){
		return this.v[this.offset+2];
	}
	
	public float getPositional(){
		return this.v[this.offset+3];
	}
	
	public void setX(float x){
		this.v[this.offset] = x;
	}
	
	public void setY(float y){
		this.v[this.offset+1] = y;
	}
	
	public void setZ(float z){
		this.v[this.offset+2] = z;
	}
		
	public void set(float x, float y, float z){
		this.v[this.offset] = x;
		this.v[this.offset+1] = y;
		this.v[this.offset+2] = z;
	}
	
	public void setAt(int i, float f){
		this.v[this.offset+i] = f;
	}
	
	/**
	 * this converts the the vector to only a directional vector
	 * that will only get rotational and scalar information applied to it
	 */
	public void makeDirectional(){
		this.v[this.offset+3] = 0.0f;
	}
	
	/**
	 * when using a float buffer, this should be called to make this a normal
	 * vector.
	 */
	public void makePositional(){
		this.v[this.offset+3] = 1.0f;
	}
	
	public void copy(Vector3d v){
		this.v[this.offset] = v.v[v.offset];
		this.v[this.offset+1] = v.v[v.offset+1];
		this.v[this.offset+2] = v.v[v.offset+2];
		this.v[this.offset+3] = v.v[v.offset+3];
	}
	
	public Vector3d clone(){
		Vector3d v = new Vector3d(this.v[this.offset], 
								  this.v[this.offset+1], 
								  this.v[this.offset+2]);
		v.v[v.offset+3] = this.v[this.offset+3];
		return v;
	}
		
	public Vector3d add(Vector3d v){
		this.v[this.offset] += v.v[v.offset];
		this.v[this.offset+1] += v.v[v.offset+1];
		this.v[this.offset+2] += v.v[v.offset+2];
		return this;
	}
	
	public Vector3d add(Vector3d v1, Vector3d v2){
		this.v[this.offset] = v1.v[v1.offset] + v2.v[v2.offset];
		this.v[this.offset+1] = v1.v[v1.offset+1] + v2.v[v2.offset+1];
		this.v[this.offset+2] = v1.v[v1.offset+2] + v2.v[v2.offset+2];
		
		return this;
	}
	
	public Vector3d sub(Vector3d v){
		this.v[this.offset] -= v.v[v.offset];
		this.v[this.offset+1] -= v.v[v.offset+1];
		this.v[this.offset+2] -= v.v[v.offset+2];
		
		return this;
	}
	
	public Vector3d sub(Vector3d v1, Vector3d v2){
		this.v[this.offset] = v1.v[v1.offset] - v2.v[v2.offset];
		this.v[this.offset+1] = v1.v[v1.offset+1] - v2.v[v2.offset+1];
		this.v[this.offset+2] = v1.v[v1.offset+2] - v2.v[v2.offset+2];
		
		return this;
	}
	
	//Add basic functionality here.
	public Vector3d scale(float s){
		this.v[this.offset] *= s;
		this.v[this.offset+1] *= s;
		this.v[this.offset+2] *= s;
		
		return this;
	}
	
	public Vector3d normalize(){
		float mag = this.magnitude();
		this.v[this.offset] /= mag;
		this.v[this.offset+1] /= mag;
		this.v[this.offset+2] /= mag;
		
		return this;
	}
	
	public float dot(Vector3d v){
		return this.v[this.offset]*v.v[v.offset] + 
			   this.v[this.offset+1]*v.v[v.offset+1] + 
			   this.v[this.offset+2]*v.v[v.offset+2];
	}
	
	public float angle(Vector3d v){
		return (float)Math.acos(this.dot(v)/(this.magnitude()*v.magnitude()));
	}
	
	public float magnitude(){
		return (float)Math.sqrt(this.magnitude2());
	}
	
	public float magnitude2(){
		float x = this.v[this.offset];
		float y = this.v[this.offset+1];
		float z = this.v[this.offset+2];
		return x*x + y*y + z*z;
	}
	
	public Vector3d project(Vector3d v){
		this.scale(this.dot(v)/this.magnitude2());
		return this;
	}
	
	/**
	 * v2 is the vector being projected to
	 * @param v1
	 * @param v2
	 * @return
	 */
	public Vector3d project(Vector3d v1, Vector3d v2){
		this.copy(v2);
		this.project(v1);
		return this;
	}
	
	/**
	 * Convenience function for projecting onto v, instead of v
	 * being projected on this.
	 * @param v
	 * @return v
	 */
	public Vector3d projectOnTo(Vector3d v){
		this.scale(v.dot(this)/v.magnitude2());
		return v;
	}
	
	@Override 
	public boolean equals(Object o){
		if(o instanceof Vector3d){
			Vector3d v = (Vector3d)o;
			return Util.floatEquals(v.v[v.offset], this.v[this.offset]) && 
				   Util.floatEquals(v.v[v.offset+1], this.v[this.offset+1]) && 
				   Util.floatEquals(v.v[v.offset+2], this.v[this.offset+2]) &&
				   Util.floatEquals(v.v[v.offset+3], this.v[this.offset+3]);
		}
		return false;
	}
}
