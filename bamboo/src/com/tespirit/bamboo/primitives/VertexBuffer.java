package com.tespirit.bamboo.primitives;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;

import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Vector3d;

public class VertexBuffer implements Externalizable{	
	private FloatBuffer[] mBuffers;
	private int[] mTypes;
	private int mCount;
	
	public final static int POSITION = 0;
	
	public final static int NORMAL = 1;
	
	public final static int TEXCOORD = 2;
	
	public final static int COLOR = 3;
	
	public final static int COUNT = 4;
	
	private final static int[] STRIDES = {3,3,2,4};
	
	/**
	 * Defaultly inits all buffers.
	 * @param size
	 */
	public VertexBuffer(int count){
		this(count, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD, VertexBuffer.COLOR});	}
	
	public VertexBuffer(int count, int[] types){
		this.mBuffers = new FloatBuffer[VertexBuffer.STRIDES.length];
		this.mCount = count;
		this.mTypes = types;
		for(int i = 0; i < types.length; i++){
			this.mBuffers[types[i]] = this.allocateFloatBuffer(STRIDES[types[i]]);
		}
	}
	
	/**
	 * copy constructor
	 * @param vertexBuffer
	 */
	public VertexBuffer(VertexBuffer vertexBuffer){
		this(vertexBuffer.mCount, vertexBuffer.mTypes);
		vertexBuffer.lock();
		this.lock();
		for(int i = 0; i < this.mTypes.length; i++){
			FloatBuffer a = this.mBuffers[this.mTypes[i]];
			FloatBuffer b = vertexBuffer.mBuffers[this.mTypes[i]];
			for(int j = 0; j < this.mCount * STRIDES[i]; j++){
				a.put(b.get());
			}
		}
		this.unlock();
		vertexBuffer.unlock();
	}
	
	public VertexBuffer(){
		
	}
	
	private FloatBuffer allocateFloatBuffer(int stride){
		ByteBuffer temp = ByteBuffer.allocateDirect(this.mCount*stride*4);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	
	public void lock(){
		
	}
	
	public void unlock(){
		for(int i = 0; i < this.mTypes.length; i++){
			this.mBuffers[this.mTypes[i]].position(0);
		}
	}
	
	public void addPosition(float x, float y, float z){
		this.mBuffers[VertexBuffer.POSITION].put(x);
		this.mBuffers[VertexBuffer.POSITION].put(y);
		this.mBuffers[VertexBuffer.POSITION].put(z);
	}
	
	public void addPosition(Vector3d point){
		this.mBuffers[VertexBuffer.POSITION].put(point.getX());
		this.mBuffers[VertexBuffer.POSITION].put(point.getY());
		this.mBuffers[VertexBuffer.POSITION].put(point.getZ());
	}
	
	public void setPosition(float[] values){
		this.mBuffers[VertexBuffer.POSITION].put(values);
		this.mBuffers[VertexBuffer.POSITION].position(0);
	}
	
	public void addNormal(float x, float y, float z){
		this.mBuffers[VertexBuffer.NORMAL].put(x);
		this.mBuffers[VertexBuffer.NORMAL].put(y);
		this.mBuffers[VertexBuffer.NORMAL].put(z);
	}
	
	public void addNormal(Vector3d point){
		this.mBuffers[VertexBuffer.NORMAL].put(point.getX());
		this.mBuffers[VertexBuffer.NORMAL].put(point.getY());
		this.mBuffers[VertexBuffer.NORMAL].put(point.getZ());
	}
	
	public void setNormal(float[] values){
		this.mBuffers[VertexBuffer.NORMAL].put(values);
		this.mBuffers[VertexBuffer.NORMAL].position(0);
	}
	
	public void addTexcoord(float u, float v){
		this.mBuffers[VertexBuffer.TEXCOORD].put(u);
		this.mBuffers[VertexBuffer.TEXCOORD].put(v);
	}
	
	public void setTexcoord(float[] values){
		this.mBuffers[VertexBuffer.TEXCOORD].put(values);
		this.mBuffers[VertexBuffer.TEXCOORD].position(0);
	}
	
	public void addColor(float r, float g, float b, float a){
		this.mBuffers[VertexBuffer.COLOR].put(r);
		this.mBuffers[VertexBuffer.COLOR].put(g);
		this.mBuffers[VertexBuffer.COLOR].put(b);
		this.mBuffers[VertexBuffer.COLOR].put(a);
	}
	
	public void addColor(float r, float g, float b){
		this.mBuffers[VertexBuffer.COLOR].put(r);
		this.mBuffers[VertexBuffer.COLOR].put(g);
		this.mBuffers[VertexBuffer.COLOR].put(b);
		this.mBuffers[VertexBuffer.COLOR].put(1.0f);
	}
	
	public void setColor(float[] values){
		this.mBuffers[VertexBuffer.TEXCOORD].put(values);
		this.mBuffers[VertexBuffer.TEXCOORD].position(0);
	}
	
	public boolean nextVector3d(Vector3d out, int type){
		FloatBuffer fb = this.mBuffers[type];
		if(fb.hasRemaining()){
			out.set(fb.get(), fb.get(), fb.get());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean nextTexcoord(Vector3d out){
		FloatBuffer fb = this.mBuffers[VertexBuffer.TEXCOORD];
		if(fb.hasRemaining()){
			out.set(fb.get(), fb.get(), 0.0f);
			return true;
		} else {
			return false;
		}
	}
	
	public FloatBuffer getBufferByType(int type){
		return this.mBuffers[type];
	}
	
	/**
	 * This handles just the count of individual vertices.
	 * @return
	 */
	public int getCount(){
		return this.mCount;
	}
	
	/**
	 * This handles getting full count of a buffer.
	 * @param type
	 * @return
	 */
	public int getCount(int type){
		if(this.mBuffers[type] != null){
			return this.mCount * VertexBuffer.STRIDES[type];
		} else {
			return 0;
		}
	}
	
	public int getStrideByType(int type){
		return VertexBuffer.STRIDES[type];
	}
	
	public boolean hasType(int type){
		return this.mBuffers[type] != null;
	}
	
	public void computeBoundingBox(AxisAlignedBox boundingBox){
		Vector3d position = new Vector3d();
		this.lock();
		while(this.nextVector3d(position, VertexBuffer.POSITION)){
			boundingBox.grow(position);
		}
		this.unlock();
	}

	//IO
	private static final long serialVersionUID = 5078756605943568977L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mTypes = (int[])in.readObject();
    	this.mCount = in.readInt();
    	this.mBuffers = new FloatBuffer[VertexBuffer.STRIDES.length];
    	this.lock();
    	for(int i = 0; i < this.mTypes.length; i++){
    		FloatBuffer buffer = this.allocateFloatBuffer(STRIDES[this.mTypes[i]]);
    		buffer.put((float[])in.readObject());
    		this.mBuffers[this.mTypes[i]] = buffer;
		}
    	this.unlock();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.mTypes);
		out.writeInt(this.mCount);
		this.lock();
		for(int i = 0; i < this.mTypes.length; i++){
			FloatBuffer buffer = this.mBuffers[this.mTypes[i]];
			float[] output = new float[buffer.capacity()];
			buffer.get(output);
			out.writeObject(output);
		}
		this.unlock();
	}

}
