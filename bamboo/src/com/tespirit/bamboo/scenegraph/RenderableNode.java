package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Matrix3d;

public interface RenderableNode {
	/**
	 * This is called every frame in rendering the scene.
	 */
	public void render();
	
	public Matrix3d getWorldTransform();
	
	public AxisAlignedBox getBoundingBox();
}
