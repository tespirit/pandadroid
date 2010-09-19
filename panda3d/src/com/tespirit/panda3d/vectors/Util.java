package com.tespirit.panda3d.vectors;

import java.lang.Math;

public class Util {
	private static float TOLERANCE = 0.00001f;
	
	public static void setTolerance(float t){
		Util.TOLERANCE = t;
	}
	
	public static boolean floatEquals(float f1, float f2){
		return Math.abs(f1-f2) < Util.TOLERANCE;
	}
}
