package com.tespirit.panda3d.test;

import com.tespirit.panda3d.geometry.VertexBuffer;
import com.tespirit.panda3d.vectors.Vector3d;

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
		
		Vector3d p1 = new Vector3d();
		Vector3d p2 = new Vector3d(1.0f, 2.0f, 3.0f);
		
		v.nextVector3d(p1, VertexBuffer.POSITION);
		Assert.assertTrue(p1.equals(p2));
		
		v.nextVector3d(p1, VertexBuffer.POSITION);
		p2.set(4.0f, 5.0f, 6.0f);
		Assert.assertTrue(p1.equals(p2));
		
		v.resetBufferPosition();
		
		Assert.assertEquals(6, v.getCount(VertexBuffer.POSITION));
		Assert.assertEquals(2, v.getCount());
	}
}
