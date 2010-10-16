package com.tespirit.bamboo.surfaces;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.Color4;

/**
 * This is a super basic color surface renderer.
 * @author Todd Espiritu Santo
 *
 */
public class Color extends Surface implements Externalizable{
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
	public void renderStart() {
		Color.renderer.renderStart(this);
	}
	
	@Override
	public void renderEnd() {
		Color.renderer.renderEnd(this);
	}

	@Override
	public void init() {
		// Void
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Color.renderer = this;
		}
		public abstract void renderStart(Color color);
		public abstract void renderEnd(Color color);
		public abstract void setup(Color color);
	}

	//IO
	private static final long serialVersionUID = -4275648857119940383L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		super.read(in);
		this.color = new Color4();
    	this.color.set(in.readFloat(),in.readFloat(),in.readFloat(),in.readFloat());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.write(out);
		out.writeFloat(this.color.getRed());
		out.writeFloat(this.color.getGreen());
		out.writeFloat(this.color.getBlue());
		out.writeFloat(this.color.getAlpha());
	}

}
