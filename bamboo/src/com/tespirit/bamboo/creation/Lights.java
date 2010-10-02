package com.tespirit.bamboo.creation;

import com.tespirit.bamboo.render.Light;
import com.tespirit.bamboo.render.Renderer;

/**
 * Place any simple configured light setups here. Obviously bam files with lights 
 * is the way to share lighting setups, though this is a fail safe way to
 * get started with using Bamboo.
 * @author Todd Espiritu Santo
 *
 */
public class Lights {
	public static void addDefaultLight(Renderer r){
		Light light = new Light();
		light.setPosition(50.0f, 50.0f, 50.0f);
		r.addNode(light);
	}
}
