package com.tespirit.panda3d.geometry;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;

public class VertexBuffer {	
	private FloatBuffer[] buffers;
	private int count;
	
	public final static int POSITION = 0;
	
	public final static int NORMAL = 1;
	
	public final static int TEXCOORD = 2;
	
	private final static int[] STRIDES = {3,3,2};
	
	/**
	 * Defaultly inits all buffers.
	 * @param size
	 */
	public VertexBuffer(int count){
		this.buffers = new FloatBuffer[VertexBuffer.STRIDES.length];
		this.count = count;
		for(int i = 0; i < STRIDES.length; i++){
			this.buffers[i] = this.allocateFloatBuffer(STRIDES[i]);
		}
	}
	
	public VertexBuffer(int count, int[] types){
		this.buffers = new FloatBuffer[VertexBuffer.STRIDES.length];
		this.count = count;
		for(int i = 0; i < types.length; i++){
			this.buffers[types[i]] = this.allocateFloatBuffer(STRIDES[types[i]]);
		}
	}
	
	private FloatBuffer allocateFloatBuffer(int stride){
		ByteBuffer temp = ByteBuffer.allocateDirect(this.count*stride*4);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	
	public void resetBufferPosition(){
		for(int i = 0; i < this.buffers.length; i++){
			if(this.buffers[i] != null){
				this.buffers[i].position(0);
			}
		}
	}
	
	public void addPosition(float x, float y, float z){
		this.buffers[VertexBuffer.POSITION].put(x);
		this.buffers[VertexBuffer.POSITION].put(y);
		this.buffers[VertexBuffer.POSITION].put(z);
	}
	
	public void addNormal(float x, float y, float z){
		this.buffers[VertexBuffer.NORMAL].put(x);
		this.buffers[VertexBuffer.NORMAL].put(y);
		this.buffers[VertexBuffer.NORMAL].put(z);
	}
	
	public FloatBuffer getBuffer(int type){
		return this.buffers[type];
	}
	
	/**
	 * This handles just the count of individual vertices.
	 * @return
	 */
	public int getCount(){
		return this.count;
	}
	
	/**
	 * This handles getting full count of a buffer.
	 * @param type
	 * @return
	 */
	public int getCount(int type){
		if(this.buffers[type] != null){
			return this.count * VertexBuffer.STRIDES[type];
		} else {
			return 0;
		}
	}
	
	public int getStride(int type){
		return VertexBuffer.STRIDES[type];
	}
}
