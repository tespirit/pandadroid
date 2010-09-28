package com.tespirit.bamboo.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.tespirit.bamboo.scenegraph.*;
import com.tespirit.bamboo.surfaces.TextureManager;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public abstract class Renderer {
	/* basic attributes */
	protected Color4 backgroundColor;
	
	
	private Node root;
	private Camera camera;
	private LightGroup lights;
	private TextureManager textures;
	private ArrayList<ComponentRenderer> renderers;
	
	protected long currentTime;
	
	private ArrayList<RenderableNode> renderableNodes;
	
	private ArrayList<TimeUpdate> timeUpdates;
	
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
		this.timeUpdates = new ArrayList<TimeUpdate>();
		this.backgroundColor = new Color4();
	}
	
	public void setBackgroundColor(Color4 color){
		this.backgroundColor.copy(color);
	}
	
	public void setSceneGraph(Node root){
		this.root = root;
	}
	
	public Node getSceneGraph(){
		return this.root;
	}
	
	public void addTimeUpdate(TimeUpdate a){
		if(a != null){
			this.timeUpdates.add(a);
		}
	}
	
	public int getTimeUpdateCount(){
		return this.timeUpdates.size();
	}
	
	public TimeUpdate getTimeUpdate(int i){
		return this.timeUpdates.get(i);
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
	
	public void updateScene(long time){
		//update all animations!
		this.currentTime = time;
		for(TimeUpdate timeUpdate : this.timeUpdates){
			timeUpdate.update(this.currentTime);
		}
		
		this.camera.update(Matrix3d.IDENTITY);
		Matrix3d view = this.camera.getWorldTransform();
		if(this.lights != null){
			this.lights.update(view);
		}
		if(this.root != null){
			this.root.update(view);
			Collections.sort(this.renderableNodes, Renderer.renderableSort);
		}
	}
	
	public void renderScene(){
		if(this.lights != null){
			for(int i = 0; i < this.lights.getChildCount(); i++){
				((Light)this.lights.getChild(i)).render();
			}
		}
		if(this.root != null){
			for(RenderableNode node : this.renderableNodes){
				this.pushMatrix(node.getWorldTransform());
				node.render();
				this.popMatrix();
			}
		}
	}
	
	public boolean lightsEnabled(){
		return this.lights != null;
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
	public abstract void disableLights();
	public abstract void disableTextures();
	
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
		Ray objectRay = new Ray();
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
