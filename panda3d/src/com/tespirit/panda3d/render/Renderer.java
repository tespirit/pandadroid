package com.tespirit.panda3d.render;

import java.util.ArrayList;

import com.tespirit.panda3d.surfaces.TextureManager;
import com.tespirit.panda3d.scenegraph.*;
import com.tespirit.panda3d.vectors.Matrix3d;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public abstract class Renderer {
	private Node root;
	private Camera camera;
	private LightGroup lights;
	private TextureManager textures;
	private ArrayList<ComponentRenderer> renderers;
	
	public Renderer(){
		super();
		this.root = null;
		//change this later.
		this.textures = TextureManager.getInstance();
		this.renderers = new ArrayList<ComponentRenderer>();
	}
	
	public void setSceneGraph(Node root){
		this.root = root;
	}
	
	public Node getSceneGraph(){
		return this.root;
	}
	
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	
	public Camera getCamera(){
		return this.camera;
	}
	
	public void setLightGroup(LightGroup lights){
		this.lights = lights;
	}
	
	public LightGroup getLightGroup(){
		return this.lights;
	}
	
	public void renderScene(){
		if(this.camera != null){
			this.camera.render();
		}
		
		if(this.lights != null){
			for(int i = 0; i < this.lights.getChildCount(); i++){
				((Light)this.lights.getChild(i)).render();
			}
		}
		
		if(this.root != null){
			this.traverseNode(this.root);
		}
		if(this.camera != null){
			this.popMatrix();
		}
	}
	
	/**
	 * This traverses nodes and correctly pushes and pops matrices.
	 * @param node
	 */
	public void traverseNode(Node node){
		if(node.getTransform() != null) {
			this.pushMatrix(node.getTransform());
		}
		if(node instanceof RenderableNode){
			((RenderableNode)node).render();
		} else {
			for(int i = 0; i < node.getChildCount(); i++){
				this.traverseNode(node.getChild(i));
			}
		}
		if(node.getTransform() != null){
			this.popMatrix();
		}
	}
	
	public void traverseSetup(Node node){
		if(node instanceof RenderableNode){
			((RenderableNode)node).setup();
		} else {
			for(int i = 0; i < node.getChildCount(); i++){
				traverseSetup(node.getChild(i));
			}
		}
	}
	
	public void traverseSetDisplay(Node node, int width, int height){
		if(node instanceof RenderableNode){
			((RenderableNode)node).setDisplay(width, height);
		} else {
			for(int i = 0; i < node.getChildCount(); i++){
				traverseSetDisplay(node.getChild(i), width, height);
			}
		}
	}

	/**
	 * This should be called to initialize any render settings before rendering
	 * takes place.
	 */
	public void setupRender(){
		this.reactivateComponentRenderers();
		if(this.lights != null){
			this.enableLights();
			this.traverseSetup(this.lights);
		}
		if(this.textures != null){
			this.enableTextures();
			for(int i = 0; i < this.textures.getTextureCount(); i++){
				this.textures.getTexture(i).setup();
			}
		}
	}

	/**
	 * This should be called any time when setting up the view.
	 * @param width
	 * @param height
	 */
	public void setDisplay(int width, int height){
		if(this.camera != null){
			this.traverseSetDisplay(this.camera, width, height);
		}
	}
	
	/* scenegraph manipulation */
	public abstract void popMatrix();
	public abstract void pushMatrix(Matrix3d transform);
	
	/* render settings */
	public abstract void enableTextures();
	public abstract void enableLights();
	
	public void addComponentRenderer(ComponentRenderer r){
		this.renderers.add(r);
	}
	
	public void reactivateComponentRenderers(){
		for(int i = 0; i < this.renderers.size(); i++){
			this.renderers.get(i).activate();
		}
	}
}
