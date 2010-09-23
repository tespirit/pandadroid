package com.tespirit.panda3d.opengl1x;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.tespirit.panda3d.core.Assets;
import com.tespirit.panda3d.primitives.IndexBuffer;
import com.tespirit.panda3d.primitives.LineIndices;
import com.tespirit.panda3d.primitives.LineList;
import com.tespirit.panda3d.primitives.Points;
import com.tespirit.panda3d.primitives.Primitive;
import com.tespirit.panda3d.primitives.TriangleIndices;
import com.tespirit.panda3d.primitives.TriangleList;
import com.tespirit.panda3d.primitives.VertexBuffer;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.render.Light;
import com.tespirit.panda3d.surfaces.Color;
import com.tespirit.panda3d.surfaces.Material;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.vectors.Color4;
import com.tespirit.panda3d.vectors.Matrix3d;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Renderer extends com.tespirit.panda3d.render.Renderer implements android.opengl.GLSurfaceView.Renderer{
	protected GL10 mGl;
	protected int mCurrentLightId;
	
	public Renderer() {
		super();
		this.mCurrentLightId = 0;
		
		IndexBuffer.setTypeEnum(0, 
								GL10.GL_UNSIGNED_SHORT, 
								GL10.GL_UNSIGNED_BYTE);
		
		Primitive.setTypeEnums(GL10.GL_TRIANGLES, 
							   GL10.GL_TRIANGLE_STRIP, 
							   GL10.GL_TRIANGLE_FAN, 
							   GL10.GL_LINES,
							   GL10.GL_LINE_STRIP,
							   GL10.GL_POINTS);
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
		
		this.mGl.glClearColor(this.backgroundColor.getRed(), 
							 this.backgroundColor.getGreen(),
							 this.backgroundColor.getBlue(),
							 this.backgroundColor.getAlpha());
		
		this.mGl.glClearDepthf(1.0f);
		this.mGl.glShadeModel(GL10.GL_SMOOTH);
		this.mGl.glEnable(GL10.GL_DEPTH_TEST);
		this.mGl.glDepthFunc(GL10.GL_LEQUAL);
		this.mGl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		this.mGl.glCullFace(GL10.GL_BACK);
		
		this.createRenderers();
		
		this.setupRender();
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
		this.mGl.glEnable(GL10.GL_LIGHTING);
		this.mGl.glEnable(GL10.GL_COLOR_MATERIAL);
		this.mGl.glEnable(GL10.GL_NORMALIZE);
	}
	
	@Override
	public void disableLights(){
		this.mGl.glDisable(GL10.GL_LIGHTING);
		this.mGl.glDisable(GL10.GL_COLOR_MATERIAL);
		this.mGl.glDisable(GL10.GL_NORMALIZE);
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
		public void setup(Light light) {
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
			mGl.glMultMatrixf(camera.getTransform().getBuffer(),camera.getTransform().getBufferOffset());
			mGl.glMultMatrixf(camera.getPivotTransform().getBuffer(),camera.getPivotTransform().getBufferOffset());
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
		public void setup(Material material) {
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
		public void setup(Texture texture) {
			Bitmap bitmap = Assets.getManager().openBitmap(texture.getDiffuseTextureName());
			if(bitmap == null){
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
		this.mGl.glVertexPointer(vertexBuffer.getStride(VertexBuffer.POSITION), 
						   GL10.GL_FLOAT, 
						   0, 
						   vertexBuffer.getBuffer(VertexBuffer.POSITION));
		
		if(vertexBuffer.hasType(VertexBuffer.NORMAL)){
			this.mGl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			this.mGl.glNormalPointer(GL10.GL_FLOAT, 
							   0, 
							   vertexBuffer.getBuffer(VertexBuffer.NORMAL));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.TEXCOORD)){
			this.mGl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			this.mGl.glTexCoordPointer(vertexBuffer.getStride(VertexBuffer.TEXCOORD), 
								 GL10.GL_FLOAT, 
								 0, 
								 vertexBuffer.getBuffer(VertexBuffer.TEXCOORD));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.COLOR)){
			this.mGl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			this.mGl.glColorPointer(vertexBuffer.getStride(VertexBuffer.COLOR), 
						 	  	   GL10.GL_FLOAT, 
						 	 	   0, 
						 	 	   vertexBuffer.getBuffer(VertexBuffer.COLOR));
		}
	}
	
	protected class TriangleIndicesRenderer extends TriangleIndices.Renderer {

		@Override
		public void render(TriangleIndices triangles) {
			mGl.glFrontFace(GL10.GL_CCW);
			mGl.glEnable(GL10.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			
			IndexBuffer indexBuffer = triangles.getIndexBuffer();
			
			mGl.glDrawElements(triangles.getTypeEnum(), 
							  indexBuffer.getCount(), 
							  indexBuffer.getTypeEnum(), 
							  indexBuffer.getBuffer());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			mGl.glDisable(GL10.GL_CULL_FACE);
		}
	}

	protected class TriangleListRenderer extends TriangleList.Renderer{
		@Override
		public void render(TriangleList triangles) {
			mGl.glFrontFace(GL10.GL_CCW);
			mGl.glEnable(GL10.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			mGl.glDrawArrays(triangles.getTypeEnum(), 0, triangles.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			mGl.glDisable(GL10.GL_CULL_FACE);
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
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			mGl.glDisable(GL10.GL_CULL_FACE);
		}
	}
	
	protected class LineListRenderer extends LineList.Renderer{
		@Override
		public void render(LineList lines) {
			renderVertexBuffer(lines.getVertexBuffer());
			mGl.glDrawArrays(lines.getTypeEnum(), 0, lines.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			mGl.glDisable(GL10.GL_CULL_FACE);
		}
	}
	
	protected class PointsRenderer extends Points.Renderer{
		@Override
		public void render(Points points) {
			renderVertexBuffer(points.getVertexBuffer());
			mGl.glDrawArrays(points.getTypeEnum(), 0, points.getVertexBuffer().getCount());
			
			mGl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			mGl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			mGl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			mGl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			mGl.glDisable(GL10.GL_CULL_FACE);
		}
	}
}
