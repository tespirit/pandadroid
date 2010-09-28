package com.tespirit.bamboo.surfaces;


import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.Color4;

/**
 * This is a super basic color surface renderer.
 * @author Todd Espiritu Santo
 *
 */
public class Color extends Surface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4142008556798689478L;
	Color4 color;
	
	public Color(){
		this.color = new Color4();
		this.setColor(0.5f, 0.5f, 0.5f, 1);
	}
	
	public void setColor(float r, float g, float b, float a){
		this.color = new Color4(r,g,b,a);
	}
	
	public void setColor(float r, float g, float b){
		this.color = new Color4(r,g,b);
	}
	
	public Color4 getColor(){
		return this.color;
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
