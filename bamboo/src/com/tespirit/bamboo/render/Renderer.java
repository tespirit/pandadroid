package com.tespirit.bamboo.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
	
	private Camera camera;
	private List<Node> mScene;
	private List<Light> mLights; 
	private ArrayList<RenderableNode> renderableNodes;
	
	private ArrayList<TimeUpdate> timeUpdates;
	
	private TextureManager textures;
	private ArrayList<ComponentRenderer> renderers;
	
	private Clock mClock;
	
	public Renderer(){
		this.mScene = new ArrayList<Node>();
		this.mLights = new ArrayList<Light>();
		this.timeUpdates = new ArrayList<TimeUpdate>();
		
		this.backgroundColor = new Color4();
		
		this.renderers = new ArrayList<ComponentRenderer>();
		this.renderableNodes = new ArrayList<RenderableNode>();
	}
	
	public void setBackgroundColor(Color4 color){
		this.backgroundColor.copy(color);
	}
	
	public void clearScene(boolean keepLights){
		this.mScene.clear();
		this.renderableNodes.clear();
		if(keepLights){
			this.mScene.addAll(this.mLights);
		} else {
			this.mLights.clear();
		}
	}
	
	public void addNode(Node node){
		Node prev = null;
		if(this.mScene.size() > 0){
			prev = this.mScene.get(this.mScene.size()-1);
		}
		this.mScene.add(node);
		this.gatherRenderables(node);
		//only sort if there is a need. in most cases new scene nodes will be inserted.
		if(prev != null && Compare.nodePrioritySort.compare(node, prev)>0){
			Collections.sort(this.mScene, Compare.nodePrioritySort);
		}
	}
	
	public void addNode(List<Node> nodes){
		for(Node node : nodes){
			this.addNode(node);
		}
	}
	
	public int getRootCount(){
		return this.mScene.size();
	}
	
	public Node getRoot(int i){
		return this.mScene.get(i);
	}
	
	public Iterator<Node> getRootIterator(){
		return this.mScene.iterator();
	}
	
	public void addTimeUpdate(TimeUpdate tu){
		if(tu != null){
			tu.setClock(this.mClock);
			this.timeUpdates.add(tu);
		}
	}
	
	public void clearTimeUpdates(){
		this.timeUpdates.clear();
	}
	
	public int getTimeUpdateCount(){
		return this.timeUpdates.size();
	}
	
	public Iterator<TimeUpdate> getTimeUpdateIterator(){
		return this.timeUpdates.iterator();
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
	
	public void updateScene(){
		this.mClock.update();
		for(TimeUpdate timeUpdate : this.timeUpdates){
			timeUpdate.update();
		}
		this.camera.update(Matrix3d.IDENTITY);
		for(Node node : this.mScene){
			node.update(Matrix3d.IDENTITY);
		}
		Collections.sort(this.renderableNodes, Compare.renderableSort);
	}
	
	public void renderScene(){
		this.camera.render();
		for(Light light : this.mLights){
			light.render();
		}
		for(RenderableNode node : this.renderableNodes){
			this.pushMatrix(node.getWorldTransform());
			node.render();
			this.popMatrix();
		}
	}
	
	public boolean lightsEnabled(){
		return this.mLights.size() > 0;
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
		this.mClock = this.createClock();
		for(TimeUpdate t : this.timeUpdates){
			t.setClock(this.mClock);
		}
		this.reactivateComponentRenderers();
		
		for(Node node : this.mScene){
			this.gatherRenderables(node);
		}
		
		for(Light light : this.mLights){
			this.traverseSetup(light);
		}
		if(this.textures != null){
			this.enableTextures();
			for(int i = 0; i < this.textures.getTextureCount(); i++){
				this.textures.getTexture(i).setup();
			}
		}
	}
	
	private void gatherRenderables(Node node){
		if(node instanceof RenderableNode){
			this.renderableNodes.add((RenderableNode)node);
		} else if (node instanceof Light){
			this.mLights.add((Light)node);
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
	
	/* scenegraph manipulation */
	public abstract void popMatrix();
	public abstract void pushMatrix(Matrix3d transform);
	
	/* render settings */
	public abstract void enableTextures();
	public abstract void enableLights();
	public abstract void disableLights();
	public abstract void disableTextures();
	public abstract Clock createClock();
}
