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
	
	public void testCenter(){
		AxisAlignedBox a = new AxisAlignedBox();
		a.setMin(-1,-1,-1);
		a.setMax(1,1,1);
		
		Vector3d v = new Vector3d();
		
		Assert.assertTrue(v.equals(a.getCenter()));
		
		a.setMin(-2,-1,0);
		a.setMax(0,1,2);
		
		v.set(-1, 0, 1);
		
		Assert.assertTrue(v.equals(a.getCenter()));
		
	}
	
	public void testRadius(){
		AxisAlignedBox a = new AxisAlignedBox();
		a.setMin(-1,-1,-1);
		a.setMax(1,1,1);
		Assert.assertTrue(Util.floatEquals((float)Math.sqrt(3), a.getRadius()));
		
		a.setMin(-2,-3,0);
		a.setMax(0,1,4);
		
		Assert.assertTrue(Util.floatEquals(3.0f, a.getRadius()));
	}
}
