package com.tespirit.bamboo.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.scenegraph.RenderableNode;

public class ResourceManager {
	private static ResourceManager mResourceManager;
	
	public static void initInstance(){
		mResourceManager = new ResourceManager();
	}
	
	public static ResourceManager getInstance(){
		return mResourceManager;
	}
	
	List<Updater> mCurrentUpdaters;
	List<Updater> mNextUpdaters;
	
	List<Node> mScene;
	List<Node> mNewNodes;
	
	List<RenderableNode> mRenderableNodes;
	
	List<DynamicLoader> mNewResources;
	
	public ResourceManager(){
		this.mCurrentUpdaters = new ArrayList<Updater>();
		this.mNextUpdaters = new ArrayList<Updater>();
		this.mScene = new ArrayList<Node>();
		this.mNewNodes = new ArrayList<Node>();
		this.mRenderableNodes = new ArrayList<RenderableNode>();
		this.mNewResources = new ArrayList<DynamicLoader>();
	}
	
	public void addNode(Node node){
		this.mNewNodes.add(node);
	}
	
	public void removeNode(Node node){
		this.mRenderableNodes.remove(node);
		this.mScene.remove(node);
	}
	
	public void addUpdater(Updater updater){
		this.mNextUpdaters.add(updater);
	}
	
	public void removerUpdater(Updater updater){
		this.mCurrentUpdaters.remove(updater);
		this.mNextUpdaters.remove(updater);
	}
	
	public void registerDynamicLoader(DynamicLoader d){
		this.mNewResources.add(d);
	}
	
	public void runUpdaters(){
		for(Updater updater : this.mCurrentUpdaters){
			updater.update();
		}
		List<Updater> temp = this.mCurrentUpdaters;
		temp.clear();
		this.mCurrentUpdaters = this.mNextUpdaters;
		this.mNextUpdaters = temp;
	}
	
	public void init(){
		//TODO: make this null out the value. But also make it thread safe?
		for(DynamicLoader n : this.mNewResources){
			n.init();
		}
		this.mNewResources.clear();
		
		for(Node n : this.mNewNodes){
			this.initRenderableNodes(n);
			this.addNodeToScene(n);
		}
		this.mNewNodes.clear();
	}
	
	void addNodeToScene(Node node){
		if(this.mScene.isEmpty()){
			this.mScene.add(node);
		} else {
			Node prev = this.mScene.get(this.mScene.size()-1);
			this.mScene.add(node);
			this.initRenderableNodes(node);
			if(Compare.nodePrioritySort.compare(node, prev)>0){
				Collections.sort(this.mScene, Compare.nodePrioritySort);
			}
		}
	}
	
	private void initRenderableNodes(Node node){
		//Joints cannot have renderables attached to them as children.
		if(node instanceof Joint){
			return;
		}
		if(node instanceof RenderableNode){
			mRenderableNodes.add((RenderableNode)node);
		}
		for(int i = 0; i < node.getChildCount(); i++){
			this.initRenderableNodes(node.getChild(i));
		}
	}
	
}
