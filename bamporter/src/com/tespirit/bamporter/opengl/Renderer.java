package com.tespirit.bamporter.opengl;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.tespirit.bamboo.creation.Lights;
import com.tespirit.bamboo.creation.Primitives;
import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexList;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.render.Camera;
import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.Light;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Material;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamporter.app.Assets;

public class Renderer extends com.tespirit.bamboo.render.Renderer implements GLEventListener{
	private GLCanvas mCanvas;
	private FPSAnimator mAnimator;
	private GL2 mGl;
	private CameraControl mCameraControl;
	
	private boolean mRenderBoundingBox;
	private boolean mRenderAxis;
	Primitives.WireCube mBoundingBox = new Primitives.WireCube();
	Primitives.Axis mAxis = new Primitives.Axis();
	Color mBoundingBoxColor = new Color();
	
	private int[] mIndexTypes;
	private int[] mPrimitiveTypes;
	
	private ArrayList<com.jogamp.opengl.util.texture.Texture> mTextures;
	
	private static GLCapabilities mGlCapabilities;
	private static GLProfile mGlProfile;
	
	public static void initGl(){
		GLProfile.initSingleton();
		mGlProfile = GLProfile.getDefault();
		mGlCapabilities = new GLCapabilities(mGlProfile);
		Assets.getInstance().setGlProfile(mGlProfile);
	}
	
	public Renderer(){
		super();
		this.mCurrentLightId = 0;
		
		this.mIndexTypes = new int[IndexBuffer.TYPE_COUNT];
		this.mIndexTypes[IndexBuffer.BUFFER32] = GL2.GL_UNSIGNED_INT;
		this.mIndexTypes[IndexBuffer.BUFFER16] = GL2.GL_UNSIGNED_SHORT;
		this.mIndexTypes[IndexBuffer.BUFFER8]  = GL2.GL_UNSIGNED_BYTE;
		
		this.mPrimitiveTypes = new int[Primitive.TYPE_COUNT];
		this.mPrimitiveTypes[Primitive.TRIANGLES] = GL2.GL_TRIANGLES;
		this.mPrimitiveTypes[Primitive.TRIANGLE_STRIP] = GL2.GL_TRIANGLE_STRIP;
		this.mPrimitiveTypes[Primitive.TRIANGLE_FAN] = GL2.GL_TRIANGLE_FAN;
		this.mPrimitiveTypes[Primitive.LINES] = GL2.GL_LINES;
		this.mPrimitiveTypes[Primitive.LINE_STRIP] = GL2.GL_LINE_STRIP;
		this.mPrimitiveTypes[Primitive.POINTS] = GL2.GL_POINTS;
		
		this.createRenderers();
		
		this.mTextures = new ArrayList<com.jogamp.opengl.util.texture.Texture>();
		this.mTextures.add(null); // this makes it easier to deal with textures that have not been initialized.
		
		//create a default camera!
		Camera camera = new Camera();
		camera.zoom(-2);
		
		camera.getPivotTransform().rotateX(45);
		this.setCamera(camera);
		
		Lights.addDefaultLight(this);
		
		this.mCanvas = new GLCanvas(mGlCapabilities);
		this.mCanvas.addGLEventListener(this);
		this.mCanvas.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent event) {
				// VOID
			}

			@Override
			public void keyReleased(KeyEvent event) {
				// VOID
			}

