package com.tespirit.panda3d.test;

import com.tespirit.panda3d.vectors.*;

import junit.framework.Assert;
import junit.framework.TestCase;

public class AxisAlignedBoxTest extends TestCase {
	
	public void testConstructor(){
		AxisAlignedBox a = new AxisAlignedBox();
		
		Vector3d v = new Vector3d();
		Assert.assertTrue(v.equals(a.min));
		Assert.assertTrue(v.equals(a.max));
	}
	
	public void grow(){
		AxisAlignedBox a = new AxisAlignedBox();
		
		Vector3d v = new Vector3d();
		
		a.grow(v);
		Assert.assertTrue(v.equals(a.min));
		Assert.assertTrue(v.equals(a.max));
		
		v.set(-1, 1, 1);
		a.grow(v);
		v.set(-1, 0, 0);
		Assert.assertTrue(v.equals(a.min));
		v.set(0, 1, 1);
		Assert.assertTrue(v.equals(a.max));
		
		v.set(-1, 1, -2);
		a.grow(v);
		v.set(-1, 0, -2);
		Assert.assertTrue(v.equals(a.min));
		v.set(0, 1, 1);
		Assert.assertTrue(v.equals(a.max));
		
	}
}
