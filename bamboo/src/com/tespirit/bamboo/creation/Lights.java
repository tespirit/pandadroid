package com.tespirit.bamboo.creation;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Light;

/**
 * Place any simple configured light setups here. Obviously bam files with lights 
 * is the way to share lighting setups, though this is a fail safe way to
 * get started with using Bamboo.
 * @author Todd Espiritu Santo
 *
 */
public class Lights {
	public static void addDefaultLight(RenderManager r){
		Light light = new Light();
		light.setPosition(50.0f, 50.0f, 50.0f);
		light.setDiffuse(1, 1, 0.85f);
		light.setAmbient(0.2f, 0.2f, 0.1f);
		r.addScene(light);
		light = new Light();
		light.setPosition(-50, -20, 30);
		light.setDiffuse(0.2f, 0.2f, 0.25f);
		light.setSpecular(0, 0, 0);
		light.setAmbient(0, 0, 0.1f);
		r.addScene(light);
		light = new Light();
		light.setPosition(-30, 5, -50);
		light.setDiffuse(0.3f, 0.3f, 0.3f);
		light.setAmbient(0, 0, 0);
		light.setSpecular(0, 0, 0);
		r.addScene(light);
	}
}
