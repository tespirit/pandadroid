package com.tespirit.panda3d.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tespirit.panda3d.primitives.Box;
import com.tespirit.panda3d.surfaces.Material;
import com.tespirit.panda3d.surfaces.TextureManager;
import com.tespirit.panda3d.scenegraph.*;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Ray;

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
	
	private ArrayList<RenderableNode> renderableNodes;
	
	private static final RenderableSort renderableSort = new RenderableSort();
	
	private static class RenderableSort implements Comparator<RenderableNode>{

		@Override
		public int compare(RenderableNode object1, RenderableNode object2) {
			float z1 = object1.getWorldTransform().getTranslation().getZ();
			float z2 = object2.getWorldTransform().getTranslation().getZ();
			if(z1 < z2) return 1;
			else if(z1 > z2) return -1;
			else return 0;
		}
		
	}
	
	public Renderer(){
		super();
		this.root = null;
		//change this later.
		this.textures = TextureManager.getInstance();
		this.renderers = new ArrayList<ComponentRenderer>();
		this.renderableNodes = new ArrayList<RenderableNode>();
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
			this.camera.update(Matrix3d.IDENTITY);
		}
		
		if(this.lights != null){
			this.lights.update(this.camera.getWorldTransform());
			for(int i = 0; i < this.lights.getChildCount(); i++){
				((Light)this.lights.getChild(i)).render();
			}
		}
		
		if(this.root != null){
			root.update(this.camera.getWorldTransform());
			Collections.sort(this.renderableNodes, Renderer.renderableSort);
			for(RenderableNode node : this.renderableNodes){
				this.pushMatrix(node.getWorldTransform());
				node.render();
				this.popMatrix();
			}
		}
	}
	
	//debug settings:
	public boolean drawBB = true;
	public boolean drawRenderables = true;
	
	public void renderSceneDebug(){
		if(this.camera != null){
			this.camera.update(Matrix3d.IDENTITY);
		}
		
		if(this.lights != null){
			this.lights.update(this.camera.getWorldTransform());
			for(int i = 0; i < this.lights.getChildCount(); i++){
				Light light = (Light)this.lights.getChild(i);
				this.pushMatrix(light.getWorldTransform());
				light.render();
				this.popMatrix();
			}
		}
		
		//bounding box debug
		Box box = null;
		Material mat = null;
		if(this.drawBB){
			box = new Box();
			box.renderWireFrame();
			mat = new Material();
			mat.setDiffuse(1, 1, 0);
		}
		
		if(this.root != null){
			root.update(this.camera.getWorldTransform());
			Collections.sort(this.renderableNodes, Renderer.renderableSort);
			for(RenderableNode node : this.renderableNodes){
				this.pushMatrix(node.getWorldTransform());
				if(box != null && mat != null){
					box.setBox(node.getBoundingBox());
					mat.render();
					box.render();
				}
				if(this.drawRenderables){
					node.render();
				}
				this.popMatrix();
			}
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
		if(this.root != null){
			this.gatherRenderables(this.root);
		}
	}
	
	private void gatherRenderables(Node node){
		if(node instanceof RenderableNode){
			this.renderableNodes.add((RenderableNode)node);
		}
		for(int i = 0; i < node.getChildCount(); i++){
			this.gatherRenderables(node.getChild(i));
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
	
	public RenderableNode select(float x, float y){
		Ray ray = camera.createRay(x, y);
		Ray objectRay = ray.clone();
		Matrix3d invertWT = new Matrix3d();
		for(RenderableNode node : this.renderableNodes){
			objectRay.transformBy(ray, invertWT.invert(node.getWorldTransform()));
			if(node.getBoundingBox().intersectsRay(objectRay)){
				return node;
			}
		}
		return null;
	}
	
	public Model selectModel(float x, float y){
		Ray ray = camera.createRay(x, y);
		Ray objectRay = ray.clone();
		Matrix3d invertWT = new Matrix3d();
		for(RenderableNode node : this.renderableNodes){
			if(node instanceof Model){
				objectRay.transformBy(ray, invertWT.invert(node.getWorldTransform()));
				if(node.getBoundingBox().intersectsRay(objectRay)){
					return (Model)node;
				}
			}
		}
		return null;
	}
}
