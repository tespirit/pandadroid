package com.tespirit.bamboo.primitives;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.tespirit.bamboo.render.ComponentRenderer;

/**
 * This is a standard mesh class that has a vertex buffer and an index buffer.
 * @author Todd Espiritu Santo
 *
 */
public class VertexIndices extends VertexList implements Externalizable{
	protected IndexBuffer mIndexBuffer;
	
	public VertexIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this(new VertexBuffer(vertexCount, vertexTypes),
			 new IndexBuffer(indexCount, vertexCount));
	}
	
	public VertexIndices(VertexBuffer vb, IndexBuffer ib){
		super(vb);
		this.mIndexBuffer = ib;
	}
	
	public VertexIndices(){
		super();
	}
	
	public IndexBuffer getIndexBuffer(){
		return this.mIndexBuffer;
	}
	
	@Override
	public void render() {
		this.mIndexBuffer.lock();
		this.mVertexBuffer.lock();
		RENDERER.render(this);
		this.mVertexBuffer.unlock();
		this.mIndexBuffer.unlock();
	}
	
	private static Renderer RENDERER;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			VertexIndices.RENDERER = this;
		}
		
		public abstract void render(VertexIndices vi);
	}

	//IO
	private static final long serialVersionUID = -4424119892618303511L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		//index buffer first, just incase later there's a need for an index buffer
		//modifier
		this.mIndexBuffer = (IndexBuffer)in.readObject();
		super.readExternal(in);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		//index buffer first, just incase later there's a need for an index buffer
		//modifier
		out.writeObject(this.mIndexBuffer);
		super.writeExternal(out);
	}
}
