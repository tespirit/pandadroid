package com.tespirit.bamboo.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.tespirit.bamboo.scenegraph.*;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public abstract class RenderManager {
	/* basic attributes */
	protected Color4 backgroundColor;
	
	private Camera mCamera;
	private List<Node> mScene;
	private List<Light> mLights; 
	private List<RenderableNode> mRenderableNodes;
	
	private List<TimeUpdate> timeUpdates;
	private List<ComponentRenderer> mRenderers;
	
	private Clock mClock;
	
	public RenderManager(Clock clock){
		this.mClock = clock;
		this.mScene = new ArrayList<Node>();
		this.mLights = new ArrayList<Light>();
		this.timeUpdates = new ArrayList<TimeUpdate>();
		
		this.backgroundColor = new Color4();
		
		this.mRenderers = new ArrayList<ComponentRenderer>();
		this.mRenderableNodes = new ArrayList<RenderableNode>();
	}
	
	public void setBackgroundColor(Color4 color){
		this.backgroundColor.copy(color);
	}
	
	public void clearScene(boolean keepLights){
		this.mScene.clear();
		this.mRenderableNodes.clear();
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
	
	public void removeNode(Node node){
		this.mScene.remove(node);
		this.mRenderableNodes.remove(node);
		this.mLights.remove(node);
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
		this.mCamera = camera;
	}
	
	public Camera getCamera(){
		return this.mCamera;
	}
	
	public void updateScene(){
		this.mClock.update();
		for(TimeUpdate timeUpdate : this.timeUpdates){
			timeUpdate.update();
		}
		this.mCamera.update(Matrix3d.IDENTITY);
		for(Node node : this.mScene){
			node.update(Matrix3d.IDENTITY);
		}
		Collections.sort(this.mRenderableNodes, Compare.renderableSort);
	}
	
	public void renderScene(){
		Node.initNewNodes();
		
		this.mCamera.render();
		for(Light light : this.mLights){
			light.render();
		}
		for(RenderableNode node : this.mRenderableNodes){
			this.pushMatrix(node.getWorldTransform());
			node.render();
			this.popMatrix();
		}
	}
	
	public boolean lightsEnabled(){
		return this.mLights.size() > 0;
	}

	/**
	 * This should be called to initialize any render settings before rendering
	 * takes place.
	 */
	public void setupRender(){
		this.mClock.start();
		for(TimeUpdate t : this.timeUpdates){
			t.setClock(this.mClock);
		}
		this.reactivateComponentRenderers();
		Node.initNewNodes();
	}
	
	private void gatherRenderables(Node node){
		if(node instanceof RenderableNode){
			this.mRenderableNodes.add((RenderableNode)node);
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
		this.mCamera.setDisplay(width, height);
	}
	
	public void addComponentRenderer(ComponentRenderer r){
		this.mRenderers.add(r);
	}
	
	public void reactivateComponentRenderers(){
		for(int i = 0; i < this.mRenderers.size(); i++){
			this.mRenderers.get(i).activate();
		}
	}
	
	public RenderableNode select(float x, float y){
		Ray ray = mCamera.createRay(x, y);
		Ray objectRay = new Ray();
		Matrix3d invertWT = new Matrix3d();
		for(RenderableNode node : this.mRenderableNodes){
			objectRay.transformBy(ray, invertWT.invert(node.getWorldTransform()));
			if(node.getBoundingBox().intersectsRay(objectRay)){
				return node;
			}
		}
		return null;
	}
	
	public Model selectModel(float x, float y){
		Ray ray = mCamera.createRay(x, y);
		Ray objectRay = ray.clone();
		Matrix3d invertWT = new Matrix3d();
		for(RenderableNode node : this.mRenderableNodes){
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
}
