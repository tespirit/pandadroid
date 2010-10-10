package com.tespirit.bamboo.scenegraph;

import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.render.UpdateManager;
import com.tespirit.bamboo.render.Updater;
import com.tespirit.bamboo.vectors.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

public abstract class Node {
	private String mName;
		
	static private Map<String, Node> nameLookup = new HashMap<String, Node>();
	
	private RenderManager mRenderManager;
	
	public Node(){
		this(null);
	}
	
	public Node(String name){
		this.mName = name;
		if(this.mName != null){
			Node.nameLookup.put(this.mName, this);
		}
	}
	
	public String getName() {
		if(this.mName != null && this.mName.length() > 0)
			return this.mName;
		else 
			return null;
	}
	
	public void setName(String n){
		if(this.getName() != null){
			Node.nameLookup.remove(this.mName);
		}
		if(n != null && n.length() > 0){
			this.mName = n;
			Node.nameLookup.put(this.mName, this);
		} else {
			this.mName = "";
		}
	}
	
	/**
	 * Override for classes that actually handle resources.
	 * @param renderManager
	 */
	public void setRenderManager(RenderManager renderManager){
		this.mRenderManager = renderManager;
		for(int i = 0; i < this.getChildCount(); i++){
			this.getChild(i).setRenderManager(renderManager);
		}
	}
	
	public RenderManager getRenderManager(){
		return this.mRenderManager;
	}
	
	public abstract Node getChild(int i);
	
	public abstract int getChildCount();
	
	public abstract Matrix3d getTransform();
	
	public abstract Matrix3d getWorldTransform();
	
	public abstract AxisAlignedBox getBoundingBox();
	
	/**
	 * this updates the nodes with the new tranformation
	 * @param m
	 */
	public abstract void update(Matrix3d transform);
	
	
	public final void recycle(){
		this.mRenderManager.addSingleUpdater(new Recycler(this));
	}
	
	private class Recycler implements Updater{
		Node mNode;
		
		Recycler(Node node){
			this.mNode = node;
		}

		@Override
		public void update() {
			this.mNode.recycleInternal();
			mRenderManager.removeScene(this.mNode);
			if(Node.nameLookup.containsKey(mName)){
				Node.nameLookup.remove(mName);
			}
			mName = null;
			this.mNode = null;
		}

		@Override
		public void setUpdateManager(UpdateManager updateManager) {
			//VOID
		}
		
	}
	
	protected abstract void recycleInternal();
	
	public static Node getNode(String name){
		return Node.nameLookup.get(name);
	}
	
	//IO
	protected void write(ObjectOutput out) throws IOException{
		out.writeUTF(this.mName);
	}
	
	protected void read(ObjectInput in) throws IOException, ClassNotFoundException{
		this.setName(in.readUTF());
	}
	
}
