package com.tespirit.pandadroid.opengl1x;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.tespirit.bamboo.primitives.IndexBuffer;
import com.tespirit.bamboo.primitives.Primitive;
import com.tespirit.bamboo.primitives.VertexIndices;
import com.tespirit.bamboo.primitives.VertexList;
import com.tespirit.bamboo.primitives.VertexBuffer;
import com.tespirit.bamboo.render.Clock;
import com.tespirit.bamboo.render.RenderManager;
import com.tespirit.bamboo.scenegraph.Camera;
import com.tespirit.bamboo.scenegraph.Light;
import com.tespirit.bamboo.surfaces.Color;
import com.tespirit.bamboo.surfaces.Material;
import com.tespirit.bamboo.surfaces.Texture;
import com.tespirit.bamboo.vectors.Color4;
import com.tespirit.bamboo.vectors.Matrix3d;
import com.tespirit.pandadroid.app.Assets;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Renderer extends RenderManager implements android.opengl.GLSurfaceView.Renderer{
	protected GL10 mGl;
	protected int mCurrentLightId;
	
	private int[] mIndexTypes;
	private int[] mPrimitiveTypes;
	
	public Renderer() {
		this(new AndroidClock());
	}
	
	public Renderer(Clock clock){
		super(clock);
		this.mCurrentLightId = 0;
		
		this.mIndexTypes = new int[IndexBuffer.TYPE_COUNT];
		this.mIndexTypes[IndexBuffer.BUFFER16] = GL10.GL_UNSIGNED_SHORT;
		this.mIndexTypes[IndexBuffer.BUFFER8]  = GL10.GL_UNSIGNED_BYTE;
		
		this.mPrimitiveTypes = new int[Primitive.TYPE_COUNT];
		this.mPrimitiveTypes[Primitive.TRIANGLES] = GL10.GL_TRIANGLES;
		this.mPrimitiveTypes[Primitive.TRIANGLE_STRIP] = GL10.GL_TRIANGLE_STRIP;
		this.mPrimitiveTypes[Primitive.TRIANGLE_FAN] = GL10.GL_TRIANGLE_FAN;
		this.mPrimitiveTypes[Primitive.LINES] = GL10.GL_LINES;
		this.mPrimitiveTypes[Primitive.LINE_STRIP] = GL10.GL_LINE_STRIP;
		this.mPrimitiveTypes[Primitive.POINTS] = GL10.GL_POINTS;
		
		this.createRenderers();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		this.mGl = gl;
		this.mGl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		this.mGl.glLoadIdentity();
		
		this.updateScene();
		this.renderScene();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.mGl = gl;
		this.setDisplay(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.mGl = gl;
		
		this.mGl.glClearColor(this.mBackgroundColor.getRed(), 
							 this.mBackgroundColor.getGreen(),
							 this.mBackgroundColor.getBlue(),
							 this.mBackgroundColor.getAlpha());
		
		this.mGl.glClearDepthf(1.0f);
		this.mGl.glShadeModel(GL10.GL_SMOOTH);
		this.mGl.glEnable(GL10.GL_DEPTH_TEST);
		this.mGl.glDepthFunc(GL10.GL_LEQUAL);
		this.mGl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		this.mGl.glCullFace(GL10.GL_BACK);
		
		this.initRender();
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

	@Override
	protected void enableLights() {
		this.mGl.glEnable(GL10.GL_LIGHTING);
		this.mGl.glEnable(GL10.GL_COLOR_MATERIAL);
		this.mGl.glEnable(GL10.GL_NORMALIZE);
	}
	
	@Override
	protected void disableLights(){
		this.mGl.glDisable(GL10.GL_LIGHTING);
		this.mGl.glDisable(GL10.GL_COLOR_MATERIAL);
		this.mGl.glDisable(GL10.GL_NORMALIZE);
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
	
	protected class LightRenderer extends Light.Renderer{
		@Override
		public void render(Light light) {
			int id = light.getLightId();
	        mGl.glLightfv(id, GL10.GL_AMBIENT, light.getAmbient().getBuffer(), light.getAmbient().getBufferOffset());
	        mGl.glLightfv(id, GL10.GL_DIFFUSE, light.getDiffuse().getBuffer(), light.getDiffuse().getBufferOffset());
	        mGl.glLightfv(id, GL10.GL_SPECULAR, light.getSpecular().getBuffer(), light.getSpecular().getBufferOffset());
			mGl.glLightfv(id, GL10.GL_POSITION, light.getWorldPosition().getBuffer(), light.getWorldPosition().getBufferOffset());
		}
		
		@Override
		public void init(Light light) {
			enableLights();
			if(mCurrentLightId < GL10.GL_MAX_LIGHTS) {
				light.setLightId(GL10.GL_LIGHT0+mCurrentLightId);
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
			mGl.glMatrixMode(GL10.GL_PROJECTION);
			mGl.glLoadIdentity();
			mGl.glFrustumf(-camera.getNearWidth(), 
						  camera.getNearWidth(),
						  -camera.getNearHeight(),
						  camera.getNearHeight(), 
						  camera.getNear(), camera.getFar());
			mGl.glMatrixMode(GL10.GL_MODELVIEW);
			mGl.glLoadIdentity();
		}
	}
	
	protected class ColorRenderer extends Color.Renderer{
		@Override
		public void render(Color color) {
			mGl.glDisable(GL10.GL_TEXTURE_2D);
			mGl.glEnable(GL10.GL_COLOR_MATERIAL);
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
			mGl.glDisable(GL10.GL_COLOR_MATERIAL);
			mGl.glDisable(GL10.GL_TEXTURE_2D);
			mGl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, material.getAmbientBuffer());
			mGl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, material.getDiffuseBuffer());
			mGl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, material.getSpecularBuffer());
			mGl.glMaterialfv(GL10.GL_FRONT, GL10.GL_EMISSION, material.getEmissionBuffer());
		}

		@Override
		public void init(Material material) {
			//VOID
		}
	}

	protected class TextureRenderer extends Texture.Renderer{
		@Override
		public void render(Texture texture) {
			mGl.glEnable(GL10.GL_TEXTURE_2D);
			mGl.glEnable(GL10.GL_COLOR_MATERIAL);
			mGl.glColor4f(1,1,1,1);
			mGl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getDiffuseTextureId());
		}
		
		@Override
		public void init(Texture texture) {
			Bitmap bitmap = Assets.getManager().openBitmap(texture.getDiffuseTextureName());
			if(bitmap == null){
				texture.setDiffuseTextureId(0);
				return;
			}
			
			int[] tempTextures = new int[1];
			mGl.glGenTextures(1, tempTextures, 0);
			texture.setDiffuseTextureId(tempTextures[0]);
			
			// currently only mip mapping is supported. 
			mGl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getDiffuseTextureId());
			mGl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			mGl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);

			if(mGl instanceof GL11) {
				mGl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			} else {
				this.buildMipmap(bitmap);
			}
			
			bitmap.recycle();
		}
		
		/**
		 * Original code: Savas Ziplies and Mike Miller
		 * @param mGl
		 * @param bitmap
		 */
		private void buildMipmap(Bitmap bitmap) {
			//
			int level = 0;
			//
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();

			//
			while(height >= 1 || width >= 1) {
				//First of all, generate the texture from our bitmap and set it to the according level
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);
				
				//
				if(height == 1 || width == 1) {
					break;
				}

				//Increase the mipmap level
				level++;

				//
				height /= 2;
				width /= 2;
				Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height, true);
				
				//Clean up
				bitmap.recycle();
				bitmap = bitmap2;
			}
		}
	}
	
	protected void renderVertexBuffer(VertexBuffer vertexBuffer){
		
		this.mGl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		this.mGl.glVertexPointer(vertexBuffer.getStrideByType(VertexBuffer.POSITION), 
						   GL10.GL_FLOAT, 
						   0, 
						   vertexBuffer.getBufferByType(VertexBuffer.POSITION));
		
		if(vertexBuffer.hasType(VertexBuffer.NORMAL)){
			this.mGl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			this.mGl.glNormalPointer(GL10.GL_FLOAT, 
							   0, 
							   vertexBuffer.getBufferByType(VertexBuffer.NORMAL));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.TEXCOORD)){
			this.mGl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			this.mGl.glTexCoordPointer(vertexBuffer.getStrideByType(VertexBuffer.TEXCOORD), 
								 GL10.GL_FLOAT, 
								 0, 
								 vertexBuffer.getBufferByType(VertexBuffer.TEXCOORD));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.COLOR)){
			this.mGl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			this.mGl.glColorPointer(vertexBuffer.getStrideByType(VertexBuffer.COLOR), 
						 	  	   GL10.GL_FLOAT, 
						 	 	   0, 
						 	 	   vertexBuffer.getBufferByType(VertexBuffer.COLOR));
		}
	}
	
	protected class VertexIndicesRenderer extends VertexIndices.Renderer {

		@Override
		public void render(VertexIndices vi) {
			mGl.glFrontFace(GL10.GL_CCW);
			mGl.glEnable(GL10.GL_CULL_FACE);
			
			VertexBuffer vb = vi.getVertexBuffer();
			renderVertexBuffer(vb);
			
			IndexBuffer indexBuffer = vi.getIndexBuffer();
			
			mGl.glDrawElements(mPrimitiveTypes[vi.getType()], 
							  indexBuffer.getCount(), 
							  mIndexTypes[indexBuffer.getType()], 
							  indexBuffer.getBuffer());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			
			mGl.glDisable(GL10.GL_CULL_FACE);
		}
	}

	protected class VertexListRenderer extends VertexList.Renderer{
		@Override
		public void render(VertexList vl) {
			mGl.glFrontFace(GL10.GL_CCW);
			mGl.glEnable(GL10.GL_CULL_FACE);
			
			VertexBuffer vb = vl.getVertexBuffer();
			renderVertexBuffer(vb);
			
			mGl.glDrawArrays(mPrimitiveTypes[vl.getType()], 0, vl.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
	}
}
