package com.tespirit.bamboo.render;

import java.util.Comparator;

import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.vectors.Vector3d;

/**
 * A convenient location for any comparator classes.
 * @author Todd Espiritu Santo
 *
 */
public class Compare {
	public static final NodePrioritySort nodePrioritySort = new NodePrioritySort();
	public static final NodeNameSort nodeNameSort = new NodeNameSort();
	
	public static class RenderableSort implements Comparator<RenderableNode>{
		private Vector3d mViewPos;
		private Vector3d mDistance;
		
		public RenderableSort(){
			this.mDistance = new Vector3d();
		}
		
		public void setView(Camera view){
			this.mViewPos = view.getWorldTransform().getTranslation();
		}

		@Override
		public int compare(RenderableNode object1, RenderableNode object2) {
			//sort by distance
			float z1 = this.mDistance.sub(object1.getWorldTransform().getTranslation(), 
										  this.mViewPos).magnitude2();
			float z2 = this.mDistance.sub(object1.getWorldTransform().getTranslation(), 
										  this.mViewPos).magnitude2();
			if(z1 < z2) return 1;
			else if(z1 > z2) return -1;
			else return 0;
		}
		
	}
	
	public static class NodePrioritySort implements Comparator<Node>{
		private NodePrioritySort(){
			
		}
		
		@Override
		public int compare(Node object1, Node object2) {
			return this.getPriority(object1)-this.getPriority(object2);
		}
		
		private int getPriority(Node node){
			if(node instanceof Joint){
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public static class NodeNameSort implements Comparator<Node>{
		private NodeNameSort(){
			
		}
		
		@Override
		public int compare(Node object1, Node object2){
			return object1.getName().compareTo(object2.getName());
		}
	}
}
