package com.tespirit.panda3d.surfaces;

import com.tespirit.panda3d.render.ComponentRenderer;

/**
 * This is a super basic color surface renderer.
 * @author Todd Espiritu Santo
 *
 */
public class Color extends Surface{
	
	private float r;
	private float g;
	private float b;
	private float a;
	
	public Color(){
		this.setColor(0.5f, 0.5f, 0.5f, 1);
	}
	
	public void setColor(float r, float g, float b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void setColor(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void setAlpha(float a){
		this.a = a;
	}
	
	public void setRed(float r){
		this.r = r;
	}
	
	public void setGreen(float g){
		this.g = g;
	}
	
	public void setBlue(float b){
		this.b = b;
	}
	
	public float getRed(){
		return this.r;
	}
	
	public float getBlue(){
		return this.b;
	}
	
	public float getGreen(){
		return this.g;
	}
	
	public float getAlpha(){
		return this.a;
	}

	@Override
	public void render() {
		Color.renderer.render(this);
	}

	@Override
	public void setup() {
		// Void
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Color.renderer = this;
		}
		public abstract void render(Color color);
		public abstract void setup(Color color);
	}

}
