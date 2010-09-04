package com.tespirit.panda3d.geometry;

import javax.microedition.khronos.opengles.GL10;

import com.tespirit.panda3d.geometry.Geometry;;

/**
 * This is a standard mesh class that has a vertex buffer and an index buffer.
 * @author Todd Espiritu Santo
 *
 */
public class Mesh extends Geometry {
	
	protected VertexBuffer vertexBuffer;
	protected IndexBuffer indexBuffer;
	
	//render flags
	private int cullDirection;
	
	public Mesh(){
		this.cullDirection = GL10.GL_CCW;
	}

	@Override
	public void render(GL10 gl) {
		gl.glFrontFace(this.cullDirection);
		gl.glEnable(GL10.GL_CULL_FACE);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glVertexPointer(this.vertexBuffer.getStride(VertexBuffer.POSITION), 
						   GL10.GL_FLOAT, 
						   0, 
						   this.vertexBuffer.getBuffer(VertexBuffer.POSITION));
		
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glNormalPointer(GL10.GL_FLOAT, 
						   0, 
						   this.vertexBuffer.getBuffer(VertexBuffer.NORMAL));
		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(this.vertexBuffer.getStride(VertexBuffer.TEXCOORD), 
							 GL10.GL_FLOAT, 
							 0, 
							 this.vertexBuffer.getBuffer(VertexBuffer.TEXCOORD));
		
		if(this.vertexBuffer.hasType(VertexBuffer.COLOR)){
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(this.vertexBuffer.getStride(VertexBuffer.COLOR), 
						 	  GL10.GL_FLOAT, 
						 	  0, 
						 	  this.vertexBuffer.getBuffer(VertexBuffer.COLOR));
		}
		
		gl.glDrawElements(GL10.GL_TRIANGLES, 
						  this.indexBuffer.getCount(), 
						  this.indexBuffer.getType(), 
						  this.indexBuffer.getBuffer());
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}
}
