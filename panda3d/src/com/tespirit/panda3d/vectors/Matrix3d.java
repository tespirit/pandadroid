package com.tespirit.panda3d.vectors;

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
		return invert(this);
	}
	
	public Matrix3d invert(Matrix3d m){
		float subDetX, subDetY, subDetZ;
		subDetX = m.yAxis.getY()*m.zAxis.getZ() - m.yAxis.getZ()*m.zAxis.getY();
		subDetY = m.yAxis.getX()*m.zAxis.getZ() - m.yAxis.getZ()*m.zAxis.getX();
		subDetZ = m.yAxis.getX()*m.zAxis.getY() - m.yAxis.getY()*m.zAxis.getX();
		
		float det = 1/(m.xAxis.getX()*subDetX - 
					   m.xAxis.getY()*subDetY + 
					   m.xAxis.getZ()*subDetZ);
		
		float a1, a2, a3, b1, b2, b3, c1, c2, c3, d1, d2 ,d3;
		
		a1 = subDetX*det;
		b1 = -subDetY*det;
		c1 = subDetZ*det;
		d1 = -m.translation.getX()*a1 - m.translation.getY()*b1 - m.translation.getZ()*c1;
		
		subDetX = m.xAxis.getY()*m.zAxis.getZ() - m.xAxis.getZ()*m.zAxis.getY();
		subDetY = m.xAxis.getX()*m.zAxis.getZ() - m.xAxis.getZ()*m.zAxis.getX();
		subDetZ = m.xAxis.getX()*m.zAxis.getY() - m.xAxis.getY()*m.zAxis.getX();
		
		a2 = -subDetX*det;
		b2 = subDetY*det;
		c2 = -subDetZ*det;
		d2 = -m.translation.getX()*a2 - m.translation.getY()*b2 - m.translation.getZ()*c2;

		subDetX = m.xAxis.getY()*m.yAxis.getZ() - m.xAxis.getZ()*m.yAxis.getY();
		subDetY = m.xAxis.getX()*m.yAxis.getZ() - m.xAxis.getZ()*m.yAxis.getX();
		subDetZ = m.xAxis.getX()*m.yAxis.getY() - m.xAxis.getY()*m.yAxis.getX();
		
		a3 = subDetX*det;
		b3 = -subDetY*det;
		c3 = subDetZ*det;
		d3 = -m.translation.getX()*a3 - m.translation.getY()*b3 - m.translation.getZ()*c3;
		
		this.xAxis.set(a1, a2, a3);
		this.yAxis.set(b1, b2, b3);
		this.zAxis.set(c1, c2, c3);
		this.translation.set(d1, d2, d3);
		
		return this;
	}
	
	public Matrix3d fastInverse(){
		return this.fastInverse(this);
	}
	
	public Matrix3d fastInverse(Matrix3d m){
		this.translation.set(-m.translation.dot(m.xAxis), 
				 			 -m.translation.dot(m.yAxis), 
				 			 -m.translation.dot(m.zAxis));
		this.transpose(m);
		return this;
	}
	
	public Matrix3d transpose(Matrix3d m){
		float a1, a2, a3, b1, b2, b3, c1, c2, c3;
		
		a1 = m.xAxis.getX();
		a2 = m.yAxis.getX();
		a3 = m.zAxis.getX();
		
		b1 = m.xAxis.getY();
		b2 = m.yAxis.getY();
		b3 = m.zAxis.getZ();
		
		c1 = m.xAxis.getZ();
		c2 = m.yAxis.getZ();
		c3 = m.zAxis.getZ();
		
		this.xAxis.set(a1, a2, a3);
		this.yAxis.set(b1, b2, b3);
		this.zAxis.set(c1, c2, c3);

		return this;
	}
	
	/**
	 * This transposes the matrix in place.
	 * @return
	 */
	public Matrix3d transpose(){
		return this.transpose(this);
	}
	
	/**
	 * This scales a matrix in place uniformly.
	 * @param s
	 * @return
	 */
	public Matrix3d scale(float s){
		return this.scale(s, s, s);
	}
	
	/**
	 * This scales a matrix in place per axis.
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Matrix3d scale(float x, float y, float z){
		this.m[this.offset] *= x;
		this.m[this.offset+5] *= y;
		this.m[this.offset+10] *= z;
		
		return this;
	}
	
	public Matrix3d scale(Vector3d v){
		return this.scale(v.getX(), v.getY(), v.getZ());
	}
	
	public Matrix3d rotateEuler(float x, float y, float z){
		this.rotateX(x);
		this.rotateY(y);
		this.rotateZ(z);
		return this;
	}
	
	public Matrix3d rotateEuler(Vector3d v){
		return this.rotateEuler(v.getX(), v.getY(), v.getZ());
	}
	
	public Matrix3d rotateX(float t){
		return this.rotateAxis(t, 1, 0, 0);
	}
	
	public Matrix3d rotateY(float t){
		return this.rotateAxis(t, 0, 1, 0);
	}
	
	public Matrix3d rotateZ(float t){
		return this.rotateAxis(t, 0, 0, 1);
	}
	
	public Matrix3d rotateAxis(float t, float x, float y, float z){
		float ta1, ta2, ta3, tb1, tb2, tb3, tc1, tc2, tc3;
		
		//compute 3x3 matrix
		float cosT = (float)Math.cos(Math.toRadians(t));
		float sinT = (float)Math.sin(Math.toRadians(t));
		float flipCosT = 1.0f-cosT;
		
		ta1 = flipCosT*x*x+cosT;
		ta2 = flipCosT*x*y+sinT*z;
		ta3 = flipCosT*x*z-sinT*y;
		
		tb1 = flipCosT*x*y-sinT*z;
		tb2 = flipCosT*y*y+cosT;
		tb3 = flipCosT*y*z+sinT*x;

		tc1 = flipCosT*x*z+sinT*y;
		tc2 = flipCosT*y*z-sinT*x;
		tc3 = flipCosT*z*z+cosT;
		
		float a1, a2, a3, b1, b2, b3, c1, c2, c3;
		
		//push 3x3 matrix onto this 3x3 matrix.
		a1 = this.xAxis.getX()*ta1 + 
			 this.xAxis.getY()*tb1 +
			 this.xAxis.getZ()*tc1;
		a2 = this.xAxis.getX()*ta2 + 
		 	 this.xAxis.getY()*tb2 +
		 	 this.xAxis.getZ()*tc2;
		a3 = this.xAxis.getX()*ta3 + 
	 	 	 this.xAxis.getY()*tb3 +
	 	 	 this.xAxis.getZ()*tc3;
		
		b1 = this.yAxis.getX()*ta1 + 
			 this.yAxis.getY()*tb1 +
			 this.yAxis.getZ()*tc1;
		b2 = this.yAxis.getX()*ta2 + 
		 	 this.yAxis.getY()*tb2 +
		 	 this.yAxis.getZ()*tc2;
		b3 = this.yAxis.getX()*ta3 + 
		 	 this.yAxis.getY()*tb3 +
		 	 this.yAxis.getZ()*tc3;
		
		c1 = this.zAxis.getX()*ta1 + 
			 this.zAxis.getY()*tb1 +
			 this.zAxis.getZ()*tc1;
		c2 = this.zAxis.getX()*ta2 + 
		 	 this.zAxis.getY()*tb2 +
		 	 this.zAxis.getZ()*tc2;
		c3 = this.zAxis.getX()*ta3 + 
		 	 this.zAxis.getY()*tb3 +
		 	 this.zAxis.getZ()*tc3;
		
		this.xAxis.set(a1, a2, a3);
		this.yAxis.set(b1, b2, b3);
		this.zAxis.set(c1, c2, c3);
		
		//Matrix.rotateM(this.m, this.offset, t, x, y, z);
		return this;
	}
	
	public Matrix3d rotateAxis(float t, Vector3d v){
		return this.rotateAxis(t, v.getX(), v.getY(), v.getZ());
	}
	
	public Matrix3d translate(float x, float y, float z){
		this.m[this.offset+12] += x;
		this.m[this.offset+13] += y;
		this.m[this.offset+14] += z;
		return this;
	}
	
	public Matrix3d translate(Vector3d v){
		return this.translate(v.getX(), v.getY(), v.getZ());
	}
	
	/**
	 * This transforms the point and stores it back to that point, so that
	 * point is modified.
	 * @param point
	 * @return
	 */
	public Vector3d transform(Vector3d v){
		return this.transform(v, v);
	}
	
	/**
	 * Use this to copy from one vector to another.
	 * @param vin
	 * @param vout
	 * @return
	 */
	public Vector3d transform(Vector3d vin, Vector3d vout){

		float x = vin.getX()*this.xAxis.getX() + 
				  vin.getY()*this.yAxis.getX() + 
				  vin.getZ()*this.zAxis.getX() + 
				  vin.getPositional()*this.translation.getX();

		float y = vin.getX()*this.xAxis.getY() + 
		 		  vin.getY()*this.yAxis.getY() + 
		 		  vin.getZ()*this.zAxis.getY() + 
		 		  vin.getPositional()*this.translation.getY();

		float z = vin.getX()*this.xAxis.getZ() + 
		 		  vin.getY()*this.yAxis.getZ() + 
		 		  vin.getZ()*this.zAxis.getZ() + 
		 		  vin.getPositional()*this.translation.getZ();
		
		vout.set(x, y, z);
		
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
		this.xAxis.set(1, 0, 0);
		this.yAxis.set(0, 1, 0);
		this.zAxis.set(0, 0, 1);
		this.translation.set(0, 0, 0);
		return this;
	}
	
	public Matrix3d identity3x3(){
		this.xAxis.set(1, 0, 0);
		this.yAxis.set(0, 1, 0);
		this.zAxis.set(0, 0, 1);
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
