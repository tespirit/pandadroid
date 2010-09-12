package com.tespirit.panda3d.geometry;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL10;

public class IndexBuffer {
	private IntBuffer buffer32;
	private ShortBuffer buffer16;
	private int count;
	private int type;
	
	public IndexBuffer(int count){
		this.count = count;
		if(count > Short.MAX_VALUE){
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 4);
			temp.order(ByteOrder.nativeOrder());
			this.buffer32 = temp.asIntBuffer();
			this.type = 0; //need to figure this part out...
		} else {
			ByteBuffer temp = ByteBuffer.allocateDirect(count * 2);
			temp.order(ByteOrder.nativeOrder());
			this.buffer16 = temp.asShortBuffer();
			this.type = GL10.GL_UNSIGNED_SHORT;
		}
	}
	
	public int getCount(){
		return this.count;
	}
	
	public void addTriangle(int v1, int v2, int v3){
		if(this.buffer16 != null){
			this.buffer16.put((short)v1);
			this.buffer16.put((short)v2);
			this.buffer16.put((short)v3);
		} else {
			this.buffer32.put(v1);
			this.buffer32.put(v2);
			this.buffer32.put(v3);
		}
	}
	
	public int getType(){
		return this.type;
	}
	
	public Buffer getBuffer(){
		return (this.buffer16!=null)?this.buffer16:this.buffer32;
	}
	
	public void resetBufferPosition(){
		if(this.buffer16 != null){
			this.buffer16.position(0);
		} else {
			this.buffer32.position(0);
		}
	}
}