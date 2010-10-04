package com.tespirit.pandadroid.debug;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexList;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.scenegraph.Light;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamboo.vectors.Vector3d;
import com.tespirit.pandadroid.opengl1x.Renderer;

public class RendererDebug extends Renderer{
	public boolean renderDebug;
	public boolean renderNormals;
	public boolean renderBoundingBox;
	public boolean renderRenderables;
	public boolean renderAxis;
	public boolean renderLightPoint;
	public boolean lightsOn;
	
	private Point pointBuffer;
	
	public static class Point{
		Vector3d v;
		Matrix3d m;
		
		public Point(Vector3d v){
			this.v = v;
			this.m = Matrix3d.IDENTITY;
		}
		
		public Point(Vector3d v, Matrix3d m){
			this.v = v;
			this.m = m;
		}
		
	}
	
	public Hashtable<Integer, FloatBuffer> normalBuffers;

	private Primitives.WireCube boundingBox;
	private Color boundingBoxColor;
	private Color selectedColor;
	private Primitives.Axis axis;
	private FloatBuffer point;
	
	public RendererDebug(){
		super(new DebugClock());
		this.renderBoundingBox = false;
		this.renderRenderables = true;
		this.renderNormals = false;
		this.renderAxis = false;
		this.renderLightPoint = false;
		this.lightsOn = true;
		
		this.boundingBox = new Primitives.WireCube();
		this.boundingBoxColor = new Color();
		this.boundingBoxColor.setColor(1, 1, 0);
		this.selectedColor = new Color();
		this.selectedColor.setColor(1, 1, 1);
		
		this.normalBuffers = new Hashtable<Integer, FloatBuffer>();
		
		this.axis = new Primitives.Axis();
				
		ByteBuffer temp = ByteBuffer.allocateDirect(4*3);
		temp.order(ByteOrder.nativeOrder());
		point = temp.asFloatBuffer();
	}
	
	public void renderPoint(Point p){
		this.pointBuffer = p;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		this.mGl = gl;
		this.mGl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		this.mGl.glLoadIdentity();
		this.mGl.glEnable(GL10.GL_POINT_SIZE);
		this.mGl.glPointSize(3);

		this.updateScene();
		
		if(this.renderDebug){
			this.pushMatrix(this.getCamera().getWorldTransform());
			//render node info!
			this.mGl.glDisable(GL10.GL_LIGHTING);
			this.mGl.glDisable(GL10.GL_TEXTURE_2D);
			
			if(this.pointBuffer != null){
				this.pushMatrix(this.pointBuffer.m);
				this.mGl.glColor4f(1, 1, 1, 1);
				this.renderPoint(this.pointBuffer.v);
				this.popMatrix();
			}
			
			for (Iterator<Node> i = this.getSceneIterator(); i.hasNext();){
				this.drawNodeInfo(i.next());
			}
			
			this.popMatrix();
			if(this.lightsEnabled() && this.lightsOn){
				this.mGl.glEnable(GL10.GL_LIGHTING);
			}
		}
		
		this.renderScene();
	}
	
	public void drawNodeInfo(Node node){
		if(node.getWorldTransform() != null){
			this.pushMatrix(node.getWorldTransform());
		}
		if(this.renderBoundingBox && node.getBoundingBox() != null){
			this.boundingBox.setBox(node.getBoundingBox());
			this.boundingBoxColor.render();
			this.boundingBox.render();
		}
		
		if(this.renderAxis){
			this.axis.render();
		}
		
		if(node.getWorldTransform() != null){
			this.popMatrix();
		}
		
		for(int i = 0; i < node.getChildCount(); i++){
			drawNodeInfo(node.getChild(i));
		}
	}
	
	@Override
	protected void createRenderers(){
		super.createRenderers();
		this.addComponentRenderer(new TriangleIndicesDebugRenderer());
		this.addComponentRenderer(new TriangleListDebugRenderer());
		this.addComponentRenderer(new LightDebugRenderer());
	}
	
	protected class LightDebugRenderer extends LightRenderer{	
		@Override
		public void render(Light light) {
			super.render(light);
			if(renderLightPoint){
				renderPoint(light.getWorldPosition());
			}
		}
	}
	
	private void renderPoint(Vector3d point){
		mGl.glDisable(GL10.GL_LIGHTING);
		mGl.glDisable(GL10.GL_TEXTURE_2D);
		this.point.put(point.getX());
		this.point.put(point.getY());
		this.point.put(point.getZ());
		
		this.point.position(0);
		
		this.mGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		this.mGl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.point);
		this.mGl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		this.mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		if(this.lightsEnabled() && this.lightsOn){
			this.mGl.glEnable(GL10.GL_LIGHTING);
		}
	}
	
	private void renderNormals(VertexBuffer vb){
		if(renderNormals && vb.hasType(VertexBuffer.NORMAL)){
			mGl.glDisable(GL10.GL_LIGHTING);
			mGl.glDisable(GL10.GL_TEXTURE_2D);
			
			int count = vb.getCount()*3*4*2;
			
			//generate normal list!
			FloatBuffer normals;
			
			normals = this.normalBuffers.get(count);
			if(normals == null){
				ByteBuffer temp = ByteBuffer.allocateDirect(vb.getCount()*3*4*2);
				temp.order(ByteOrder.nativeOrder());
				normals = temp.asFloatBuffer();
				this.normalBuffers.put(count, normals);
			}
			
			Vector3d position = new Vector3d();
			Vector3d normalOffset = new Vector3d();
			vb.lock();
			while(vb.nextVector3d(position, VertexBuffer.POSITION) && 
				  vb.nextVector3d(normalOffset, VertexBuffer.NORMAL)){
				normals.put(position.getX());
				normals.put(position.getY());
				normals.put(position.getZ());
				
				normalOffset.add(position);
				normals.put(normalOffset.getX());
				normals.put(normalOffset.getY());
				normals.put(normalOffset.getZ());
			}
			vb.unlock();
			normals.position(0);
			
			//draw position!
			mGl.glColor4f(0, 1, 1, 1);
			mGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb.getBufferByType(VertexBuffer.POSITION));
			mGl.glDrawArrays(GL10.GL_POINTS, 0, vb.getCount());
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			
			//draw normals!
			mGl.glColor4f(0, 0.5f, 0.5f, 1);
			mGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glVertexPointer(3, GL10.GL_FLOAT, 0, normals);
			mGl.glDrawArrays(GL10.GL_LINES, 0, vb.getCount()*2);
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			if(lightsEnabled() && this.lightsOn){
				mGl.glEnable(GL10.GL_LIGHTING);
			}
		}
	}
	
	/**
	 * this renders out debug info such as normals.
	 * @author Todd Espiritu Santo
	 *
	 */
	protected class TriangleIndicesDebugRenderer extends VertexIndicesRenderer{
		@Override
		public void render(VertexIndices triangles){
			if(renderRenderables ){
				super.render(triangles);
			}
			renderNormals(triangles.getVertexBuffer());	
		}
	}
	
	/**
	 * this renders out debug info such as normals.
	 * @author Todd Espiritu Santo
	 *
	 */
	protected class TriangleListDebugRenderer extends VertexListRenderer{
		@Override
		public void render(VertexList triangles) {
			if(renderRenderables ){
				super.render(triangles);
			}
			renderNormals(triangles.getVertexBuffer());	
		}
	}
}