			@Override
			public void keyTyped(KeyEvent event) {
				switch(event.getKeyChar()){
				case '1':
					mRenderBoundingBox = !mRenderBoundingBox;
					break;
				case '2':
					mRenderAxis = !mRenderAxis;
					break;
				}
			}
			
		});
		
		//add a camera listener!
		this.mCameraControl = new CameraControl(camera);
		this.mCanvas.addMouseMotionListener(this.mCameraControl);
		this.mCanvas.addMouseListener(this.mCameraControl);
		
		this.mAnimator = new FPSAnimator(this.mCanvas, 30);
		this.mAnimator.add(this.mCanvas);
		this.mAnimator.start();
		
		//debug stuff
		this.mBoundingBoxColor.setColor(1, 1, 0);
	}
	
	public Component getView(){
		return this.mCanvas;
	}

	public void createRenderers(){
		//create renderers!
		this.addComponentRenderer(new VertexIndicesRenderer());
		this.addComponentRenderer(new VertexListRenderer());
		this.addComponentRenderer(new MaterialRenderer());
		this.addComponentRenderer(new LightRenderer());
		this.addComponentRenderer(new CameraRenderer());
		this.addComponentRenderer(new TextureRenderer());
		this.addComponentRenderer(new ColorRenderer());
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
		this.mGl.glMultMatrixf(transform.getBuffer(), transform.getBufferOffset());
	}
	
	@Override
	public Clock createClock() {
		return new CalendarClock();
	}

	public void renderDebug(){
		this.mGl.glDisable(GL2.GL_LIGHTING);
		this.mGl.glDisable(GL2.GL_TEXTURE_2D);
		this.pushMatrix(this.getCamera().getWorldTransform());
		for(int i = 0; i < this.getRootCount(); i++){
			this.drawNodeInfo(this.getRoot(i));
		}
		this.popMatrix();
		if(this.lightsEnabled()){
			this.mGl.glEnable(GL2.GL_LIGHTING);
		}
	}
	
	
	public void drawNodeInfo(Node node){
		if(node.getWorldTransform() != null){
			this.pushMatrix(node.getWorldTransform());
		}
		
		if(node.getBoundingBox() != null && this.mRenderBoundingBox){
			this.mBoundingBox.setBox(node.getBoundingBox());
			this.mBoundingBoxColor.render();
			this.mBoundingBox.render();
		}
		
		if(this.mRenderAxis){
			this.mAxis.render();
		}
		
		if(node.getWorldTransform() != null){
			this.popMatrix();
		}
		
		for(int i = 0; i < node.getChildCount(); i++){
			drawNodeInfo(node.getChild(i));
		}
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		this.mGl = drawable.getGL().getGL2();
		
		this.mGl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		this.mGl.glLoadIdentity();
		
		this.updateScene();
		this.renderDebug();
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
			mGl.glMultMatrixf(camera.getWorldTransform().getBuffer(),
							  camera.getWorldTransform().getBufferOffset());
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
		public void init(Material material) {
			//VOID
		}
	}

	protected class TextureRenderer extends Texture.Renderer{
		@Override
		public void render(Texture texture) {
			int id = texture.getDiffuseTextureId();
			if(id != -1){
				com.jogamp.opengl.util.texture.Texture t = mTextures.get(id);				mGl.glColor4f(1,1,1,1);
				t.enable();
				t.bind();
			}
		}
		
		@Override
		public void init(Texture texture) {
			try{
				TextureData textureData = Assets.getInstance().openTexture(texture.getDiffuseTextureName());
				mTextures.add(TextureIO.newTexture(textureData));
				texture.setDiffuseTextureId(mTextures.size()-1);
			} catch(Exception e){
				texture.setDiffuseTextureId(-1);
			}
			
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
	
	protected class VertexIndicesRenderer extends VertexIndices.Renderer {

		@Override
		public void render(VertexIndices vi) {
			mGl.glFrontFace(GL2.GL_CCW);
			mGl.glEnable(GL2.GL_CULL_FACE);
			
			renderVertexBuffer(vi.getVertexBuffer());
			
			IndexBuffer indexBuffer = vi.getIndexBuffer();
			
			mGl.glDrawElements(mPrimitiveTypes[vi.getType()], 
							  indexBuffer.getCount(), 
							  mIndexTypes[indexBuffer.getType()], 
							  indexBuffer.getBuffer());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}

	protected class VertexListRenderer extends VertexList.Renderer{
		@Override
		public void render(VertexList vl) {
			mGl.glFrontFace(GL2.GL_CCW);
			mGl.glEnable(GL2.GL_CULL_FACE);
			
			renderVertexBuffer(vl.getVertexBuffer());
			mGl.glDrawArrays(mPrimitiveTypes[vl.getType()], 0, vl.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			mGl.glDisable(GL2.GL_CULL_FACE);
		}
	}
}
