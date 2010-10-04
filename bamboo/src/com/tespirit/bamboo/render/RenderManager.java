package com.tespirit.bamboo.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tespirit.bamboo.scenegraph.*;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Ray;

/**
 * 
 * @author Todd Espiritu Santo
 *
 */
public abstract class RenderManager implements UpdateManager {
	
	private class ThreadBuffer<Item> implements Iterable<Item>{
		protected Collection<Item> mRead;
		protected Collection<Item> mWrite;
		
		protected ThreadBuffer(Collection<Item> read, Collection<Item> write){
			this.mRead = read;
			this.mWrite = write;
		}
		
		public void add(Item object){
			this.mWrite.add(object);
		}
		
		public void remove(Item object){
			this.mRead.remove(object);
			this.mWrite.remove(object);
		}
		
		public void removeAll(Collection<Item> objects){
			this.mRead.removeAll(objects);
			this.mWrite.removeAll(objects);
		}
		
		public void clear(){
			this.mRead.clear();
			this.mWrite.clear();
		}
		
		@Override
		public Iterator<Item> iterator() {
			Collection<Item> temp = this.mRead;
			temp.clear();
			this.mRead = mWrite;
			this.mWrite = temp;
			return this.mRead.iterator();
		}
		
	}
	
	private class ThreadBufferList<Item> extends ThreadBuffer<Item>{
		ThreadBufferList(){
			super(new ArrayList<Item>(), new ArrayList<Item>());
		}
	}
	
	private class ThreadBufferSet<Item> extends ThreadBuffer<Item>{

		protected ThreadBufferSet() {
			super(new HashSet<Item>(), new HashSet<Item>());
		}
		
	}
	
	List<Updater> mUpdaters;
	
	List<Node> mScene;
	List<RenderableNode> mRenderables;
	
	ThreadBufferSet<Updater> mSingleUpdaters;
	ThreadBufferList<Node> mNewNodes;
	Set<Resource> mStoredResources;
	ThreadBufferList<Resource> mResources;
	
	private Camera mCamera;
	
	protected Color4 mBackgroundColor;
	
	private List<ComponentRenderer> mRenderers;
	
	private Clock mClock;

	private boolean mLightsEnabled;
	
	private Compare.RenderableSort mRenderSort;
	
	public RenderManager(Clock clock){
		this.mClock = clock;
		this.mBackgroundColor = new Color4();
		this.mRenderers = new ArrayList<ComponentRenderer>();
		this.mLightsEnabled = true;
		this.mRenderSort = new Compare.RenderableSort();
		
		this.mSingleUpdaters = new ThreadBufferSet<Updater>();
		this.mNewNodes = new ThreadBufferList<Node>();
		this.mResources = new ThreadBufferList<Resource>();
		this.mStoredResources = new HashSet<Resource>();
		this.mScene = new ArrayList<Node>();
		this.mRenderables = new ArrayList<RenderableNode>();
		this.mUpdaters = new ArrayList<Updater>();
	}
	
	public void clear(){
		this.mSingleUpdaters.clear();
		this.mNewNodes.clear();
		this.mRenderables.clear();
		this.mResources.clear();
		this.mScene.clear();
		this.mUpdaters.clear();
	}
	
	public void setBackgroundColor(Color4 color){
		this.mBackgroundColor.copy(color);
	}
	
	public void addScene(Node scene){
		scene.setRenderManager(this);
		this.mNewNodes.add(scene);
	}
	
	public void addScenes(List<Node> scenes){
		for(Node node : scenes){
			this.addScene(node);
		}
	}
	
	public void removeScene(Node scene){
		scene.setRenderManager(null);
		this.mScene.remove(scene);
		this.mRenderables.remove(scene);
		this.mNewNodes.remove(scene);
	}
	
	public void removeNode(Collection<Node> scenes){
		this.mScene.removeAll(scenes);
		this.mRenderables.removeAll(scenes);
		this.mNewNodes.removeAll(scenes);
	}
	
	public Iterator<Node> getSceneIterator(){
		return this.mScene.iterator();
	}
	
