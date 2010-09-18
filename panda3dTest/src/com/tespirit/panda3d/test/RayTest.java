package com.tespirit.panda3d.test;

import com.tespirit.panda3d.vectors.AxisAlignedBox;
import com.tespirit.panda3d.vectors.Ray;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RayTest extends TestCase {
	public void testIntersect(){
		Ray r = new Ray();
		AxisAlignedBox bb = new AxisAlignedBox(0,0,0,1,1,1);
		
		//simple tests
		
		//xy plane
		r.setDirection(0, 0, 1);
		r.setPostion(0.5f, 0.5f, -1);
		
		Assert.assertTrue(bb.intersectsRay(r));
		
		r.setPostion(1.5f, 1.5f, -1);
		
		Assert.assertFalse(bb.intersectsRay(r));
		
		//xz plane
		r.setDirection(0, 1, 0);
		r.setPostion(0.5f, -1, 0.5f);
		
		Assert.assertTrue(bb.intersectsRay(r));
		
		r.setPostion(1.5f, -1, 1.5f);
		
		Assert.assertFalse(bb.intersectsRay(r));
		
		//yz plane
		r.setDirection(1, 0, 0);
		r.setPostion(-1, 0.5f, 0.5f);
		
		Assert.assertTrue(bb.intersectsRay(r));
		
		r.setPostion(-1, 1.5f, 1.5f);
		
		Assert.assertFalse(bb.intersectsRay(r));
		
		//now more complex tests!
		r.setDirection(1, 1, 1);
		r.setPostion(-1f, -1f, -1f);
		
		Assert.assertTrue(bb.intersectsRay(r));
		
		r.setPostion(-3, -1, -1);
		
		Assert.assertFalse(bb.intersectsRay(r));
		
		r.setDirection(-1, -1, -1);
		r.setPostion(2, 2, 2);
		
		Assert.assertTrue(bb.intersectsRay(r));
		
		r.setPostion(4, 2, 2);
		
		Assert.assertFalse(bb.intersectsRay(r));
	}
}
