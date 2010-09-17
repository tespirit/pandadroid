package com.tespirit.panda3d.scenegraph;

import com.tespirit.panda3d.vectors.Matrix3d;

public interface RenderableNode {
	/**
	 * This is called every frame in rendering the scene.
	 */
	public void render();
	
	/**
	 * This is called when the renderer is initialized and created.
	 */
	public void setup();
	
	public Matrix3d getWorldTransform();
	
	/**
	 * This is called if the view size changes objects that
	 * don't need to consider this can just implement a stub.
	 * @param width
	 * @param height
	 */
	public void setDisplay(int width, int height);
}
