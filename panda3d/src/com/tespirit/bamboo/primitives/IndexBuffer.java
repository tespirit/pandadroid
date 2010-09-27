package com.tespirit.bamboo.primitives;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.Buffer;

public class IndexBuffer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2531725665599885532L;
	private IntBuffer buffer32;
	private ShortBuffer buffer16;
	private ByteBuffer buffer8;
	private Buffer buffer;
	private int count;
	private int type;
	
	public static void setTypeEnum(int int32, int short16, int byte8){
		IndexBuffer.types[0] = int32;
		IndexBuffer.types[1] = short16;
		IndexBuffer.types[2] = byte8;
	}
	
	private static int[] types = {0,1,2};
	
	public IndexBuffer(int count){
		this.count = count;
		ByteBuffer temp = ByteBuffer.allocateDirect(count * 4);
		temp.order(ByteOrder.nativeOrder());
		this.buffer16 = temp.asShortBuffer();
		this.buffer = this.buffer16;
		this.type = 1;
	}
	
	public IndexBuffer(int count, int maxValue){
		this.count = count;
		if(maxValue > Short.MAX_VALUE - Short.MIN_VALUE){
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 4);
			temp.order(ByteOrder.nativeOrder());
			this.buffer32 = temp.asIntBuffer();
			this.buffer = this.buffer32;
			this.type = 0;
		} else if(maxValue > Byte.MAX_VALUE - Byte.MIN_VALUE){
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 2);
			temp.order(ByteOrder.nativeOrder());
			this.buffer16 = temp.asShortBuffer();
			this.buffer = this.buffer16;
			this.type = 1;
		} else {
			this.buffer8 = ByteBuffer.allocateDirect(count * 2);
			this.buffer8.order(ByteOrder.nativeOrder());
			this.buffer = this.buffer8;
			this.type = 2;
		}
	}
	
	public int getCount(){
		return this.count;
	}
	
	public void addTriangle(int v1, int v2, int v3){
		if(this.buffer8 != null){
			this.buffer8.put((byte)v1);
			this.buffer8.put((byte)v2);
			this.buffer8.put((byte)v3);
		} else if(this.buffer16 != null){
			this.buffer16.put((short)v1);
			this.buffer16.put((short)v2);
			this.buffer16.put((short)v3);
		} else {
			this.buffer32.put(v1);
			this.buffer32.put(v2);
			this.buffer32.put(v3);
		}
	}
	
	public void addLine(int v1, int v2){
		if(this.buffer8 != null){
			this.buffer8.put((byte)v1);
			this.buffer8.put((byte)v2);
		} else if(this.buffer16 != null){
			this.buffer16.put((short)v1);
			this.buffer16.put((short)v2);
		} else {
			this.buffer32.put(v1);
			this.buffer32.put(v2);
		}
	}
	
	public int getTypeEnum(){
		return IndexBuffer.types[this.type];
	}
	
	public Buffer getBuffer(){
		return this.buffer;
	}
	
	public void resetBufferPosition(){
		this.buffer.position(0);
	}
}