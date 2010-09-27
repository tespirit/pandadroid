package com.tespirit.panda3d.test;

import com.tespirit.bamboo.vectors.*;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AxisAlignedBoxTest extends TestCase {
	
	public void testConstructor(){
		AxisAlignedBox a = new AxisAlignedBox();
		
		Vector3d v = new Vector3d();
		Assert.assertTrue(v.equals(a.getMin()));
		Assert.assertTrue(v.equals(a.getMax()));
	}
	
	public void testGrow(){
		AxisAlignedBox a = new AxisAlignedBox();
		
		Vector3d v = new Vector3d();
		
		a.grow(v);
		Assert.assertTrue(v.equals(a.getMin()));
		Assert.assertTrue(v.equals(a.getMax()));
		
		v.set(-1, 1, 1);
		a.grow(v);
		v.set(-1, 0, 0);
		Assert.assertTrue(v.equals(a.getMin()));
		v.set(0, 1, 1);
		Assert.assertTrue(v.equals(a.getMax()));
		
		v.set(-1, 1, -2);
		a.grow(v);
		v.set(-1, 0, -2);
		Assert.assertTrue(v.equals(a.getMin()));
		v.set(0, 1, 1);
		Assert.assertTrue(v.equals(a.getMax()));
		
	}
}
