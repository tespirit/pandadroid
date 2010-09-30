package com.tespirit.bamboo.surfaces;


import com.tespirit.bamboo.render.ComponentRenderer;

public class Texture extends Surface{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1229859858483360355L;

	private String diffuseTextureName;
	
	private int diffuseTextureId;
	
	public Texture(){
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
}