	public void addUpdater(Updater updater){
		if(updater instanceof TimeUpdater){
			((TimeUpdater)updater).setClock(this.mClock);
		}
		this.mUpdaters.add(updater);
	}
	
	public void removeUpdater(Updater updater){
		this.mUpdaters.remove(updater);
	}
	
	public void removeUpdaters(Collection<Updater> updaters){
		this.mUpdaters.removeAll(updaters);
	}
	
	
	public Iterator<Updater> getUpdateIterator(){
		return this.mUpdaters.iterator();
	}
	
	/**
	 * This is for Updaters that run once.
	 * Please register updaters before adding them to here.
	 */
	public void addSingleUpdater(Updater updater){
		this.mSingleUpdaters.add(updater);
	}
	
	public void removeSingleUpdater(Updater updater){
		this.mSingleUpdaters.remove(updater);
	}
	
	public void removeSingleUpdaters(Collection<Updater> updaters){
		this.mSingleUpdaters.removeAll(updaters);
	}
	
	/**
	 * This will set an Updater's UpdateManager for Updaters that
	 * are to be used as single updaters.
	 * @param updater
	 */
	public void registerUpdater(Updater updater){
		updater.setUpdateManager(this);
	}
	
	public void loadResource(Resource resource){
		if(!this.mStoredResources.contains(resource)){
			this.mResources.add(resource);
			this.mStoredResources.add(resource);
		}
	}
	
	public void unloadResource(Resource resource){
		if(this.mStoredResources.contains(resource)){
			this.mResources.remove(resource);
			this.mStoredResources.remove(resource);
		}
	}
	
	void addRenderable(RenderableNode node){
		this.mRenderables.add(node);
	}
	
	public void setCamera(Camera camera){
		this.mCamera = camera;
		this.mRenderSort.setView(camera);
	}
	
	public Camera getCamera(){
		return this.mCamera;
	}
	
	protected void updateScene(){
		this.mClock.update();
		for(Resource r : this.mResources){
			r.init();
		}
		for(Node n : this.mNewNodes){
			this.mScene.add(n);
		}
		for(Updater u : this.mSingleUpdaters){
			u.update();
		}
		for(Updater u : this.mUpdaters){
			u.update();
		}
		this.mRenderables.clear();
		this.mCamera.update(Matrix3d.IDENTITY);
		for(Node n : this.mScene){
			n.update(Matrix3d.IDENTITY);
		}
	}
	
	protected void renderScene(){
		this.mCamera.render();
		Collections.sort(this.mRenderables, this.mRenderSort);
		for(RenderableNode node : this.mRenderables){
			this.pushMatrix(node.getWorldTransform());
			node.render();
			this.popMatrix();
		}
	}
	
	protected boolean lightsEnabled(){
		return this.mLightsEnabled;
	}

	/**
	 * This should be called to initialize any render settings before rendering
	 * takes place.
	 */
	protected void initRender(){
		this.mClock.start();
		this.reactivateComponentRenderers();
		if(this.mLightsEnabled){
			this.enableLights();
		}
	}

	/**
	 * This should be called any time when setting up the view.
	 * @param width
	 * @param height
	 */
	protected void setDisplay(int width, int height){
		this.mCamera.setDisplay(width, height);
	}
	
	protected void addComponentRenderer(ComponentRenderer r){
		this.mRenderers.add(r);
	}
	
	private void reactivateComponentRenderers(){
		for(int i = 0; i < this.mRenderers.size(); i++){
			this.mRenderers.get(i).activate();
		}
	}
	
	public RenderableNode select(float x, float y){
		Ray ray = mCamera.createRay(x, y);
		Ray objectRay = new Ray();
		Matrix3d invertWT = new Matrix3d();
		for(RenderableNode node : this.mRenderables){
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
		for(RenderableNode node : this.mRenderables){
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
	protected abstract void popMatrix();
	protected abstract void pushMatrix(Matrix3d transform);
	
	/* render settings */
	protected abstract void enableTextures();
	protected abstract void enableLights();
	protected abstract void disableLights();
	protected abstract void disableTextures();
}
