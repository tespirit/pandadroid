package com.tespirit.bamboo.io;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tespirit.bamboo.animation.Animation;
import com.tespirit.bamboo.animation.Player;
import com.tespirit.bamboo.render.Compare;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Node;


/**
 * A simple io class for loading objects. This way the io can change without
 * having to adjust higher level code. 
 * This class should be inherited to provide stream access correctly so that
 * a user doesn't have to worry about how to get a stream.
 * @author Todd Espiritu Santo
 *
 */
public class Bamboo implements BambooAsset{
	
	public static BambooAsset loadBamboo(InputStream stream) throws Exception{
		return new Bamboo(stream);
	}
	
	public static List<Node> loadNodes(InputStream stream) throws Exception{
		ObjectInputStream input = new ObjectInputStream(stream);
		BambooHeader header = new BambooHeader(input, BambooHeader.NODE);
		ArrayList<Node> nodes = new ArrayList<Node>(header.mNodeCount);
		for(int i = 0; i < header.mNodeCount; i++){
			nodes.add((Node)input.readObject());
		}
		return nodes;
	}
	
	public static List<Animation> loadAnimations(InputStream stream) throws Exception{
		ObjectInputStream input = new ObjectInputStream(stream);
		BambooHeader header = new BambooHeader(input, BambooHeader.ANIMATION);
		ArrayList<Animation> nodes = new ArrayList<Animation>(header.mAnimationCount);
		for(int i = 0; i < header.mAnimationCount; i++){
			nodes.add((Animation)input.readObject());
		}
		return nodes;
	}
	
	/**
	 * Nodes will be sorted by priority after this function is called.
	 * @param nodes
	 * @param stream
	 * @throws Exception
	 */
	public static void saveNodes(List<Node> nodes, OutputStream stream) throws Exception{
		Collections.sort(nodes, Compare.nodePrioritySort);
		BambooHeader header = new BambooHeader(BambooHeader.NODE);
		header.mNodeCount = nodes.size();
		ObjectOutputStream output = new ObjectOutputStream(stream);
		header.write(output);
		for(Node node : nodes){
			output.writeObject(node);
		}
	}
	
	public static void saveAnimations(List<Animation> animations, OutputStream stream) throws Exception{
		BambooHeader header = new BambooHeader(BambooHeader.ANIMATION);
		header.mAnimationCount = animations.size();
		ObjectOutputStream output = new ObjectOutputStream(stream);
		header.write(output);
		for(Animation animation : animations){
			output.writeObject(animation);
		}
	}
	
	public static void saveCameras(List<Camera> cameras, OutputStream stream) throws Exception{
		BambooHeader header = new BambooHeader(BambooHeader.CAMERA);
		header.mCameraCount = cameras.size();
		ObjectOutputStream output = new ObjectOutputStream(stream);
		header.write(output);
		for(Camera camera : cameras){
			output.writeObject(camera);
		}
	}
	
	/**
	 * right now this only saves out scenegraph and animation data.
	 * @param asset
	 * @param stream
	 * @throws Exception
	 */
	public static void saveBamboo(BambooAsset asset, OutputStream stream) throws Exception{
		Collections.sort(asset.getScenes(), Compare.nodePrioritySort);
		BambooHeader header = new BambooHeader(BambooHeader.BAMBOO);
		header.mNodeCount = asset.getScenes().size();
		header.mAnimationCount = asset.getAnimations().size();
		header.mCameraCount = asset.getCameras().size();
		header.mPlayerCount = asset.getPlayers().size();
		
		ObjectOutputStream output = new ObjectOutputStream(stream);
		header.write(output);
		for(Node node : asset.getScenes()){
			output.writeObject(node);
		}
		for(Animation animation : asset.getAnimations()){
			output.writeObject(animation);
		}
		for(Camera camera : asset.getCameras()){
			output.writeObject(camera);
		}
		for(Player player : asset.getPlayers()){
			output.writeObject(player);
		}
	}
	
	private static class BambooHeader {
		private static final long serialVersionUID = 2547871559135917340L;
		private static final byte NODE = 0;
		private static final byte ANIMATION = 1;
		private static final byte CAMERA = 2;
		private static final byte BAMBOO = 3;
		
		private static final String[] NAMES = new String[]{
			"Node",
			"Animation",
			"Camera",
			"Bamboo"
		};
		
		private byte mType;
		private int mNodeCount;
		private int mAnimationCount;
		private int mCameraCount;
		private int mPlayerCount;
		
		private BambooHeader(byte type){
			this.mType = type;
		}
		
		private BambooHeader(ObjectInputStream stream, byte type) throws Exception{
			if(BambooHeader.serialVersionUID != stream.readLong()){
				throw new Exception("Incompatible Header Version");
			}
			this.mType = stream.readByte();
			if(this.mType != BAMBOO && type != BAMBOO && this.mType != type){
				throw new Exception("File type incompatible: "+ NAMES[type] + " " + NAMES[this.mType]);
			}
			this.mNodeCount = stream.readInt();
			this.mAnimationCount = stream.readInt();
			this.mCameraCount = stream.readInt();
			this.mPlayerCount = stream.readInt();
		}
		
		private void write(ObjectOutputStream stream) throws Exception{
			stream.writeLong(BambooHeader.serialVersionUID);
			stream.writeByte(this.mType);
			stream.writeInt(this.mNodeCount);
			stream.writeInt(this.mAnimationCount);
			stream.writeInt(this.mCameraCount);
			stream.writeInt(this.mPlayerCount);
		}
	}
	
	private List<Node> mSceneRoots;
	private List<Animation> mAnimations;
	private List<Camera> mCameras;
	private List<Player> mPlayers;
	
	public Bamboo(){
		this.mSceneRoots = new ArrayList<Node>();
		this.mAnimations = new ArrayList<Animation>();
		this.mCameras = new ArrayList<Camera>();
		this.mPlayers = new ArrayList<Player>();
	}
	
	public Bamboo(InputStream stream) throws Exception{
		ObjectInputStream output = new ObjectInputStream(stream);
		BambooHeader header = new BambooHeader(output, BambooHeader.BAMBOO);
		
		this.mSceneRoots = new ArrayList<Node>(header.mNodeCount);
		this.mAnimations = new ArrayList<Animation>(header.mAnimationCount);
		this.mCameras = new ArrayList<Camera>(header.mCameraCount);
		this.mPlayers = new ArrayList<Player>(header.mPlayerCount);
		
		for(int i = 0; i < header.mNodeCount; i++){
			this.mSceneRoots.add((Node)output.readObject());
		}
		for(int i = 0; i < header.mAnimationCount; i++){
			this.mAnimations.add((Animation)output.readObject());
		}
		for(int i = 0; i < header.mCameraCount; i++){
			this.mCameras.add((Camera)output.readObject());
		}
		for(int i = 0; i < header.mPlayerCount; i++){
			this.mPlayers.add((Player)output.readObject());
		}
	}

	@Override
	public List<Animation> getAnimations() {
		return this.mAnimations;
	}

	@Override
	public List<Camera> getCameras() {
		return this.mCameras;
	}

	@Override
	public List<Node> getScenes() {
		return this.mSceneRoots;
	}
	
	@Override
	public List<Player> getPlayers(){
		return this.mPlayers;
	}
}
