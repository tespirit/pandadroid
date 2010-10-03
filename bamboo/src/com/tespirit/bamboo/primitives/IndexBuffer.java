package com.tespirit.bamboo.primitives;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.Buffer;

public class IndexBuffer implements Externalizable{
	private IntBuffer mBuffer32;
	private ShortBuffer mBuffer16;
	private ByteBuffer mBuffer8;
	private Buffer mBuffer;
	private int mCount;
	private int mType;
	
	public static final int BUFFER32 = 0;
	public static final int BUFFER16 = 1;
	public static final int BUFFER8 = 2;
	
	public static final int TYPE_COUNT = 3;
	
	public IndexBuffer(int count){
		this.mCount = count;
		ByteBuffer temp = ByteBuffer.allocateDirect(count * 4);
		temp.order(ByteOrder.nativeOrder());
		this.mBuffer16 = temp.asShortBuffer();
		this.mBuffer = this.mBuffer16;
		this.mType = BUFFER16;
	}
	
	public IndexBuffer(int count, int maxValue){
		this.mCount = count;
		if(maxValue > Short.MAX_VALUE - Short.MIN_VALUE){
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 4);
			temp.order(ByteOrder.nativeOrder());
			this.mBuffer32 = temp.asIntBuffer();
			this.mBuffer = this.mBuffer32;
			this.mType = BUFFER32;
		} else if(maxValue > Byte.MAX_VALUE - Byte.MIN_VALUE){
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 2);
			temp.order(ByteOrder.nativeOrder());
			this.mBuffer16 = temp.asShortBuffer();
			this.mBuffer = this.mBuffer16;
			this.mType = BUFFER16;
		} else {
			this.mBuffer8 = ByteBuffer.allocateDirect(count * 2);
			this.mBuffer8.order(ByteOrder.nativeOrder());
			this.mBuffer = this.mBuffer8;
			this.mType = BUFFER8;
		}
	}
	
	public IndexBuffer(){
		
	}
	
	public void lock(){
		
	}
	
	public void unlock(){
		this.mBuffer.position(0);
	}
	
	public int getCount(){
		return this.mCount;
	}
	
	public void addTriangle(int v1, int v2, int v3){
		if(this.mBuffer8 != null){
			this.mBuffer8.put((byte)v1);
			this.mBuffer8.put((byte)v2);
			this.mBuffer8.put((byte)v3);
		} else if(this.mBuffer16 != null){
			this.mBuffer16.put((short)v1);
			this.mBuffer16.put((short)v2);
			this.mBuffer16.put((short)v3);
		} else {
			this.mBuffer32.put(v1);
			this.mBuffer32.put(v2);
			this.mBuffer32.put(v3);
		}
	}
	
	public void addLine(int v1, int v2){
		if(this.mBuffer8 != null){
			this.mBuffer8.put((byte)v1);
			this.mBuffer8.put((byte)v2);
		} else if(this.mBuffer16 != null){
			this.mBuffer16.put((short)v1);
			this.mBuffer16.put((short)v2);
		} else {
			this.mBuffer32.put(v1);
			this.mBuffer32.put(v2);
		}
	}
	
	public int getType(){
		return this.mType;
	}
	
	public Buffer getBuffer(){
		return this.mBuffer;
	}
	
	//IO
	private static final long serialVersionUID = 446637802592556955L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.mType = in.readInt();
    	this.mCount = in.readInt();
    	
    	ByteBuffer temp = ByteBuffer.allocateDirect(mCount * 4);
		temp.order(ByteOrder.nativeOrder());
    	switch(this.mType){
		case BUFFER32:
			this.mBuffer32 = temp.asIntBuffer();
			this.mBuffer32.put((int[])in.readObject());
			this.mBuffer = mBuffer32;
			break;
		case BUFFER16:
			this.mBuffer16 = temp.asShortBuffer();
			this.mBuffer16.put((short[])in.readObject());
			this.mBuffer = mBuffer16;
			break;
		case BUFFER8:
			this.mBuffer8 = temp;
			this.mBuffer8.put((byte[])in.readObject());
			this.mBuffer = mBuffer8;
			break;
		}
    	this.mBuffer.position(0);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(this.mType);
		out.writeInt(this.mCount);
		this.lock();
		switch(this.mType){
		case BUFFER32:
			int[] output32 = new int[this.mCount];
			this.mBuffer32.get(output32);
			out.writeObject(output32);
			break;
		case BUFFER16:
			short[] output16 = new short[this.mCount];
			this.mBuffer16.get(output16);
			out.writeObject(output16);
			break;
		case BUFFER8:
			byte[] output8 = new byte[this.mCount];
			this.mBuffer8.get(output8);
			out.writeObject(output8);
			break;
		}
		this.unlock();
	}
}