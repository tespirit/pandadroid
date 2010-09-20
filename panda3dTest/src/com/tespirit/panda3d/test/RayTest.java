package com.tespirit.panda3d.test;

import com.tespirit.panda3d.vectors.AxisAlignedBox;
import com.tespirit.panda3d.vectors.Plane;
import com.tespirit.panda3d.vectors.Ray;
import com.tespirit.panda3d.vectors.Vector3d;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RayTest extends TestCase {
	public void testBoxIntersect(){
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
	
	public void testPlaneIntersect(){
		Ray r = new Ray();
		Vector3d v = new Vector3d();
		Plane p = new Plane();
		
		//simple case
		r.setPostion(0, 0, -1.0f);
		r.setDirection(0, 0, 1.0f);
		
		Vector3d result = p.rayIntersectsAt(r);
		
		Assert.assertTrue(v.equals(result));
		
		r.setPostion(1.0f, 1.0f, -1.0f);
		result = p.rayIntersectsAt(r);
		v.set(1.0f, 1.0f, 0.0f);
		
		Assert.assertTrue(v.equals(result));
		
		//more complex case!
		v.set(2, 2, 1);
		p.setNormal(v);
		
		v.set(1, 1, 1);
		p.setPoint(v);
		
		r.setPostion(-1, 2, 2);
		v.sub(r.getPosition());
		r.setDirection(v);
		
		v.set(1, 1, 1);
		result = p.rayIntersectsAt(r);
		
		Assert.assertTrue(v.equals(result));
	
		
	}
}
