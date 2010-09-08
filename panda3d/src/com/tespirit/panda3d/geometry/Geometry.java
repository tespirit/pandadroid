package com.tespirit.panda3d.geometry;

import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.vectors.AxisAlignedBox;

public abstract class Geometry {
	abstract public void render(GL10 gl);
	
	abstract public AxisAlignedBox getBoundingBox();
}
