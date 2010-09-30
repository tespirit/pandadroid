package com.tespirit.bamporter.opengl;

import java.awt.Component;
import java.util.Calendar;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;
import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.LineIndices;
import com.tespirit.bamboo.primitives.LineList;
import com.tespirit.bamboo.primitives.Points;
import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.primitives.TriangleIndices;
import com.tespirit.bamboo.primitives.TriangleList;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.render.Light;
import com.tespirit.bamboo.render.LightGroup;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Material;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;

public class Renderer extends com.tespirit.bamboo.render.Renderer implements GLEventListener{
	GLCanvas mCanvas;
	FPSAnimator mAnimator;
	private GL2 mGl;
	
	private static GLCapabilities mGlCapabilities;
	
	public static void initGl(){
		GLProfile.initSingleton();
		GLProfile glp = GLProfile.getDefault();
		mGlCapabilities = new GLCapabilities(glp);
	}
	
	public Renderer(){
		super();
		this.mCurrentLightId = 0;
		
		IndexBuffer.setTypeEnum(GL2.GL_UNSIGNED_INT, 
								GL2.GL_UNSIGNED_SHORT, 
								GL2.GL_UNSIGNED_BYTE);
		
		Primitive.setTypeEnums(GL2.GL_TRIANGLES, 
							   GL2.GL_TRIANGLE_STRIP, 
							   GL2.GL_TRIANGLE_FAN, 
							   GL2.GL_LINES,
							   GL2.GL_LINE_STRIP,
							   GL2.GL_POINTS);
		this.createRenderers();
		
		//create a default camera!
		Camera camera = new Camera();
		camera.zoom(-3);
		camera.getPivotTransform().rotateX(45);
		this.setCamera(camera);
		
		LightGroup lights = new LightGroup();
		lights.createBasic();
		this.setLightGroup(lights);
		
		this.mCanvas = new GLCanvas(mGlCapabilities);
		this.mCanvas.addGLEventListener(this);
		
		this.mAnimator = new FPSAnimator(this.mCanvas, 30);
		this.mAnimator.add(this.mCanvas);
		this.mAnimator.start();
	}
	
	public Component getView(){
		return this.mCanvas;
	}

	public void createRenderers(){
		//create renderers!
		this.addComponentRenderer(new TriangleIndicesRenderer());
		this.addComponentRenderer(new TriangleListRenderer());
		this.addComponentRenderer(new MaterialRenderer());
		this.addComponentRenderer(new LightRenderer());
		this.addComponentRenderer(new CameraRenderer());
		this.addComponentRenderer(new TextureRenderer());
		this.addComponentRenderer(new ColorRenderer());
		this.addComponentRenderer(new LineIndicesRenderer());
		this.addComponentRenderer(new LineListRenderer());
		this.addComponentRenderer(new PointsRenderer());
	}

	@Override
	public void enableLights() {
		this.mGl.glEnable(GL2.GL_LIGHTING);
		this.mGl.glEnable(GL2.GL_COLOR_MATERIAL);
		this.mGl.glEnable(GL2.GL_NORMALIZE);
	}
	
	@Override
	public void disableLights(){
		this.mGl.glDisable(GL2.GL_LIGHTING);
		this.mGl.glDisable(GL2.GL_COLOR_MATERIAL);
		this.mGl.glDisable(GL2.GL_NORMALIZE);
	}

	@Override
	public void enableTextures() {
		//VOID
	}
	
	@Override
	public void disableTextures(){
		//VOID
	}

	@Override
	public void popMatrix() {
		this.mGl.glPopMatrix();
	}

	@Override
	public void pushMatrix(Matrix3d transform) {
		this.mGl.glPushMatrix();
		this.mGl.glLoadMatrixf(transform.getBuffer(), transform.getBufferOffset());
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		this.mGl = drawable.getGL().getGL2();
		
		this.mGl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		this.mGl.glLoadIdentity();
		
		this.updateScene(Calendar.getInstance().getTimeInMillis());
		this.renderScene();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		this.mGl = drawable.getGL().getGL2();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		this.mGl = drawable.getGL().getGL2();
		this.mGl.glClearDepthf(1.0f);
		this.mGl.glShadeModel(GL2.GL_SMOOTH);
		this.mGl.glEnable(GL2.GL_DEPTH_TEST);
		this.mGl.glDepthFunc(GL2.GL_LEQUAL);
		this.mGl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		this.mGl.glCullFace(GL2.GL_BACK);
		this.setupRender();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		this.mGl = drawable.getGL().getGL2();
		this.setDisplay(width, height);
	}
	
	
	//component renderers
	int mCurrentLightId = 0;
	protected class LightRenderer extends Light.Renderer{
		@Override
		public void render(Light light) {
			int id = light.getLightId();
	        mGl.glLightfv(id, GL2.GL_AMBIENT, light.getAmbient().getBuffer(), light.getAmbient().getBufferOffset());
	        mGl.glLightfv(id, GL2.GL_DIFFUSE, light.getDiffuse().getBuffer(), light.getDiffuse().getBufferOffset());
	        mGl.glLightfv(id, GL2.GL_SPECULAR, light.getSpecular().getBuffer(), light.getSpecular().getBufferOffset());
			mGl.glLightfv(id, GL2.GL_POSITION, light.getWorldPosition().getBuffer(), light.getWorldPosition().getBufferOffset());
		}
		
