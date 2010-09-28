package com.tespirit.panda3d.test;

import junit.framework.*;

import com.tespirit.bamboo.vectors.*;

public class Vector3dTest extends TestCase{

	public void setUp() throws Exception {
		Util.setTolerance(0.0000001f);
	}

	public void tearDown() throws Exception {
		Util.setTolerance(0.0000001f);
	}
	
	private void assertEqualsVector3d(float x, float y, float z, Vector3d v) {
		Assert.assertEquals(x, v.getX(),0.000001);
		Assert.assertEquals(y, v.getY(),0.000001);
		Assert.assertEquals(z, v.getZ(),0.000001);
	}

	public void testConstructor(){
		Vector3d t = new Vector3d();
		assertEqualsVector3d(0.0f, 0.0f , 0.0f, t);
		
		t = new Vector3d(1.0f,2.0f,3.0f);
		assertEqualsVector3d(1.0f, 2.0f, 3.0f, t);
	}
	
	public void testEquals(){
		Vector3d t = new Vector3d(1.0f, 2.0f, 3.0f);
		Vector3d u = new Vector3d(1.0f, 2.0f, 3.0f);
		Vector3d v = new Vector3d(3.0f, 2.0f, 3.0f);
		
		Assert.assertTrue(t.equals(u));
		Assert.assertFalse(t.equals(v));
		
		Util.setTolerance(0.1f);
		
		u.setX(u.getX()+0.05f);
		
		Assert.assertTrue(t.equals(u));
		
		Util.setTolerance(0.01f);
		Assert.assertFalse(t.equals(u));
	}
	
	public void testMagnitude(){
		Vector3d t = new Vector3d(2.0f, 0.0f, 0.0f);
		Assert.assertEquals(2.0f, t.magnitude(), 0.000001);
		Assert.assertEquals(4.0f, t.magnitude2(), 0.000001);
		
		t.setY(1.0f);
		t.setZ(3.0f);
		Assert.assertEquals(3.741657f, t.magnitude(), 0.000001);
		Assert.assertEquals(14.0f, t.magnitude2(), 0.000001);
	}
	
	public void testNormal(){
		Vector3d t = new Vector3d(2.0f, 0.0f, 0.0f);
		t.normalize();
		assertEqualsVector3d(1.0f, 0.0f, 0.0f, t);
		
		t.setY(2.0f);
		t.setZ(3.0f);
		t.normalize();

		assertEqualsVector3d(1.0f/3.741657f, 2.0f/3.741657f, 3.0f/3.741657f, t);
	}
	
	public void testScale(){
		Vector3d t = new Vector3d(1.0f, 1.0f, 1.0f);
		t.scale(2.0f);

		assertEqualsVector3d(2.0f, 2.0f, 2.0f, t);
		
		t.scale(0.5f).scale(3.0f);
		
		assertEqualsVector3d(3.0f, 3.0f, 3.0f, t);
	}
	
	public void testAdd(){
		Vector3d t = new Vector3d(1.0f, 2.0f, 3.0f);
		Vector3d u = new Vector3d(3.0f, 2.0f, 1.0f);
		Vector3d v = new Vector3d(1.0f, 1.0f, 1.0f);
		
		v.add(t, u);
		assertEqualsVector3d(4.0f, 4.0f, 4.0f, v);
		
		v.add(t);
		assertEqualsVector3d(5.0f, 6.0f, 7.0f, v);
		
		u.add(t).add(v);
		assertEqualsVector3d(9.0f, 10.0f, 11.0f, u);
	}
	
	public void testSub(){
		Vector3d t = new Vector3d(10.0f, 11.0f, 12.0f);
		Vector3d u = new Vector3d(1.0f, 2.0f, 3.0f);
		Vector3d v = new Vector3d(1.0f, 1.0f, 1.0f);
		
		v.sub(t, u);
		assertEqualsVector3d(9.0f, 9.0f, 9.0f, v);
		
		v.sub(u);
		assertEqualsVector3d(8.0f, 7.0f, 6.0f, v);
		
		t.sub(u).sub(v);
		assertEqualsVector3d(1.0f, 2.0f, 3.0f, t);
	}
	
	public void testDotAndAngle(){
		Vector3d t = new Vector3d(2.0f, 3.0f, 4.0f);
		Vector3d u = new Vector3d(4.0f, 2.0f, 3.0f);
		
		Assert.assertEquals(8+6+12, t.dot(u), 0.000001);
		
		t.set(1.0f, 0.0f, 0.0f);
		u.set(0.0f, 1.0f, 0.0f);
		Assert.assertEquals(Math.PI/2.0, t.angle(u), 0.000001);
	}
}

