package com.tespirit.bamboo.primitives;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

import com.tespirit.bamboo.render.ComponentRenderer;
import com.tespirit.bamboo.vectors.AxisAlignedBox;
import com.tespirit.bamboo.modifiers.VertexModifier;

/**
 * This is a standard mesh class that has a vertex buffer and an index buffer.
 * @author Todd Espiritu Santo
 *
 */
public class TriangleIndices extends Primitive implements Externalizable{
	protected VertexBuffer vertexBuffer;
	protected IndexBuffer indexBuffer;
	protected ArrayList<VertexModifier> modifierStack;
	
	protected VertexBuffer topBuffer;
	
	public TriangleIndices(){
		
	}
	
	public TriangleIndices(VertexBuffer vb, IndexBuffer ib){
		this.vertexBuffer = vb;
		this.indexBuffer = ib;
		this.init();
	}
	
	public void init(){
		this.topBuffer = this.vertexBuffer;
		this.modifierStack = new ArrayList<VertexModifier>();
		this.renderAsTriangles();
	}
	
	public TriangleIndices(int indexCount, int vertexCount, int[] vertexTypes){
		this(new VertexBuffer(vertexCount, vertexTypes),
			 new IndexBuffer(indexCount, vertexCount));
	}
	
	public VertexBuffer getVertexBuffer(){
		return this.topBuffer;
	}
	
	public IndexBuffer getIndexBuffer(){
		return this.indexBuffer;
	}
	
	public void addModifier(VertexModifier vm){
		vm.setVertexBuffer(this.topBuffer);
		this.topBuffer = vm.getModifiedBuffer();
		this.modifierStack.add(vm);
	}
	
	public void updateModifiers(){
		for(int i = 0; i < this.modifierStack.size(); i++){
			this.modifierStack.get(i).update();
		}
	}

	@Override
	public void computeBoundingBox(AxisAlignedBox boundingBox) {
		this.vertexBuffer.computeBoundingBox(boundingBox);
	}
	
	public void renderWireFrame(){
		this.renderAsLineStrip();
	}
	
	public void renderSolid(){
		this.renderAsTriangleStrip();
	}
	
	@Override
	public void render() {
		TriangleIndices.renderer.render(this);
	}
	
	private static Renderer renderer;
	
	public static abstract class Renderer implements ComponentRenderer{
		public void activate(){
			TriangleIndices.renderer = this;
		}
		
		public abstract void render(TriangleIndices triangles);
	}

	//IO
	private static final long serialVersionUID = -4424119892618303511L;
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.vertexBuffer = (VertexBuffer)in.readObject();
    	this.indexBuffer = (IndexBuffer)in.readObject();
    	this.init();
    	int modifiers = in.readInt();
    	for(int i = 0; i < modifiers; i++){
    		VertexModifier vm = (VertexModifier)in.readObject();
    		this.addModifier(vm);
    	}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.vertexBuffer);
		out.writeObject(this.indexBuffer);
		out.writeInt(this.modifierStack.size());
		for(VertexModifier vm : this.modifierStack){
			out.writeObject(vm);
		}
	}
}
