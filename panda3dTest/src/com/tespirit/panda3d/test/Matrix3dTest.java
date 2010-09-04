package com.tespirit.panda3d.test;


import junit.framework.*;

import com.tespirit.panda3d.vectors.*;

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
		Matrix3d m2 = new Matrix3d(2.0f, 0.0f, 0.0f, 0.0f,
								   0.0f, 2.0f, 0.0f, 0.0f,
								   0.0f, 0.0f, 2.0f, 0.0f);
		m.scale(2.0f);
		
		Assert.assertTrue(m.equals(m2));
		
		m.scale(0.5f);
		
		m.scale(new Vector3d(3.0f, 4.0f, 5.0f));
		
		m2.setValue(3.0f, 0, 0);
		m2.setValue(4.0f, 1, 1);
		m2.setValue(5.0f, 2, 2);
		
		Assert.assertTrue(m.equals(m2));
	}
	
	public void testTranslate(){
		Matrix3d m = new Matrix3d();
		Matrix3d m2 = new Matrix3d();
		
		Vector3d v = new Vector3d(2.0f, 3.0f, 4.0f);
		
		m.translate(v);
		
		m2.setValue(2.0f, 3,0);
		m2.setValue(3.0f, 3,1);
		m2.setValue(4.0f, 3,2);
		
		Assert.assertTrue(m.equals(m2));
		
		m.translate(1.0f, 2.0f, 3.0f);
		
		m2.setValue(3.0f, 3,0);
		m2.setValue(5.0f, 3,1);
		m2.setValue(7.0f, 3,2);

		Assert.assertTrue(m.equals(m2));
	}
	
	public void testRotate(){
		Matrix3d m = new Matrix3d();
		
		m.rotateX((float)Math.PI/2);		
	}

	public void testMultiply(){
		Matrix3d m = new Matrix3d();
		Matrix3d i = Matrix3d.IDENTITY;
		
		//super simple case
		m.multiply(i, i);
		Assert.assertTrue(m.equals(i));
		
		m = new Matrix3d(2.0f, 2.0f, 2.0f, 2.0f,
						 2.0f, 2.0f, 2.0f, 2.0f,
						 2.0f, 2.0f, 2.0f, 2.0f);
		
		Matrix3d c = m.clone();
		
		m.multiply(i);
		
		Assert.assertTrue(m.equals(c));
		
		/*
		 * 1 2 3 1		1 0 2 1		5 8 5 15
		 * 0 1 2 2		2 1 0 2		2 5 2 10
		 * 3 0 1 3		0 2 1 3		3 2 7 9
		 * 0 0 0 1		0 0 0 1		0 0 0 1
		 */
		
		c = new Matrix3d(1.0f, 2.0f, 3.0f, 1.0f,
		 		 		 0.0f, 1.0f, 2.0f, 2.0f,
		 		 		 3.0f, 0.0f, 1.0f, 3.0f);
		
		
		m = new Matrix3d(1.0f, 0.0f, 2.0f, 1.0f,
				 		 2.0f, 1.0f, 0.0f, 2.0f,
				 		 0.0f, 2.0f, 1.0f, 3.0f);
		
		Matrix3d r = new Matrix3d(5.0f, 8.0f, 5.0f, 15.0f,
		 		 				  2.0f, 5.0f, 2.0f, 10.0f,
		 		 				  3.0f, 2.0f, 7.0f, 9.0f);
		
		m.multiply(c);
		
		Assert.assertTrue(m.equals(r));
	
		/*
		 * 1 0 2 1		1 2 3 1		7 2 5 8
		 * 2 1 0 2		0 1 2 2		2 5 8 6
		 * 0 2 1 3		3 0 1 3		3 2 5 10
		 * 0 0 0 1		0 0 0 1		0 0 0 1
		 */
		m = new Matrix3d(1.0f, 0.0f, 2.0f, 1.0f,
		 		 		 2.0f, 1.0f, 0.0f, 2.0f,
		 		 		 0.0f, 2.0f, 1.0f, 3.0f);

		r = new Matrix3d(7.0f, 2.0f, 5.0f, 8.0f,
		  		 		 2.0f, 5.0f, 8.0f, 6.0f,
		  		 		 3.0f, 2.0f, 5.0f, 10.0f);

		c.multiply(m);

		Assert.assertTrue(c.equals(r));
	}
	
	public void testInvert(){
		//Matrix3d m = new Matrix3d();
	}
}
