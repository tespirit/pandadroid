package com.tespirit.panda3d.opengl1x;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.tespirit.panda3d.core.Assets;
import com.tespirit.panda3d.primitives.IndexBuffer;
import com.tespirit.panda3d.primitives.LineIndices;
import com.tespirit.panda3d.primitives.LineList;
import com.tespirit.panda3d.primitives.Primitive;
import com.tespirit.panda3d.primitives.TriangleIndices;
import com.tespirit.panda3d.primitives.TriangleList;
import com.tespirit.panda3d.primitives.VertexBuffer;
import com.tespirit.panda3d.render.Camera;
import com.tespirit.panda3d.render.Light;
import com.tespirit.panda3d.surfaces.Material;
import com.tespirit.panda3d.surfaces.Texture;
import com.tespirit.panda3d.vectors.Matrix3d;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Renderer extends com.tespirit.panda3d.render.Renderer implements android.opengl.GLSurfaceView.Renderer{
	private GL10 gl;
	private int currentLightId;
	Matrix3d modelView;
	
	public Renderer() {
		this.currentLightId = 0;
		modelView = new Matrix3d();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		this.gl = gl;
		this.renderScene();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.gl = gl;
		this.setDisplay(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.gl = gl;
		this.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		this.gl.glClearDepthf(1.0f);
		this.gl.glShadeModel(GL10.GL_SMOOTH);
		this.gl.glEnable(GL10.GL_DEPTH_TEST);
		this.gl.glDepthFunc(GL10.GL_LEQUAL);
		this.gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		this.gl.glCullFace(GL10.GL_BACK);
		
		IndexBuffer.setTypeEnum(0, GL10.GL_UNSIGNED_SHORT, GL10.GL_UNSIGNED_BYTE);
		Primitive.setTypeEnums(GL10.GL_TRIANGLES, GL10.GL_TRIANGLE_STRIP, GL10.GL_TRIANGLE_FAN, GL10.GL_LINES);	
		
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
		this.addComponentRenderer(new LineIndicesRenderer());
		this.addComponentRenderer(new LineListRenderer());
	}

	@Override
	public void enableLights() {
		this.gl.glEnable(GL10.GL_LIGHTING);
		this.gl.glEnable(GL10.GL_COLOR_MATERIAL);
		this.gl.glEnable(GL10.GL_NORMALIZE);
	}

	@Override
	public void enableTextures() {
		//VOID
	}

	@Override
	public void popMatrix() {
		this.gl.glPopMatrix();
	}

	@Override
	public void pushMatrix(Matrix3d transform) {
		this.gl.glPushMatrix();
		this.gl.glMultMatrixf(transform.getBuffer(), transform.getBufferOffset());
	}

	class LightRenderer extends Light.Renderer{
		@Override
		public void render(Light light) {
			int id = light.getLightId();
	        gl.glLightfv(id, GL10.GL_AMBIENT, light.getAmbientBuffer());
	        gl.glLightfv(id, GL10.GL_DIFFUSE, light.getDiffuseBuffer());
	        gl.glLightfv(id, GL10.GL_SPECULAR, light.getSpecularBuffer());
			gl.glLightfv(id, GL10.GL_POSITION, light.getPositionBuffer());
		}
		
		@Override
		public void setup(Light light) {
			if(currentLightId < GL10.GL_MAX_LIGHTS) {
				light.setLightId(GL10.GL_LIGHT0+currentLightId);
				gl.glEnable(light.getLightId());
				currentLightId++;
			}
		}
	}
	
	class CameraRenderer extends Camera.Renderer{
		@Override
		public void render(Camera camera) {
			gl.glMultMatrixf(camera.getTransform().getBuffer(),camera.getTransform().getBufferOffset());
			gl.glMultMatrixf(camera.getPivotTransform().getBuffer(),camera.getPivotTransform().getBufferOffset());
		}
		
		@Override
		public void setDisplay(Camera camera, int width, int height) {
			if(height == 0){
				height = 1;
			}
			
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glFrustumf(-camera.getNearWidth(), 
						  camera.getNearWidth(),
						  -camera.getNearHeight(),
						  camera.getNearHeight(), 
						  camera.getNear(), camera.getFar());
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
		}
	}

	class MaterialRenderer extends Material.Renderer{
		@Override
		public void render(Material material) {
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, material.getAmbientBuffer());
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, material.getDiffuseBuffer());
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, material.getSpecularBuffer());
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_EMISSION, material.getEmissionBuffer());
		}

		@Override
		public void setup(Material material) {
			//VOID
		}
	}

	class TextureRenderer extends Texture.Renderer{
		@Override
		public void render(Texture texture) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getDiffuseTextureId());
		}
		
		@Override
		public void setup(Texture texture) {
			Bitmap bitmap = Assets.getManager().openBitmap(texture.getDiffuseTextureName());
			if(bitmap == null){
				return;
			}
			
			int[] tempTextures = new int[1];
			gl.glGenTextures(1, tempTextures, 0);
			texture.setDiffuseTextureId(tempTextures[0]);
			
			// currently only mip mapping is supported. 
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getDiffuseTextureId());
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);

			if(gl instanceof GL11) {
				gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			} else {
				this.buildMipmap(bitmap);
			}
			
			bitmap.recycle();
		}
		
		/**
		 * Original code: Savas Ziplies and Mike Miller
		 * @param gl
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
	
	private void renderVertexBuffer(VertexBuffer vertexBuffer){
		
		this.gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		this.gl.glVertexPointer(vertexBuffer.getStride(VertexBuffer.POSITION), 
						   GL10.GL_FLOAT, 
						   0, 
						   vertexBuffer.getBuffer(VertexBuffer.POSITION));
		
		if(vertexBuffer.hasType(VertexBuffer.NORMAL)){
			this.gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
			this.gl.glNormalPointer(GL10.GL_FLOAT, 
							   0, 
							   vertexBuffer.getBuffer(VertexBuffer.NORMAL));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.TEXCOORD)){
			this.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			this.gl.glTexCoordPointer(vertexBuffer.getStride(VertexBuffer.TEXCOORD), 
								 GL10.GL_FLOAT, 
								 0, 
								 vertexBuffer.getBuffer(VertexBuffer.TEXCOORD));
		}
		
		if(vertexBuffer.hasType(VertexBuffer.COLOR)){
			this.gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			this.gl.glColorPointer(vertexBuffer.getStride(VertexBuffer.COLOR), 
						 	  	   GL10.GL_FLOAT, 
						 	 	   0, 
						 	 	   vertexBuffer.getBuffer(VertexBuffer.COLOR));
		}
	}
	
	class TriangleIndicesRenderer extends TriangleIndices.Renderer {

		@Override
		public void render(TriangleIndices triangles) {
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnable(GL10.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			
			IndexBuffer indexBuffer = triangles.getIndexBuffer();
			
			gl.glDrawElements(triangles.getTypeEnum(), 
							  indexBuffer.getCount(), 
							  indexBuffer.getTypeEnum(), 
							  indexBuffer.getBuffer());
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}

	class TriangleListRenderer extends TriangleList.Renderer{
		@Override
		public void render(TriangleList triangles) {
			gl.glFrontFace(GL10.GL_CCW);
			gl.glEnable(GL10.GL_CULL_FACE);
			
			renderVertexBuffer(triangles.getVertexBuffer());
			gl.glDrawArrays(triangles.getTypeEnum(), 0, triangles.getVertexBuffer().getCount());
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}
	
	class LineIndicesRenderer extends LineIndices.Renderer{
		@Override
		public void render(LineIndices lines) {
			
			renderVertexBuffer(lines.getVertexBuffer());
			
			IndexBuffer indexBuffer = lines.getIndexBuffer();
			gl.glDrawElements(lines.getTypeEnum(), 
					  indexBuffer.getCount(), 
					  indexBuffer.getTypeEnum(), 
					  indexBuffer.getBuffer());
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}
	
	class LineListRenderer extends LineList.Renderer{
		@Override
		public void render(LineList lines) {
			renderVertexBuffer(lines.getVertexBuffer());
			gl.glDrawArrays(lines.getTypeEnum(), 0, lines.getVertexBuffer().getCount());
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			gl.glDisable(GL10.GL_CULL_FACE);
		}
	}
}
