package com.tespirit.panda3d.render;

import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.scenegraph.RenderableNode;
import com.tespirit.panda3d.vectors.AxisAlignedBox;
import com.tespirit.panda3d.vectors.Matrix3d;
import com.tespirit.panda3d.vectors.Vector3d;


public class Light extends Node implements RenderableNode{
	private Vector3d ambient;
	private Vector3d diffuse;
	private Vector3d specular;
	private Vector3d position;
	
	private Vector3d worldPosition;
	private Matrix3d worldTransform;
	
	private int lightId;
	
	public Light(){
		float[] buffer = Vector3d.createBuffer(5);
		this.ambient = new Vector3d(buffer);
		this.diffuse = new Vector3d(buffer, Vector3d.SIZE);
		this.specular = new Vector3d(buffer, Vector3d.SIZE*2);
		this.position = new Vector3d(buffer, Vector3d.SIZE*3);
		
		this.worldPosition = new Vector3d(buffer, Vector3d.SIZE*4);
		
		this.setAmbient(0.5f, 0.5f, 0.5f);
		this.setDiffuse(1.0f, 1.0f, 1.0f);
		this.setSpecular(1.0f, 1.0f, 1.0f);
		this.setPosition(3.0f, 1.0f, 2.0f);
	}
	
	@Override
	public Matrix3d getWorldTransform(){
		return this.worldTransform;
	}
	
	@Override
	public void update(Matrix3d transform){
		this.worldTransform = transform;
		this.worldTransform.transform(this.position, this.worldPosition);
	}
	
	@Override
	public AxisAlignedBox getBoundingBox() {
		return null;
	}

	@Override
	public Node getChild(int i) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public Matrix3d getTransform() {
		return null;
	}
	
	public void setLightId(int id){
		this.lightId = id;
	}
	
	public int getLightId(){
		return this.lightId;
	}
	
	public void setDiffuse(float r, float g, float b, float a){
		this.diffuse.set(r, g, b, a);
	}
	
	public void setDiffuse(float r, float g, float b){
		this.diffuse.set(r, g, b);
	}
	
	public Vector3d getDiffuse(){
		return this.diffuse;
	}
	
	public void setSpecular(float r, float g, float b){
		this.specular.set(r, g, b);
	}
	
	public Vector3d getSpecular(){
		return this.specular;
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient.set(r, g, b);
	}
	
	public Vector3d getAmbient(){
		return this.ambient;
	}
	
	public void setPosition(float x, float y, float z){
		this.position.set(x, y, z);
	}
	
	public Vector3d getPosition(){
		return this.position;
	}
	
	public Vector3d getWorldPosition(){
		return this.worldPosition;
	}
	
	@Override
	public void render(){
		Light.renderer.render(this);
	}
	
	@Override
	public void setup(){
		Light.renderer.setup(this);
	}
	
	@Override
	public void setDisplay(int width, int height) {
		//VOID for now
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Light.renderer = this;
		}
		public abstract void render(Light light);
		public abstract void setup(Light light);
	}
}
