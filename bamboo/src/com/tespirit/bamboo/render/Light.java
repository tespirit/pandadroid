package com.tespirit.bamboo.render;

import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.scenegraph.RenderableNode;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;


public class Light extends Node implements RenderableNode{
	private Color4 ambient;
	private Color4 diffuse;
	private Color4 specular;
	private Vector3d position;
	
	private Vector3d worldPosition;
	private Matrix3d worldTransform;
	
	private int lightId;
	
	public Light(){
		float[] buffer = Color4.createBuffer(5);
		this.ambient = new Color4(buffer);
		this.diffuse = new Color4(buffer, Color4.SIZE);
		this.specular = new Color4(buffer, Color4.SIZE*2);
		this.position = new Vector3d(buffer, Color4.SIZE*3);
		this.worldPosition = new Vector3d(buffer, Vector3d.SIZE*4);
		
		this.setAmbient(0.5f, 0.5f, 0.5f);
		this.setDiffuse(1.0f, 1.0f, 1.0f);
		this.setSpecular(1.0f, 1.0f, 1.0f);
		this.setPosition(0.0f, 0.0f, 0.0f);
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
	
	public Color4 getDiffuse(){
		return this.diffuse;
	}
	
	public void setSpecular(float r, float g, float b){
		this.specular.set(r, g, b);
	}
	
	public Color4 getSpecular(){
		return this.specular;
	}
	
	public void setAmbient(float r, float g, float b){
		this.ambient.set(r, g, b);
	}
	
	public Color4 getAmbient(){
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
	public void init(){
		Light.renderer.init(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			Light.renderer = this;
		}
		public abstract void render(Light light);
		public abstract void init(Light light);
	}
}
