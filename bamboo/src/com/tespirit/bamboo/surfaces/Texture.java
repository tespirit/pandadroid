package com.tespirit.bamboo.surfaces;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.ComponentRenderer;

public class Texture extends Surface implements Externalizable{

	private String mDiffuseTextureName;
	private boolean mInitialized;
	
	private int mDiffuseTextureId;
	
	public Texture(){
		this.mInitialized = false;
	}
	
	public void init(){
		if(!this.mInitialized){
			Texture.renderer.init(this);
			this.mInitialized = true;
		}
	}
	
	public void setDiffuseTextureName(String textureName){
		this.mDiffuseTextureName = textureName;
	}
	
	public void setDiffuseTextureId(int id){
		this.mDiffuseTextureId = id;
	}
	
	public String getDiffuseTextureName(){
		return this.mDiffuseTextureName;
	}
	
	public int getDiffuseTextureId(){
		return this.mDiffuseTextureId;
	}
	
	@Override
	public void render() {
		Texture.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Texture.renderer = this;
		}
		public abstract void render(Texture material);
		public abstract void init(Texture material);
	}
	
	//IO
	private static final long serialVersionUID = -2749691288631756854L;

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
    	this.mDiffuseTextureName = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(this.mDiffuseTextureName);
	}
}
