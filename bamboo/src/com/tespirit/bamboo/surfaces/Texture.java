package com.tespirit.bamboo.surfaces;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.ComponentRenderer;

public class Texture extends Surface implements Externalizable{

	private String diffuseTextureName;
	
	private int diffuseTextureId;
	
	public Texture(){
		this.init();
	}
	
	public void init(){
		TextureManager.getInstance().addTexture(this);
	}
	
	public void setDiffuseTextureName(String textureName){
		this.diffuseTextureName = textureName;
	}
	
	public void setDiffuseTextureId(int id){
		this.diffuseTextureId = id;
	}
	
	public String getDiffuseTextureName(){
		return this.diffuseTextureName;
	}
	
	public int getDiffuseTextureId(){
		return this.diffuseTextureId;
	}
	
	@Override
	public void render() {
		Texture.renderer.render(this);
	}

	@Override
	public void setup() {
		Texture.renderer.setup(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Texture.renderer = this;
		}
		public abstract void render(Texture material);
		public abstract void setup(Texture material);
	}
	
	//IO
	private static final long serialVersionUID = -2749691288631756854L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.init();
    	this.diffuseTextureName = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.diffuseTextureName);
	}
}
