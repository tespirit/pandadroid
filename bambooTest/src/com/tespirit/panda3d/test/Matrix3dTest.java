package com.tespirit.panda3d.test;


import junit.framework.*;

import com.tespirit.bamboo.vectors.*;

public class Matrix3dTest extends TestCase{

	public void testConstructor(){
		Matrix3d m = new Matrix3d();
		Matrix3d i = Matrix3d.IDENTITY;

		Assert.assertTrue(m.equals(i));
		
		float[] f = {2.0f, 3.0f, 4.0f, 5.0f,
					 6.0f, 7.0f, 8.0f, 9.0f,
					 10.0f, 11.0f, 12.0f, 13.0f,
					 15.0f, 16.0f, 17.0f, 18.0f};
		
		m = new Matrix3d(f);
		Assert.assertEquals(f, m.getBuffer());
	}
	
	public void testEquals(){
		Matrix3d m = new Matrix3d();
		Assert.assertTrue(m.equals(Matrix3d.IDENTITY));
		
		m.setValue(2.0f, 0);	
		Assert.assertFalse(m.equals(Matrix3d.IDENTITY));
		
		float[] f = {2.0f, 3.0f, 4.0f, 5.0f,
				 6.0f, 7.0f, 8.0f, 9.0f,
				 10.0f, 11.0f, 12.0f, 13.0f,
				 15.0f, 16.0f, 17.0f, 18.0f};
		
		m = new Matrix3d(f);
		Matrix3d m2 = m.clone();
		
		Assert.assertTrue(m.equals(m2));
		Assert.assertFalse(m.getBuffer() == m2.getBuffer());
	}
	
	public void testScale() {
		Matrix3d m = new Matrix3d();
		Matrix3d m2 = new Matrix3d(2, 0, 0,
								   0, 2, 0,
								   0, 0, 2,
								   0, 0, 0);
		m.scale(2);
		
		Assert.assertTrue(m.equals(m2));
		
		m.scale(0.5f);
		
		m.scale(new Vector3d(3, 4, 5));
		
		m2.setValue(3, 0, 0);
		m2.setValue(4, 1, 1);
		m2.setValue(5, 2, 2);
		
		Assert.assertTrue(m.equals(m2));
	}
	
	public void testTranslate(){
		Matrix3d m = new Matrix3d();
		Matrix3d m2 = new Matrix3d();
		
		Vector3d v = new Vector3d(2, 3, 4);
		
		m.translate(v);
		
		m2.getTranslation().set(2, 3, 4);
		
		Assert.assertTrue(m.equals(m2));
		
		m.translate(1, 2, 3);
		
		m2.getTranslation().set(3, 5, 7);

		Assert.assertTrue(m.equals(m2));
	}
	
	public void testRotate(){
		Matrix3d m = new Matrix3d();
		
		m.rotateX((float)Math.PI/2);		
	}

	public void testMultiply(){
		float[] buffer = Matrix3d.createBuffer(3);
		float[] buffer2 = Matrix3d.createBuffer(5);
		Matrix3d m = new Matrix3d();
		Matrix3d i = Matrix3d.IDENTITY;
		
		//super simple case
		m.multiply(i, i);
		Assert.assertTrue(m.equals(i));
		
		m = new Matrix3d(2, 2, 2, 
						 2, 2, 2, 
						 2, 2, 2, 
						 2, 2, 2);
		
		Matrix3d c = m.clone();
		
		m.multiply(i);
		
		Assert.assertTrue(m.equals(c));
		
		/*
		 * 1 2 3 1		1 0 2 1		5 8 5 15
		 * 0 1 2 2		2 1 0 2		2 5 2 10
		 * 3 0 1 3		0 2 1 3		3 2 7 9
		 * 0 0 0 1		0 0 0 1		0 0 0 1
		 */
		
		c = new Matrix3d(1, 0, 3, 
						 2, 1, 0, 
						 3, 2, 1, 
						 1, 2, 3);
		
		Matrix3d cb = new Matrix3d(buffer);
		Matrix3d cb2 = new Matrix3d(buffer2, Matrix3d.SIZE*2);
		cb.copy(c);
		cb2.copy(c);
		
		m = new Matrix3d(1, 2, 0,
						 0, 1, 2,
						 2, 0, 1,
						 1, 2, 3);
		
		Matrix3d mb = new Matrix3d(buffer, Matrix3d.SIZE);
		Matrix3d mb2 = new Matrix3d(buffer2, Matrix3d.SIZE);
		mb.copy(m);
		mb2.copy(m);
		
		Matrix3d r = new Matrix3d(5, 2, 3,
								  8, 5, 2,
								  5, 2, 7,
								  15, 10, 9);
		
		Matrix3d rb = new Matrix3d(buffer, Matrix3d.SIZE*2);
		Matrix3d rb2 = new Matrix3d(buffer2, Matrix3d.SIZE*3);
		rb.copy(r);
		rb2.copy(r);
		
		Matrix3d t = new Matrix3d();
		t.multiply(c,m);
		Assert.assertTrue(t.equals(r));
		
		Matrix3d tb2 = new Matrix3d(buffer2, Matrix3d.SIZE*4);
		tb2.multiply(cb2, mb2);
		Assert.assertTrue(t.equals(rb2));
		
		m.multiply(c);
		
		Assert.assertTrue(m.equals(r));
		
		mb.multiply(cb);
		mb2.multiply(cb2);
		
		Assert.assertTrue(mb.equals(rb));
		Assert.assertTrue(mb2.equals(rb2));
		
		
	
		/*
		 * 1 0 2 1		1 2 3 1		7 2 5 8
		 * 2 1 0 2		0 1 2 2		2 5 8 6
		 * 0 2 1 3		3 0 1 3		3 2 5 10
		 * 0 0 0 1		0 0 0 1		0 0 0 1
		 */
		m = new Matrix3d(1, 2, 0,
						 0, 1, 2,
						 2, 0, 1,
						 1, 2, 3);
		
		mb.copy(m);
		mb2.copy(m);
		
		r = new Matrix3d(7, 2, 3,
						 2, 5, 2,
						 5, 8, 5,
						 8, 6, 10);
		
		rb.copy(r);
		rb2.copy(r);
		
		t.multiply(m,c);
		Assert.assertTrue(t.equals(r));
		tb2.multiply(cb2, mb2);
		Assert.assertTrue(t.equals(rb2));
		

		c.multiply(m);

		Assert.assertTrue(c.equals(r));
		
		cb.multiply(mb);
		cb2.multiply(mb2);
		Assert.assertTrue(cb.equals(rb));
	}
	
	public void testInvert(){
		//Matrix3d m = new Matrix3d();
	}
}
