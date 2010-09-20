package com.tespirit.panda3d.opengl1x;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.primitives.Axis;
import com.tespirit.panda3d.primitives.Box;
import com.tespirit.panda3d.primitives.TriangleIndices;
import com.tespirit.panda3d.primitives.TriangleList;
import com.tespirit.panda3d.primitives.VertexBuffer;
import com.tespirit.panda3d.primitives.WireBox;
import com.tespirit.panda3d.render.Light;
import com.tespirit.panda3d.scenegraph.Node;
import com.tespirit.panda3d.surfaces.Color;
import com.tespirit.panda3d.vectors.Vector3d;

public class RendererDebug extends Renderer{
	public boolean renderNormals;
	public boolean renderBB;
	public boolean renderRenderables;
	public boolean renderAxis;
	public boolean renderLightPoint;
	public boolean lightsOn;
	
	
	public Hashtable<Integer, FloatBuffer> normalBuffers;

	private WireBox boundingBox;
	private Color boundingBoxColor;
	private Axis axis;
	private FloatBuffer point;
	
	public RendererDebug(){
		super();
		this.renderBB = false;
		this.renderRenderables = true;
		this.renderNormals = false;
		this.renderAxis = false;
		this.renderLightPoint = false;
		this.lightsOn = true;
		
		this.boundingBox = new WireBox();
		this.boundingBoxColor = new Color();
		this.boundingBoxColor.setColor(1, 1, 0);
		
		this.normalBuffers = new Hashtable<Integer, FloatBuffer>();
		
		this.axis = new Axis();
		
		ByteBuffer temp = ByteBuffer.allocateDirect(4*3);
		temp.order(ByteOrder.nativeOrder());
		point = temp.asFloatBuffer();
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		gl.glEnable(GL10.GL_POINT_SIZE);
		gl.glPointSize(3);
		
		this.gl = gl;

		this.updateScene();
		
		//render node info!
		this.gl.glDisable(GL10.GL_LIGHTING);
		this.gl.glDisable(GL10.GL_TEXTURE_2D);
		this.drawNodeInfo(this.getSceneGraph());
		if(this.lightsEnabled() && this.lightsOn){
			this.gl.glEnable(GL10.GL_LIGHTING);
		}
		
		this.renderScene();
	}
	
	public void drawNodeInfo(Node node){
		if(node.getWorldTransform() != null){
			this.pushMatrix(node.getWorldTransform());
		}
		if(this.renderBB && node.getBoundingBox() != null){
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
	public void createRenderers(){
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
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		this.point.put(point.getX());
		this.point.put(point.getY());
		this.point.put(point.getZ());
		
		this.point.position(0);
		
		this.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		this.gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.point);
		this.gl.glDrawArrays(GL10.GL_POINTS, 0, 1);
		this.gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		if(this.lightsEnabled() && this.lightsOn){
			this.gl.glEnable(GL10.GL_LIGHTING);
		}
	}
	
	private void renderNormals(VertexBuffer vb){
		if(renderNormals && vb.hasType(VertexBuffer.NORMAL)){
			gl.glDisable(GL10.GL_LIGHTING);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			
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
			vb.resetBufferPosition();
			normals.position(0);
			
			//draw position!
			gl.glColor4f(0, 1, 1, 1);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vb.getBuffer(VertexBuffer.POSITION));
			gl.glDrawArrays(GL10.GL_POINTS, 0, vb.getCount());
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			
			//draw normals!
			gl.glColor4f(0, 0.5f, 0.5f, 1);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, normals);
			gl.glDrawArrays(GL10.GL_LINES, 0, vb.getCount()*2);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			if(lightsEnabled() && this.lightsOn){
				gl.glEnable(GL10.GL_LIGHTING);
			}
		}
	}
	
	/**
	 * this renders out debug info such as normals.
	 * @author Todd Espiritu Santo
	 *
	 */
	protected class TriangleIndicesDebugRenderer extends TriangleIndicesRenderer{
		@Override
		public void render(TriangleIndices triangles){
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
	protected class TriangleListDebugRenderer extends TriangleListRenderer{
		@Override
		public void render(TriangleList triangles) {
			if(renderRenderables ){
				super.render(triangles);
			}
			renderNormals(triangles.getVertexBuffer());	
		}
	}
}
