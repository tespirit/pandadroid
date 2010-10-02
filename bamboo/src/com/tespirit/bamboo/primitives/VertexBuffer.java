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
	private FloatBuffer[] buffers;
	private int count;
	private int[] types;
	
	public final static int POSITION = 0;
	
	public final static int NORMAL = 1;
	
	public final static int TEXCOORD = 2;
	
	public final static int COLOR = 3;
	
	private final static int[] strides = {3,3,2,4};
	
	/**
	 * Defaultly inits all buffers.
	 * @param size
	 */
	public VertexBuffer(int count){
		this(count, new int[]{VertexBuffer.POSITION, VertexBuffer.NORMAL, VertexBuffer.TEXCOORD, VertexBuffer.COLOR});	}
	
	public VertexBuffer(int count, int[] types){
		this.buffers = new FloatBuffer[VertexBuffer.strides.length];
		this.count = count;
		this.types = types;
		for(int i = 0; i < types.length; i++){
			this.buffers[types[i]] = this.allocateFloatBuffer(strides[types[i]]);
		}
	}
	
	/**
	 * copy constructor
	 * @param vertexBuffer
	 */
	public VertexBuffer(VertexBuffer vertexBuffer){
		this(vertexBuffer.count, vertexBuffer.types);
		for(int i = 0; i < this.types.length; i++){
			FloatBuffer a = this.buffers[this.types[i]];
			FloatBuffer b = vertexBuffer.buffers[this.types[i]];
			for(int j = 0; j < this.count * strides[i]; j++){
				a.put(b.get());
			}
			a.position(0);
			b.position(0);
		}
	}
	
	public VertexBuffer(){
		
	}
	
	private FloatBuffer allocateFloatBuffer(int stride){
		ByteBuffer temp = ByteBuffer.allocateDirect(this.count*stride*4);
		temp.order(ByteOrder.nativeOrder());
		return temp.asFloatBuffer();
	}
	
	public void lock(){
		
	}
	
	public void unlock(){
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
	
	public void addPosition(Vector3d point){
		this.buffers[VertexBuffer.POSITION].put(point.getX());
		this.buffers[VertexBuffer.POSITION].put(point.getY());
		this.buffers[VertexBuffer.POSITION].put(point.getZ());
	}
	
	public void setPosition(float[] values){
		this.buffers[VertexBuffer.POSITION].put(values);
		this.buffers[VertexBuffer.POSITION].position(0);
	}
	
	public void addNormal(float x, float y, float z){
		this.buffers[VertexBuffer.NORMAL].put(x);
		this.buffers[VertexBuffer.NORMAL].put(y);
		this.buffers[VertexBuffer.NORMAL].put(z);
	}
	
	public void addNormal(Vector3d point){
		this.buffers[VertexBuffer.NORMAL].put(point.getX());
		this.buffers[VertexBuffer.NORMAL].put(point.getY());
		this.buffers[VertexBuffer.NORMAL].put(point.getZ());
	}
	
	public void setNormal(float[] values){
		this.buffers[VertexBuffer.NORMAL].put(values);
		this.buffers[VertexBuffer.NORMAL].position(0);
	}
	
	public void addTexcoord(float u, float v){
		this.buffers[VertexBuffer.TEXCOORD].put(u);
		this.buffers[VertexBuffer.TEXCOORD].put(v);
	}
	
	public void setTexcoord(float[] values){
		this.buffers[VertexBuffer.TEXCOORD].put(values);
		this.buffers[VertexBuffer.TEXCOORD].position(0);
	}
	
	public void addColor(float r, float g, float b, float a){
		this.buffers[VertexBuffer.COLOR].put(r);
		this.buffers[VertexBuffer.COLOR].put(g);
		this.buffers[VertexBuffer.COLOR].put(b);
		this.buffers[VertexBuffer.COLOR].put(a);
	}
	
	public void addColor(float r, float g, float b){
		this.buffers[VertexBuffer.COLOR].put(r);
		this.buffers[VertexBuffer.COLOR].put(g);
		this.buffers[VertexBuffer.COLOR].put(b);
		this.buffers[VertexBuffer.COLOR].put(1.0f);
	}
	
	public void setColor(float[] values){
		this.buffers[VertexBuffer.TEXCOORD].put(values);
		this.buffers[VertexBuffer.TEXCOORD].position(0);
	}
	
	public boolean nextVector3d(Vector3d out, int type){
		FloatBuffer fb = this.buffers[type];
		if(fb.hasRemaining()){
		out.set(fb.get(), fb.get(), fb.get());
			return true;
		} else {
			return false;
		}
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
			return this.count * VertexBuffer.strides[type];
		} else {
			return 0;
		}
	}
	
	public int getStride(int type){
		return VertexBuffer.strides[type];
	}
	
	public boolean hasType(int type){
		return this.buffers[type] != null;
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
		this.types = (int[])in.readObject();
    	this.count = in.readInt();
    	this.buffers = new FloatBuffer[VertexBuffer.strides.length]; 
    	for(int i = 0; i < this.types.length; i++){
			this.buffers[this.types[i]] = this.allocateFloatBuffer(strides[this.types[i]]);
			this.buffers[this.types[i]].put((float[])in.readObject());
			this.buffers[this.types[i]].position(0);
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.types);
		out.writeInt(this.count);
		for(int i = 0; i < this.types.length; i++){
			float[] output = new float[this.buffers[this.types[i]].capacity()];
			this.buffers[this.types[i]].get(output);
			out.writeObject(output);
			this.buffers[this.types[i]].position(0);
		}
	}

}
