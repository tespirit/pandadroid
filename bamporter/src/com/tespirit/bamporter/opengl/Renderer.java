package com.tespirit.bamporter.opengl;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

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
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Light;
import com.tespirit.bamboo.scenegraph.Node;
import com.tespirit.bamboo.scenegraph.Model;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Material;
import com.tespirit.bamboo.surfaces.Surface;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.bamporter.app.Assets;
import com.tespirit.bamporter.editor.Util;

public class Renderer extends RenderManager implements GLEventListener{
	private GLCanvas mCanvas;
	private FPSAnimator mAnimator;
	private GL2 mGl;
	private CameraControl mCameraControl;
	
	private boolean mRenderBoundingBox;
	private boolean mRenderAxis;
	private Primitives.WireCube mBoundingBox = new Primitives.WireCube();
	private Primitives.Axis mAxis = new Primitives.Axis();
	private Color mBoundingBoxColor = new Color();
	private UVTextureViewer mTexViewer;
	
	private int[] mIndexTypes;
	private int[] mPrimitiveTypes;
	private int[] mBlendSource;
	private int[] mBlendDest;
	
	private ArrayList<com.jogamp.opengl.util.texture.Texture> mTextures;
	
	private static GLCapabilities mGlCapabilities;
	private static GLProfile mGlProfile;
	
	public static void initGl(){
		GLProfile.initSingleton();
		mGlProfile = GLProfile.getDefault();
		mGlCapabilities = new GLCapabilities(mGlProfile);
		Assets.setGlProfile(mGlProfile);
	}
	
