package com.tespirit.panda3d.animation;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.scenegraph.Transform;
import com.tespirit.panda3d.vectors.*;

/**
 * In keeping things simple, the joint class will be a SceneGraphNode,
 * though not necisarily in the renderable scene tree. By being a SGN, 
 * future extensions, ie physics, can also be added to this without having
 * to special case it.
 * This class is optimized towards assumptions about a skeleton, such as a joint
 * does not need to support dynamic addition of children.
 * This class can still have any SceneGraph node as a child, making it easy to
 * have no skinning required skeleton animations.
 * @author Dennis McAutoKnight
 *
 */
public class Joint extends Transform{
	
	private Node[] children;
	
	public Joint(){
		this.children = null;
	}
	
	public Joint(String name){
		this.children = null;
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	public Node getChild(int i) {
		return this.children[i];
	}

	@Override
	public int getChildCount() {
		return this.children.length;
	}
	
	public void setChildren(Node[] c){
		this.children = c;
	}

	@Override
	public Matrix3d getTransform() {
		return null;
	}

}
