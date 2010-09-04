package com.tespirit.panda3d.test;

import com.tespirit.panda3d.geometry.VertexBuffer;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VertexBufferTest extends TestCase {
	public void testConstructor(){
		VertexBuffer v = new VertexBuffer(12);
		
		Assert.assertEquals(12, v.getCount());
		Assert.assertEquals(12*3, v.getCount(VertexBuffer.POSITION));
		Assert.assertEquals(12*3, v.getCount(VertexBuffer.NORMAL));
		Assert.assertEquals(12*2, v.getCount(VertexBuffer.TEXCOORD));
	}
	
	public void testManupulation(){
		VertexBuffer v = new VertexBuffer(2);
		
		v.addPosition(1.0f, 2.0f, 3.0f);
		v.addPosition(4.0f, 5.0f, 6.0f);
		
		v.resetBufferPosition();
		
	}
}