	public Renderer(java.awt.Color background){
		super(new CalendarClock(), new Color4());
		this.mCurrentLightId = 0;
		
		Color4 bgColor = new Color4();
		bgColor.set(background.getRed(), background.getGreen(), background.getBlue());
		this.setBackground(bgColor);
		
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
		
		this.mBlendDest = new int[Surface.BLEND_TYPE_COUNT];
		this.mBlendSource = new int[Surface.BLEND_TYPE_COUNT];
		this.mBlendDest[Surface.BLEND_ALPHA] = GL2.GL_ONE_MINUS_SRC_ALPHA;
		this.mBlendSource[Surface.BLEND_ALPHA] = GL2.GL_SRC_ALPHA;
		this.mBlendDest[Surface.BLEND_ADD] = GL2.GL_ONE;
		this.mBlendSource[Surface.BLEND_ADD] = GL2.GL_SRC_COLOR;
		
		this.createRenderers();
		
		this.mTextures = new ArrayList<com.jogamp.opengl.util.texture.Texture>();
		
		//create a default camera!
		Camera camera = new Camera();
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
				case '3':
					String name = Util.promptString("Enter the node name to view the uvs.");
					if(name != null){
						Node node = Node.getNode(name);
						if(node instanceof Model){
							mTexViewer = new UVTextureViewer((Model)node);
						}
					} else {
						mTexViewer = null;
					}
				}
			}
			
		});
		
		//add a camera listener!
		this.mCameraControl = new CameraControl(this);
		this.mCanvas.addMouseMotionListener(this.mCameraControl);
		this.mCanvas.addMouseListener(this.mCameraControl);
		this.mCanvas.addMouseWheelListener(this.mCameraControl);
		
		this.mAnimator = new FPSAnimator(this.mCanvas, 30);
		this.mAnimator.add(this.mCanvas);
		this.mAnimator.start();
		
		//debug stuff
		this.mBoundingBoxColor.setColor(1, 1, 0);
	}
	
	public Component getView(){
		return this.mCanvas;
	}

	protected void createRenderers(){
		//create renderers!
		this.addComponentRenderer(new VertexIndicesRenderer());
		this.addComponentRenderer(new VertexListRenderer());
		this.addComponentRenderer(new MaterialRenderer());
		this.addComponentRenderer(new LightRenderer());
		this.addComponentRenderer(new CameraRenderer());
		this.addComponentRenderer(new TextureRenderer());
		this.addComponentRenderer(new ColorRenderer());
	}
	
	public void setBackground(java.awt.Color color){
		this.setBackground(new Color4(color.getRed(), color.getGreen(), color.getBlue()));
	}

	@Override
	protected void enableLights() {
		this.mGl.glEnable(GL2.GL_LIGHTING);
		this.mGl.glEnable(GL2.GL_COLOR_MATERIAL);
		this.mGl.glEnable(GL2.GL_NORMALIZE);
	}
	
	@Override
	protected void disableLights(){
		this.mGl.glDisable(GL2.GL_LIGHTING);
		this.mGl.glDisable(GL2.GL_COLOR_MATERIAL);
		this.mGl.glDisable(GL2.GL_NORMALIZE);
	}
	
	@Override
	protected void setBackgroundColor(Color4 color) {
		this.mGl.glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	@Override
	protected void enableTextures() {
		//VOID
	}
	
	@Override
	protected void disableTextures(){
		//VOID
	}

	@Override
	protected void popMatrix() {
		this.mGl.glPopMatrix();
	}

	@Override
	protected void pushMatrix(Matrix3d transform) {
		this.mGl.glPushMatrix();
		this.mGl.glMultMatrixf(transform.getBuffer(), transform.getBufferOffset());
	}

	private Matrix3d mTexPos = new Matrix3d();
	private void renderDebug(){
		this.mGl.glDisable(GL2.GL_LIGHTING);
		this.mGl.glDisable(GL2.GL_TEXTURE_2D);
		this.pushMatrix(this.getCamera().getWorldTransform());
		for (Iterator<Node> i = this.getSceneIterator(); i.hasNext();){
			this.drawNodeInfo(i.next());
		}
		this.popMatrix();
		if(this.lightsEnabled()){
			this.mGl.glEnable(GL2.GL_LIGHTING);
		}
	}
	
	
	protected void drawNodeInfo(Node node){
		if(node.getWorldTransform() != null){
			this.pushMatrix(node.getWorldTransform());
		}
		
		if(node.getBoundingBox() != null && this.mRenderBoundingBox){
			this.mBoundingBox.setBox(node.getBoundingBox());
			this.mBoundingBoxColor.renderStart();
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
		
		this.mGl.glDrawBuffer(GL2.GL_BACK);
		
		this.mGl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		this.mGl.glLoadIdentity();
		
		this.updateScene();
		this.renderDebug();
		this.renderScene();
		if(this.mTexViewer != null){
			this.mTexPos.getTranslation().setZ(-2.0f);
			this.mGl.glDisable(GL2.GL_DEPTH_TEST);
			this.pushMatrix(this.mTexPos);
			this.mTexViewer.render();
			this.popMatrix();
			this.mGl.glEnable(GL2.GL_DEPTH_TEST);
		}
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
		this.initRender();
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
		public void init(Light light) {
			enableLights();
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
			mGl.glLoadMatrixf(camera.getProjection().getBuffer(), camera.getProjection().getBufferOffset());
			mGl.glDepthRangef(camera.getNear(), camera.getFar());
			
			mGl.glMatrixMode(GL2.GL_MODELVIEW);
			mGl.glLoadIdentity();
		}
	}
	
	private void renderSurfaceStart(Surface surface){
		if(surface.enableBlending()){
			mGl.glEnable(GL2.GL_BLEND);
			byte blendMethod = surface.getBlendMethod();
			mGl.glBlendFunc(mBlendSource[blendMethod], mBlendDest[blendMethod]);
			mGl.glDepthMask(false);
		}
	}
	
	private void renderSurfaceEnd(Surface surface){
		mGl.glDisable(GL2.GL_BLEND);
		mGl.glDepthMask(true);
	}
	
	protected class ColorRenderer extends Color.Renderer{
		@Override
		public void renderStart(Color color) {
			mGl.glEnable(GL2.GL_COLOR_MATERIAL);
			renderSurfaceStart(color);
			Color4 c = color.getColor();
			mGl.glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
		}

		@Override
		public void setup(Color color) {
			//VOID
		}

		@Override
		public void renderEnd(Color color) {
			renderSurfaceEnd(color);
			mGl.glDisable(GL2.GL_COLOR_MATERIAL);
		}
	}

	protected class MaterialRenderer extends Material.Renderer{
		@Override
		public void renderStart(Material material) {
			renderSurfaceStart(material);
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, material.getAmbientBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, material.getDiffuseBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, material.getSpecularBuffer());
			mGl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, material.getEmissionBuffer());
		}

		@Override
		public void init(Material material) {
			//VOID
		}

		@Override
		public void renderEnd(Material material) {
			renderSurfaceEnd(material);
		}
	}

	protected class TextureRenderer extends Texture.Renderer{
		@Override
		public void renderStart(Texture texture) {
			int id = texture.getDiffuseTextureId();
			if(id != -1){
				com.jogamp.opengl.util.texture.Texture t = mTextures.get(id);
				mGl.glColor4f(1, 1, 1, 1);
				mGl.glEnable(GL2.GL_COLOR_MATERIAL);
				renderSurfaceStart(texture);
				t.enable();
				t.bind();
			} else {
				mGl.glColor4f(0, 1, 1, 1);
				mGl.glEnable(GL2.GL_COLOR_MATERIAL);
			}
		}
		
		@Override
		public void init(Texture texture) {
			try{
				TextureData textureData = Assets.openTexture(texture.getDiffuseTextureName());
				com.jogamp.opengl.util.texture.Texture t = TextureIO.newTexture(textureData);
				t.setTexParameteri(GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);

				t.setTexParameteri(GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
				mTextures.add(t);
				texture.setDiffuseTextureId(mTextures.size()-1);
			} catch(Exception e){
				texture.setDiffuseTextureId(-1);
			}
			
		}

		@Override
		public void renderEnd(Texture texture) {
			renderSurfaceEnd(texture);
			mGl.glDisable(GL2.GL_COLOR_MATERIAL);
			mGl.glDisable(GL2.GL_TEXTURE_2D);
		}
	}
	
	protected void renderVertexBuffer(VertexBuffer vertexBuffer){
		
		this.mGl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		this.mGl.glVertexPointer(vertexBuffer.getStrideByType(VertexBuffer.POSITION), 
						   GL2.GL_FLOAT, 
						   0, 
						   vertexBuffer.getBufferByType(VertexBuffer.POSITION));
		
		if(vertexBuffer.hasType(VertexBuffer.NORMAL)){
			this.mGl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			this.mGl.glNormalPointer(GL2.GL_FLOAT, 
							   0, 
							   vertexBuffer.getBufferByType(VertexBuffer.NORMAL));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.TEXCOORD)){
			this.mGl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			this.mGl.glTexCoordPointer(vertexBuffer.getStrideByType(VertexBuffer.TEXCOORD), 
								 GL2.GL_FLOAT, 
								 0, 
								 vertexBuffer.getBufferByType(VertexBuffer.TEXCOORD));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.COLOR)){
			this.mGl.glEnableClientState(GL2.GL_COLOR_ARRAY);
			this.mGl.glColorPointer(vertexBuffer.getStrideByType(VertexBuffer.COLOR), 
						 	  	   GL2.GL_FLOAT, 
						 	 	   0, 
						 	 	   vertexBuffer.getBufferByType(VertexBuffer.COLOR));
		}
	}
	
	protected class VertexIndicesRenderer extends VertexIndices.Renderer {

		@Override
		public void render(VertexIndices vi) {
			
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
		}
	}

	protected class VertexListRenderer extends VertexList.Renderer{
		@Override
		public void render(VertexList vl) {
			renderVertexBuffer(vl.getVertexBuffer());
			mGl.glDrawArrays(mPrimitiveTypes[vl.getType()], 0, vl.getVertexBuffer().getCount());

			mGl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL2.GL_COLOR_ARRAY);
		}
	}
}