		@Override
		public void setup(Light light) {
			if(mCurrentLightId < GL2.GL_MAX_LIGHTS) {
				light.setLightId(GL2.GL_LIGHT0+mCurrentLightId);
				mGl.glEnable(light.getLightId());
				mCurrentLightId++;
			}
		}
	}
	
	protected class CameraRenderer extends Camera.Renderer{
		@Override
		public void render(Camera camera) {
			mGl.glMultMatrixf(camera.getTransform().getBuffer(),camera.getTransform().getBufferOffset());
			mGl.glMultMatrixf(camera.getPivotTransform().getBuffer(),camera.getPivotTransform().getBufferOffset());
		}
		
		@Override
		public void setDisplay(Camera camera, int width, int height) {
			if(height == 0){
				height = 1;
			}
			
			mGl.glViewport(0, 0, width, height);
			mGl.glMatrixMode(GL2.GL_PROJECTION);
			mGl.glLoadIdentity();
			mGl.glFrustumf(-camera.getNearWidth(), 
						  camera.getNearWidth(),
						  -camera.getNearHeight(),
						  camera.getNearHeight(), 
						  camera.getNear(), camera.getFar());
			mGl.glMatrixMode(GL2.GL_MODELVIEW);
			mGl.glLoadIdentity();
		}
	}
	
	protected class ColorRenderer extends Color.Renderer{
		@Override
		public void render(Color color) {
			mGl.glDisable(GL2.GL_TEXTURE_2D);
			mGl.glEnable(GL2.GL_COLOR_MATERIAL);
			Color4 c = color.getColor();
			mGl.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		}

		@Override
		public void setup(Color color) {
			//VOID
		}
	}

	protected class MaterialRenderer extends Material.Renderer{
		@Override
		public void render(Material material) {
			mGl.glDisable(GL2.GL_COLOR_MATERIAL);
			mGl.glDisable(GL2.GL_TEXTURE_2D);
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, material.getAmbientBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, material.getDiffuseBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, material.getSpecularBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, material.getEmissionBuffer());
		}

		@Override
		public void setup(Material material) {
			//VOID
		}
	}

	protected class TextureRenderer extends Texture.Renderer{
		@Override
		public void render(Texture texture) {
			mGl.glEnable(GL2.GL_TEXTURE_2D);
			mGl.glEnable(GL2.GL_COLOR_MATERIAL);
			mGl.glColor4f(1,1,1,1);
			mGl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getDiffuseTextureId());
		}
		
		@Override
		public void setup(Texture texture) {

		}
	}
	
	protected void renderVertexBuffer(VertexBuffer vertexBuffer){
		
		this.mGl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		this.mGl.glVertexPointer(vertexBuffer.getStride(VertexBuffer.POSITION), 
						   GL2.GL_FLOAT, 
						   0, 
						   vertexBuffer.getBuffer(VertexBuffer.POSITION));
		
		if(vertexBuffer.hasType(VertexBuffer.NORMAL)){
			this.mGl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			this.mGl.glNormalPointer(GL2.GL_FLOAT, 
							   0, 
							   vertexBuffer.getBuffer(VertexBuffer.NORMAL));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.TEXCOORD)){
			this.mGl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			this.mGl.glTexCoordPointer(vertexBuffer.getStride(VertexBuffer.TEXCOORD), 
								 GL2.GL_FLOAT, 
								 0, 
								 vertexBuffer.getBuffer(VertexBuffer.TEXCOORD));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.COLOR)){
			this.mGl.glEnableClientState(GL2.GL_COLOR_ARRAY);
			this.mGl.glColorPointer(vertexBuffer.getStride(VertexBuffer.COLOR), 
						 	  	   GL2.GL_FLOAT, 
						 	 	   0, 
						 	 	   vertexBuffer.getBuffer(VertexBuffer.COLOR));
		}
	}
	
	protected class TriangleIndicesRenderer extends TriangleIndices.Renderer {

		@Override
		public void render(TriangleIndices triangles) {
			mGl.glFrontFace(GL2.GL_CCW);
			mGl.glEnable(GL2.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			
			IndexBuffer indexBuffer = triangles.getIndexBuffer();
			
			mGl.glDrawElements(triangles.getTypeEnum(), 
							  indexBuffer.getCount(), 
							  indexBuffer.getTypeEnum(), 
							  indexBuffer.getBuffer());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}

	protected class TriangleListRenderer extends TriangleList.Renderer{
		@Override
		public void render(TriangleList triangles) {
			mGl.glFrontFace(GL2.GL_CCW);
			mGl.glEnable(GL2.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			mGl.glDrawArrays(triangles.getTypeEnum(), 0, triangles.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}
	
	protected class LineIndicesRenderer extends LineIndices.Renderer{
		@Override
		public void render(LineIndices lines) {
			
			renderVertexBuffer(lines.getVertexBuffer());
			
			IndexBuffer indexBuffer = lines.getIndexBuffer();
			mGl.glDrawElements(lines.getTypeEnum(), 
					  indexBuffer.getCount(), 
					  indexBuffer.getTypeEnum(), 
					  indexBuffer.getBuffer());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}
	
	protected class LineListRenderer extends LineList.Renderer{
		@Override
		public void render(LineList lines) {
			renderVertexBuffer(lines.getVertexBuffer());
			mGl.glDrawArrays(lines.getTypeEnum(), 0, lines.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}
	
	protected class PointsRenderer extends Points.Renderer{
		@Override
		public void render(Points points) {
			renderVertexBuffer(points.getVertexBuffer());
			mGl.glDrawArrays(points.getTypeEnum(), 0, points.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}
}
