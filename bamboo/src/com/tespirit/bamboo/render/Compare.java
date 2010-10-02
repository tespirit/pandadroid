package com.tespirit.bamboo.render;

import java.util.Comparator;

import com.tespirit.bamboo.animation.Joint;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.scenegraph.RenderableNode;

/**
 * A convenient location for any comparator classes.
 * @author Todd Espiritu Santo
 *
 */
public class Compare {
	public static final Comparator<RenderableNode> renderableSort = new RenderableSort();
	public static final Comparator<Node> nodePrioritySort = new NodePrioritySort();
	
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
	
	private static class NodePrioritySort implements Comparator<Node>{
		@Override
		public int compare(Node object1, Node object2) {
			return this.getPriority(object1)-this.getPriority(object2);
		}
		
		private int getPriority(Node node){
			if(node instanceof Light){
				return 1;
			} else if(node instanceof Joint){
				return 0;
			} else {
				return -1;
			}
		}
	}
}
