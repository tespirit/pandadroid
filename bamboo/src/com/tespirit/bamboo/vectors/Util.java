package com.tespirit.bamboo.vectors;

import java.lang.Math;

public class Util {
	private static int SPREAD = 0x00000800;
	
	public static void setSpread(int s){
		Util.SPREAD = s;
	}
	
	public static boolean floatEquals(float f1, float f2){
		int i1 = Float.floatToRawIntBits(f1);
		int i2 = Float.floatToRawIntBits(f2);
		if(i1 < 0){
			i1 = 0x80000000 - i1;
		}
		if(i2 < 0){
			i2 = 0x80000000 - i2;
		}
		i1 = Math.abs(i1-i2);
		return i1 < SPREAD;
	}
	
	public static float mod(float lval, float rval){
		return lval - rval*(float)Math.floor(lval/rval);
	}
}
