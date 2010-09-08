package com.tespirit.panda3d.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.tespirit.panda3d.geometry.*;

public class IndexBufferTest extends TestCase {
	
	public void testConstructor(){
		IndexBuffer i = new IndexBuffer(12);
		
		Assert.assertEquals(12, i.getCount());
	}
	
	public void testManupulation(){
		IndexBuffer i = new IndexBuffer(6);
		
		i.addTriangle(1, 2, 3);
		i.addTriangle(4, 5, 6);
		
		i.resetBufferPosition();
		
		Assert.assertEquals(6, i.getCount());
	}
	
	
}
