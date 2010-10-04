package com.tespirit.bamboo.render;

/**
 * This is an interface for nodes that need to be initialized dynamically. 
 * @author Todd Espiritu Santo
 *
 */
public interface Resource {
	/**
	 * This sets up the resource
	 */
	public void init();
}
