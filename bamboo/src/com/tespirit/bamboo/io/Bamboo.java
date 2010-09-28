package com.tespirit.bamboo.io;

import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.scenegraph.Node;


/**
 * A simple io class for loading objects. This way the io can change without
 * having to adjust higher level code.
 * @author Todd Espiritu Santo
 *
 */
public class Bamboo implements BambooAsset{
	
	public static BambooAsset loadAsset(InputStream stream) throws Exception{
		return new Bamboo(stream);
	}

	public static Node loadNode(InputStream stream) throws Exception{
		ObjectInputStream o = new ObjectInputStream(stream);
		Object obj = o.readObject();
		o.close();
		if(obj instanceof Node){
			return (Node)obj;
		} else {
			throw new Exception("This is not a valid scene graph file.");
		}
	}
	
	public static Animation loadAnimation(InputStream stream) throws Exception{
		ObjectInputStream o = new ObjectInputStream(stream);
		Object obj = o.readObject();
		o.close();
		if(obj instanceof Animation){
			return (Animation)obj;
		} else {
			throw new Exception("This is not a valid animation file.");
		}
	}
	
	public static void save(Node root, OutputStream stream) throws Exception{
		ObjectOutputStream o = new ObjectOutputStream(stream);
		o.writeObject(root);
		o.close();
	}
	
	public static void save(Animation animation, OutputStream stream) throws Exception{
		ObjectOutputStream o = new ObjectOutputStream(stream);
		o.writeObject(animation);
		o.close();
	}
	
	/**
	 * right now this only saves out scenegraph and animation data.
	 * @param asset
	 * @param stream
	 * @throws Exception
	 */
	public static void save(BambooAsset asset, OutputStream stream) throws Exception{
		ObjectOutputStream o = new ObjectOutputStream(stream);
		if(asset.getSceneGraph() != null){
			o.writeObject(asset.getSceneGraph());
		}
		if(asset.getAnimation() != null){
			o.writeObject(asset.getAnimation());
		}
		o.close();
	}
	
	private Node mRoot;
	private Animation mAnimation;
	private Camera[] mCameras;
	private LightGroup mLights;
	
	protected Bamboo(InputStream stream) throws Exception{
		ObjectInputStream o = new ObjectInputStream(stream);
		
		try{
			Object obj = o.readObject();
			while(obj != null){
				if(obj instanceof Node){
					this.mRoot = (Node)obj;
				} else if(obj instanceof Animation){
					this.mAnimation = (Animation)obj;
				} else if(obj instanceof LightGroup){
					this.mLights = (LightGroup)obj;
				} else if(obj instanceof Camera[]){
					this.mCameras = (Camera[])obj;
				}
				obj = o.readObject();
			}
		} catch(EOFException e){
			//end the input.
		}
		o.close();
	}

	@Override
	public Animation getAnimation() {
		return this.mAnimation;
	}

	@Override
	public Camera[] getCameras() {
		return this.mCameras;
	}

	@Override
	public LightGroup getLightGroup() {
		return this.mLights;
	}

	@Override
	public Node getSceneGraph() {
		return this.mRoot;
	}
}
